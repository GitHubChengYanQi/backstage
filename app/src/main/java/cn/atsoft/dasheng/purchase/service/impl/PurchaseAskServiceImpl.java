package cn.atsoft.dasheng.purchase.service.impl;


import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.auth.model.LoginUser;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.CodingRules;
import cn.atsoft.dasheng.erp.service.CodingRulesService;
import cn.atsoft.dasheng.form.entity.*;
import cn.atsoft.dasheng.form.model.params.ActivitiProcessTaskParam;
import cn.atsoft.dasheng.form.model.result.ActivitiProcessLogResult;
import cn.atsoft.dasheng.form.pojo.AuditRule;
import cn.atsoft.dasheng.form.service.*;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.purchase.entity.PurchaseAsk;
import cn.atsoft.dasheng.purchase.entity.PurchaseListing;
import cn.atsoft.dasheng.purchase.mapper.PurchaseAskMapper;
import cn.atsoft.dasheng.purchase.model.params.PurchaseAskParam;
import cn.atsoft.dasheng.purchase.model.params.PurchaseListingParam;
import cn.atsoft.dasheng.purchase.model.result.PurchaseAskResult;
import cn.atsoft.dasheng.purchase.model.result.PurchaseListingResult;
import cn.atsoft.dasheng.purchase.service.PurchaseAskService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.purchase.service.PurchaseListingService;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static cn.atsoft.dasheng.form.pojo.StepsType.START;

/**
 * <p>
 * 采购申请 服务实现类
 * </p>
 *
 * @author song
 * @since 2021-12-15
 */
@Service
public class PurchaseAskServiceImpl extends ServiceImpl<PurchaseAskMapper, PurchaseAsk> implements PurchaseAskService {
    @Autowired
    private UserService userService;
    @Autowired
    private PurchaseListingService purchaseListingService;
    @Autowired
    private ActivitiProcessLogService activitiProcessLogService;
    @Autowired
    private ActivitiProcessTaskService activitiProcessTaskService;
    @Autowired
    private ActivitiProcessService activitiProcessService;
    @Autowired
    private ActivitiStepsService stepsService;
    @Autowired
    private ActivitiAuditService auditService;
    @Autowired
    private ActivitiProcessTaskService taskService;
    @Autowired
    private ActivitiProcessLogService logService;
    @Autowired
    private CodingRulesService rulesService;

    @Override
    @Transactional
    public void add(PurchaseAskParam param) {
        //创建编码规则
        CodingRules rules = rulesService.query().eq("coding_rules_id", param.getCoding()).one();
        if (ToolUtil.isNotEmpty(rules)) {
            String backCoding = rulesService.backCoding(Long.valueOf(param.getCoding()));
            param.setCoding(backCoding);
        }
        PurchaseAsk entity = getEntity(param);
        this.save(entity);
        //防止重复添加sku
        List<Long> longs = param.getPurchaseListingParams().stream().map(PurchaseListingParam::getSkuId).distinct().collect(Collectors.toList());
        if (longs.size() != param.getPurchaseListingParams().size()) {
            throw new ServiceException(500, "单据中出现重复物料");
        }

        int totalCount = 0;
        int totalType = param.getPurchaseListingParams().size();
        List<PurchaseListing> purchaseListings = new ArrayList<>();
        //添加采购清单
        for (PurchaseListingParam purchaseListingParam : param.getPurchaseListingParams()) {
            totalCount += purchaseListingParam.getApplyNumber();
            purchaseListingParam.setPurchaseAskId(entity.getPurchaseAskId());
            PurchaseListing purchaseListing = new PurchaseListing();
            ToolUtil.copyProperties(purchaseListingParam, purchaseListing);
            purchaseListings.add(purchaseListing);
        }
        purchaseListingService.saveBatch(purchaseListings);
        entity.setTypeNumber((long) totalType);
        entity.setNumber((long) totalCount);
        this.updateById(entity);

        //发起审批流程
        ActivitiProcess activitiProcess = activitiProcessService.query().eq("type", "purchase").eq("status", 99).eq("module", "purchaseAsk").one();
        if (ToolUtil.isNotEmpty(activitiProcess)) {
            this.power(activitiProcess);//检查创建权限
            LoginUser user = LoginContextHolder.getContext().getUser();
            ActivitiProcessTaskParam activitiProcessTaskParam = new ActivitiProcessTaskParam();
            activitiProcessTaskParam.setTaskName(user.getName() + "发起的采购申请 (" + param.getCoding() + ")");
            activitiProcessTaskParam.setQTaskId(entity.getPurchaseAskId());
            activitiProcessTaskParam.setUserId(param.getCreateUser());
            activitiProcessTaskParam.setFormId(entity.getPurchaseAskId());
            activitiProcessTaskParam.setType("purchase");
            activitiProcessTaskParam.setProcessId(activitiProcess.getProcessId());
            Long taskId = activitiProcessTaskService.add(activitiProcessTaskParam);
            //添加log
            activitiProcessLogService.addLogJudgeBranch(activitiProcess.getProcessId(), taskId, entity.getPurchaseAskId(), "purchaseAsk");
            activitiProcessLogService.autoAudit(taskId, 1);
        } else {
            throw new ServiceException(500, "请创建质检流程！");
        }

    }

    private void power(ActivitiProcess activitiProcess) {
        ActivitiSteps startSteps = stepsService.query().eq("process_id", activitiProcess.getProcessId()).eq("type", START).one();
        if (ToolUtil.isNotEmpty(startSteps)) {
            ActivitiAudit audit = auditService.query().eq("setps_id", startSteps.getSetpsId()).one();
            if (!stepsService.checkUser(audit.getRule())) {
                throw new ServiceException(500, "您没有权限创建任务");
            }
        }
    }

    @Override
    public void update(PurchaseAskParam param) {
        PurchaseAsk oldEntity = getOldEntity(param);
        PurchaseAsk newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public PurchaseAskResult findBySpec(PurchaseAskParam param) {
        return null;
    }

    @Override
    public List<PurchaseAskResult> findListBySpec(PurchaseAskParam param) {
        return null;
    }

    @Override
    public PageInfo<PurchaseAskResult> findPageBySpec(PurchaseAskParam param) {
        Page<PurchaseAskResult> pageContext = getPageContext();
        IPage<PurchaseAskResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    public void format(List<PurchaseAskResult> param) {
        List<Long> userIds = new ArrayList<>();
        for (PurchaseAskResult purchaseAskResult : param) {
            userIds.add(purchaseAskResult.getCreateUser());
        }
        List<User> userList = userService.listByIds(userIds);
        for (PurchaseAskResult purchaseAskResult : param) {
            for (User user : userList) {
                if (purchaseAskResult.getCreateUser().equals(user.getUserId())) {
                    purchaseAskResult.setCreateUserName(user.getName());
                }
            }
        }
    }

    @Override
    public PurchaseAskResult detail(PurchaseAskParam param) {
        PurchaseAsk detail = this.getById(param.getPurchaseAskId());
        PurchaseAskResult result = new PurchaseAskResult();
        ToolUtil.copyProperties(detail, result);


        List<PurchaseListingResult> purchaseListing = purchaseListingService.getByAskId(param.getPurchaseAskId());
        result.setPurchaseListingResults(purchaseListing);

        return result;
    }

    /**
     * 修改采购申请状态
     *
     * @param taskId
     * @param status
     */
    @Override
    public void updateStatus(Long taskId, Integer status) {
        ActivitiProcessTask task = taskService.getById(taskId);
        if (task.getType().equals("purchase")) {
            if (status == 0) {
                logService.autoAudit(taskId, 0);
                PurchaseAsk purchaseAsk = new PurchaseAsk();
                purchaseAsk.setStatus(1);
                this.update(purchaseAsk, new QueryWrapper<PurchaseAsk>() {{
                    eq("purchase_ask_id", task.getFormId());
                }});
            } else if (status == 1) {
                //判断完成状态
                List<ActivitiProcessLogResult> logAudit = logService.getLogAudit(taskId);
                for (ActivitiProcessLogResult logResult : logAudit) {
                    AuditRule rule = logResult.getActivitiAudit().getRule();
                    if (ToolUtil.isNotEmpty(rule) && rule.getType().toString().equals("purchase_complete")) {
                        if (logResult.getStatus() == 1) {
                            PurchaseAsk purchaseAsk = new PurchaseAsk();
                            purchaseAsk.setStatus(2);
                            this.update(purchaseAsk, new QueryWrapper<PurchaseAsk>() {{
                                eq("purchase_ask_id", task.getFormId());
                            }});
                        }
                    }
                }
            }
        }
    }

    private Serializable getKey(PurchaseAskParam param) {
        return param.getPurchaseAskId();
    }

    private Page<PurchaseAskResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private PurchaseAsk getOldEntity(PurchaseAskParam param) {
        return this.getById(getKey(param));
    }

    private PurchaseAsk getEntity(PurchaseAskParam param) {
        PurchaseAsk entity = new PurchaseAsk();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}