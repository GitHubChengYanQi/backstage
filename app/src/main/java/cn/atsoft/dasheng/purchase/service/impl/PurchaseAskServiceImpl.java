package cn.atsoft.dasheng.purchase.service.impl;


import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.auth.model.LoginUser;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.CodingRules;
import cn.atsoft.dasheng.erp.service.CodingRulesService;
import cn.atsoft.dasheng.form.entity.*;
import cn.atsoft.dasheng.form.model.params.ActivitiProcessTaskParam;
import cn.atsoft.dasheng.form.pojo.ViewUpdate;
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
import cn.atsoft.dasheng.sendTemplate.WxCpSendTemplate;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    private CodingRulesService rulesService;
    @Autowired
    private WxCpSendTemplate wxCpSendTemplate;
    @Autowired
    private StepsService stepsSer;


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
//        //防止重复添加sku
//        List<Long> longs = param.getPurchaseListingParams().stream().map(PurchaseListingParam::getSkuId).distinct().collect(Collectors.toList());
//        if (longs.size() != param.getPurchaseListingParams().size()) {
//            throw new ServiceException(500, "单据中出现重复物料");
//        }

        int totalCount = 0;
        int totalType = param.getPurchaseListings().size();
        List<PurchaseListing> purchaseListings = new ArrayList<>();
        //添加采购清单
        for (PurchaseListingParam purchaseListingParam : param.getPurchaseListings()) {
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
            activitiProcessTaskParam.setType("purchaseAsk");
            activitiProcessTaskParam.setProcessId(activitiProcess.getProcessId());
            Long taskId = activitiProcessTaskService.add(activitiProcessTaskParam);
            //添加小铃铛
            wxCpSendTemplate.setSource("processTask");
            wxCpSendTemplate.setSourceId(taskId);
            //添加log
            activitiProcessLogService.addLogJudgeBranch(activitiProcess.getProcessId(), taskId, entity.getPurchaseAskId(), "purchaseAsk");
            activitiProcessLogService.autoAudit(taskId, 1);
        } else {
            entity.setStatus(2);
            this.updateById(entity);
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
        this.format(page.getRecords()
        );
        return PageFactory.createPageInfo(page);
    }

    public void format(List<PurchaseAskResult> param) {
        List<Long> userIds = new ArrayList<>();
        List<Long> askIds = new ArrayList<>();
        for (PurchaseAskResult purchaseAskResult : param) {
            userIds.add(purchaseAskResult.getCreateUser());
            askIds.add(purchaseAskResult.getPurchaseAskId());
        }


        List<PurchaseListing> purchaseListings = askIds.size() == 0 ? new ArrayList<>() : purchaseListingService.query().in("purchase_ask_id", askIds).eq("display", 1).list();
        List<PurchaseListingResult> resultList = BeanUtil.copyToList(purchaseListings, PurchaseListingResult.class, new CopyOptions());
        purchaseListingService.format(resultList);

        List<User> userList = userIds.size() == 0 ? new ArrayList<>() : userService.listByIds(userIds);
        for (PurchaseAskResult purchaseAskResult : param) {
            for (User user : userList) {
                if (purchaseAskResult.getCreateUser().equals(user.getUserId())) {
                    purchaseAskResult.setCreateUserName(user.getName());
                }
            }
            int type = 0;
            int number = 0;
            for (PurchaseListing purchaseListing : purchaseListings) {
                if (purchaseListing.getPurchaseAskId().equals(purchaseAskResult.getPurchaseAskId())) {
                    type = type + 1;
                    number = Math.toIntExact(number + purchaseListing.getApplyNumber());
                }
            }
            ViewUpdate viewUpdate = stepsSer.getProcessTime(purchaseAskResult.getPurchaseAskId());
            purchaseAskResult.setViewUpdate(viewUpdate);
            purchaseAskResult.setApplyNumber(number);
            purchaseAskResult.setApplyType(type);

            List<PurchaseListingResult> results = new ArrayList<>();
            for (PurchaseListingResult listingResult : resultList) {
                if (listingResult.getPurchaseAskId().equals(purchaseAskResult.getPurchaseAskId())) {
                    results.add(listingResult);
                }
            }
            purchaseAskResult.setPurchaseListings(results);
        }

    }

    @Override
    public PurchaseAskResult detail(PurchaseAskParam param) {
        PurchaseAsk detail = this.getById(param.getPurchaseAskId());
        PurchaseAskResult result = new PurchaseAskResult();
        ToolUtil.copyProperties(detail, result);
        User user = userService.getById(result.getCreateUser());
        result.setUser(user);
        int number = 0;
        List<PurchaseListingResult> purchaseListing = purchaseListingService.getByAskId(param.getPurchaseAskId());
        for (PurchaseListingResult listingResult : purchaseListing) {
            number = Math.toIntExact(number + listingResult.getApplyNumber());
        }
        result.setPurchaseListings(purchaseListing);
        result.setApplyType(purchaseListing.size());
        result.setApplyNumber(number);
        return result;
    }

    /**
     * 修改采购申请状态
     * <p>
     * //     * @param taskId
     * //     * @param status
     */
    @Override
    public void updateStatus(ActivitiProcessTask param) {
        if (param.getType().equals("purchaseAsk")) {
            PurchaseAsk ask = this.getById(param.getFormId());
            ask.setStatus(2);
            this.updateById(ask);
        }
    }

    /**
     * 驳回状态更改方法
     *
     * @param param
     */
    @Override
    public void updateRefuseStatus(ActivitiProcessTask param) {
        if (param.getType().equals("purchaseAsk")) {
            PurchaseAsk ask = this.getById(param.getFormId());
            ask.setStatus(1);
            this.updateById(ask);
        }
    }

    /**
     * 采购申请驳回
     *
     * @param askId
     */
    @Override
    public void rejected(Long askId) {
        ActivitiProcessTask processTask = activitiProcessTaskService.query().eq("form_id", askId).one();
        if (ToolUtil.isEmpty(processTask)) {
            throw new ServiceException(500, "当前采购申请没有流程任务");
        }
        PurchaseAsk ask = this.getById(askId);
        ask.setStatus(3);
        this.updateById(ask);

        List<ActivitiProcessLog> processLogs = activitiProcessLogService.query().eq("task_id", processTask).list();
        for (ActivitiProcessLog processLog : processLogs) {
            processLog.setStatus(0);
        }
        activitiProcessLogService.updateBatchById(processLogs);
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

    @Override
    public void complateAsk(Long processTaskId) {
        ActivitiProcessTask processTask = activitiProcessTaskService.getById(processTaskId);
        PurchaseAsk purchaseAsk = this.getById(processTask.getFormId());
        purchaseAsk.setStatus(2);
        this.updateById(purchaseAsk);
        activitiProcessLogService.autoAudit(processTaskId, 1);
    }

    @Override
    public List<PurchaseAskResult> getResults(List<Long> askIds) {
        List<PurchaseAskResult> askResults = new ArrayList<>();

        if (ToolUtil.isEmpty(askIds)) {
            return askResults;
        }

        List<PurchaseAsk> purchaseAsks = this.listByIds(askIds);
        List<Long> userIds = new ArrayList<>();
        for (PurchaseAsk purchaseAsk : purchaseAsks) {
            PurchaseAskResult askResult = new PurchaseAskResult();
            ToolUtil.copyProperties(purchaseAsk, askResult);
            askResults.add(askResult);
            userIds.add(purchaseAsk.getCreateUser());
        }
        List<User> users = userIds.size() == 0 ? new ArrayList<>() : userService.listByIds(userIds);

        for (PurchaseAskResult askResult : askResults) {
            for (User user : users) {
                if (user.getUserId().equals(askResult.getCreateUser())) {
                    askResult.setUser(user);
                    break;
                }
            }
        }
        return askResults;
    }
    @Override
    public List<PurchaseAskResult> listResultByIds(List<Long> ids){
        List<PurchaseAsk> purchaseAsks = ids.size() == 0 ? new ArrayList<>() : this.listByIds(ids);
        List<PurchaseAskResult> results = new ArrayList<>();
        for (PurchaseAsk purchaseAsk : purchaseAsks) {
            PurchaseAskResult result = new PurchaseAskResult();
            ToolUtil.copyProperties(purchaseAsk,result);
            results.add(result);
        }
        return results;
    }
}
