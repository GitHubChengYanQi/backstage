package cn.atsoft.dasheng.purchase.service.impl;


import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.auth.model.LoginUser;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.form.entity.ActivitiProcess;
import cn.atsoft.dasheng.form.entity.ActivitiProcessTask;
import cn.atsoft.dasheng.form.model.params.ActivitiProcessTaskParam;
import cn.atsoft.dasheng.form.service.ActivitiProcessLogService;
import cn.atsoft.dasheng.form.service.ActivitiProcessService;
import cn.atsoft.dasheng.form.service.ActivitiProcessTaskService;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.purchase.entity.ProcurementPlan;
import cn.atsoft.dasheng.purchase.entity.ProcurementPlanBind;
import cn.atsoft.dasheng.purchase.entity.ProcurementPlanDetal;
import cn.atsoft.dasheng.purchase.entity.PurchaseListing;
import cn.atsoft.dasheng.purchase.mapper.ProcurementPlanMapper;
import cn.atsoft.dasheng.purchase.model.params.ProcurementPlanBindParam;
import cn.atsoft.dasheng.purchase.model.params.ProcurementPlanDetalParam;
import cn.atsoft.dasheng.purchase.model.params.ProcurementPlanParam;
import cn.atsoft.dasheng.purchase.model.result.ProcurementPlanDetalResult;
import cn.atsoft.dasheng.purchase.model.result.ProcurementPlanResult;
import cn.atsoft.dasheng.purchase.service.*;
import cn.atsoft.dasheng.core.util.ToolUtil;
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

/**
 * <p>
 * 采购计划主表 服务实现类
 * </p>
 *
 * @author song
 * @since 2021-12-21
 */
@Service
public class ProcurementPlanServiceImpl extends ServiceImpl<ProcurementPlanMapper, ProcurementPlan> implements ProcurementPlanService {

    @Autowired
    private ProcurementPlanDetalService procurementPlanDetalService;

    @Autowired
    private ProcurementPlanBindService bindService;

    @Autowired
    private UserService userService;
    @Autowired
    private ActivitiProcessService activitiProcessService;
    @Autowired
    private ActivitiProcessTaskService activitiProcessTaskService;

    @Autowired
    private ActivitiProcessLogService activitiProcessLogService;

    @Autowired
    private PurchaseListingService purchaseListingService;

    @Override
    @Transactional
    public void add(ProcurementPlanParam param) {
        ProcurementPlan entity = getEntity(param);
        this.save(entity);

        param.setProcurementPlanId(entity.getProcurementPlanId());
        procurementPlanDetalService.batchAdd(param);
        bindService.batchAdd(param);

        /**
         * 创建采购计划申请审批流程
         */
        LoginUser user = LoginContextHolder.getContext().getUser();
        ActivitiProcess activitiProcess = activitiProcessService.query().eq("type", "purchase").eq("status", 99).eq("module", "purchasePlan").one();
        if (ToolUtil.isNotEmpty(activitiProcess)) {
            ActivitiProcessTaskParam activitiProcessTaskParam = new ActivitiProcessTaskParam();
            activitiProcessTaskParam.setTaskName(user.getName() + "发起的采购计划申请 (" + param.getProcurementPlanName() + ")");
            activitiProcessTaskParam.setFormId(entity.getProcurementPlanId());
            activitiProcessTaskParam.setType("purchasePlan");
            activitiProcessTaskParam.setProcessId(activitiProcess.getProcessId());
            Long taskId = activitiProcessTaskService.add(activitiProcessTaskParam);
            //添加log
            activitiProcessLogService.addLog(activitiProcess.getProcessId(), taskId);
            activitiProcessLogService.autoAudit(taskId, 1);
        }

    }

    @Override
    public void delete(ProcurementPlanParam param) {
        param.setDisplay(0);
        ProcurementPlan newEntity = getEntity(param);
        this.updateById(newEntity);
    }

    @Override
    public void update(ProcurementPlanParam param) {
        ProcurementPlan oldEntity = getOldEntity(param);
        ProcurementPlan newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        if (oldEntity.getDisplay().equals(newEntity.getDisplay())) {
            this.updateById(newEntity);
            if (newEntity.getStatus()==99) {

            }
        }
    }

    @Override
    public void updateState(ActivitiProcessTask processTask) {
        processTask.getFormId();
        ProcurementPlan procurementPlan = this.getById(processTask.getFormId());
        procurementPlan.setStatus(98);
        this.updateById(procurementPlan);
    }
    @Override
    public void updateRefuseStatus(ActivitiProcessTask processTask) {

        ProcurementPlan procurementPlan = this.getById(processTask.getFormId());
        procurementPlan.setStatus(97);
        this.updateById(procurementPlan);
        //如果采购申请拒绝   将采购申请 里面的 物料数据 回滚
        //从bind表中查找出对应购买申请的 数据
        List<ProcurementPlanBind> detailList = bindService.lambdaQuery().eq(ProcurementPlanBind::getProcurementPlanId, processTask.getFormId()).list();
        List<Long> askDetailIds = new ArrayList<>();
        for (ProcurementPlanBind procurementPlanBind : detailList) {
            askDetailIds.add(procurementPlanBind.getAskDetailId());
        }
        //取购买申请里查出数据状态
        List<PurchaseListing> purchaseListings = purchaseListingService.list(new QueryWrapper<PurchaseListing>() {{
            in("purchase_listing_id", askDetailIds);
        }});
        //修改为未创建的状态
        for (PurchaseListing purchaseListing : purchaseListings) {
            purchaseListing.setStatus(0);
        }
        //更新
        purchaseListingService.updateBatchById(purchaseListings);

    }

    @Override
    public ProcurementPlanResult findBySpec(ProcurementPlanParam param) {
        return null;
    }

    @Override
    public List<ProcurementPlanResult> findListBySpec(ProcurementPlanParam param) {
        return null;
    }

    @Override
    public PageInfo<ProcurementPlanResult> findPageBySpec(ProcurementPlanParam param) {
        Page<ProcurementPlanResult> pageContext = getPageContext();
        IPage<ProcurementPlanResult> page = this.baseMapper.customPageList(pageContext, param);
        format(page.getRecords());
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(ProcurementPlanParam param) {
        return param.getProcurementPlanId();
    }

    private Page<ProcurementPlanResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private ProcurementPlan getOldEntity(ProcurementPlanParam param) {
        return this.getById(getKey(param));
    }

    private ProcurementPlan getEntity(ProcurementPlanParam param) {
        ProcurementPlan entity = new ProcurementPlan();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

    private void format(List<ProcurementPlanResult> data) {
        List<Long> planIds = new ArrayList<>();
        List<Long> userIds = new ArrayList<>();
        for (ProcurementPlanResult datum : data) {
            planIds.add(datum.getProcurementPlanId());
            userIds.add(datum.getCreateUser());
        }

        List<User> userList = userIds.size() == 0 ? new ArrayList<>() : userService.listByIds(userIds);

        for (ProcurementPlanResult datum : data) {

            for (User user : userList) {
                if (user.getUserId().equals(datum.getCreateUser())) {
                    datum.setUser(user);
                    break;
                }
            }
        }
    }
}
