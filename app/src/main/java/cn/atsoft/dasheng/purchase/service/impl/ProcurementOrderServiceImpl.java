package cn.atsoft.dasheng.purchase.service.impl;


import cn.atsoft.dasheng.app.entity.*;
import cn.atsoft.dasheng.app.model.params.ContractParam;
import cn.atsoft.dasheng.app.service.*;
import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.auth.model.LoginUser;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.crm.entity.ContractClass;
import cn.atsoft.dasheng.crm.service.ContractClassService;
import cn.atsoft.dasheng.erp.config.MobileService;
import cn.atsoft.dasheng.erp.service.QualityTaskService;
import cn.atsoft.dasheng.form.entity.ActivitiProcess;
import cn.atsoft.dasheng.form.entity.ActivitiProcessTask;
import cn.atsoft.dasheng.form.model.params.ActivitiProcessTaskParam;
import cn.atsoft.dasheng.form.model.result.ActivitiProcessTaskResult;
import cn.atsoft.dasheng.form.service.*;
import cn.atsoft.dasheng.form.service.ActivitiProcessLogV1Service;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.purchase.entity.ProcurementOrder;
import cn.atsoft.dasheng.purchase.entity.ProcurementOrderDetail;
import cn.atsoft.dasheng.purchase.entity.ProcurementPlan;
import cn.atsoft.dasheng.purchase.entity.ProcurementPlanDetal;
import cn.atsoft.dasheng.purchase.mapper.ProcurementOrderMapper;
import cn.atsoft.dasheng.purchase.model.params.ProcurementOrderDetailParam;
import cn.atsoft.dasheng.purchase.model.params.ProcurementOrderParam;
import cn.atsoft.dasheng.purchase.model.result.ProcurementOrderResult;
import cn.atsoft.dasheng.purchase.service.ProcurementOrderDetailService;
import cn.atsoft.dasheng.purchase.service.ProcurementOrderService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.purchase.service.ProcurementPlanDetalService;
import cn.atsoft.dasheng.purchase.service.ProcurementPlanService;
import cn.atsoft.dasheng.sendTemplate.WxCpSendTemplate;

import cn.atsoft.dasheng.sys.modular.system.entity.User;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 采购单 服务实现类
 * </p>
 *
 * @author song
 * @since 2022-01-13
 */
@Service
public class ProcurementOrderServiceImpl extends ServiceImpl<ProcurementOrderMapper, ProcurementOrder> implements ProcurementOrderService {
    @Autowired
    private ProcurementOrderDetailService detailService;
    @Autowired
    private ProcurementPlanDetalService planDetailService;
    @Autowired
    private ContractService contractService;
    @Autowired
    private ProcurementPlanService planService;
    @Autowired
    private TemplateService templateService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private ContactsService contactsService;
    @Autowired
    private AdressService adressService;
    @Autowired
    private PhoneService phoneService;
    @Autowired
    private ContractDetailService contractDetailService;
    @Autowired
    private QualityTaskService qualityTaskService;
    @Autowired
    private ActivitiProcessService activitiProcessService;
    @Autowired
    private ActivitiProcessTaskService activitiProcessTaskService;
    @Autowired
    private ActivitiProcessLogV1Service activitiProcessLogService;
    @Autowired
    private WxCpSendTemplate wxCpSendTemplate;
    @Autowired
    private MobileService mobileService;
    @Autowired
    private UserService userService;

    @Override
    @Transactional
    public void add(ProcurementOrderParam param) throws Exception {
        ProcurementOrder entity = getEntity(param);
        this.save(entity);

        ProcurementPlan procurementPlan = planService.getById(param.getProcurementPlanId());
        if (ToolUtil.isEmpty(procurementPlan)) {
            throw new ServiceException(500, "请确认采购计划");
        }

        List<ProcurementOrderDetailParam> params = param.getDetailParams();
        List<ProcurementOrderDetail> details = new ArrayList<>();
        Map<Long, List<ProcurementOrderDetailParam>> supplierMap = new HashMap<>();

        List<ProcurementPlanDetal> planDetals = new ArrayList<>();
        for (ProcurementOrderDetailParam procurementOrderDetailParam : params) {

            ProcurementPlanDetal planDetal = new ProcurementPlanDetal();//更改采购计划详情状态
            planDetal.setDetailId(procurementOrderDetailParam.getDetailId());
            planDetal.setStatus(procurementOrderDetailParam.getStatus());
            planDetals.add(planDetal);

            procurementOrderDetailParam.setProcurementOrderId(entity.getProcurementOrderId());
            ProcurementOrderDetail procurementOrderDetail = new ProcurementOrderDetail();
            ToolUtil.copyProperties(procurementOrderDetailParam, procurementOrderDetail);
            details.add(procurementOrderDetail);


            //过滤供应商
            List<ProcurementOrderDetailParam> detailParams = supplierMap.get(procurementOrderDetail.getCustomerId());
            if (ToolUtil.isEmpty(detailParams)) {
                List<ProcurementOrderDetailParam> detailParamList = new ArrayList<>();
                detailParamList.add(procurementOrderDetailParam);
                supplierMap.put(procurementOrderDetail.getCustomerId(), detailParamList);
            } else {
                detailParams.add(procurementOrderDetailParam);
                supplierMap.put(procurementOrderDetail.getCustomerId(), detailParams);
            }
        }
        planDetailService.updateBatchById(planDetals);
        planService.updateStatus(procurementPlan.getProcurementPlanId());

        //添加合同

//        List<Long> customerIds = new ArrayList<>();
//        for (Map.Entry<Long, List<ProcurementOrderDetailParam>> longListEntry : supplierMap.entrySet()) {
//            customerIds.add(longListEntry.getKey());
//        }
//        addContract(customerIds, supplierMap, entity.getProcurementOrderId());
        detailService.saveBatch(details);
        /**
         * 添加进入流程审批
         */
        LoginUser user = LoginContextHolder.getContext().getUser();
        ActivitiProcess activitiProcess = activitiProcessService.query().eq("type", "purchase").eq("status", 99).eq("module", "purchaseOrder").one();
        if (ToolUtil.isNotEmpty(activitiProcess)) {
            qualityTaskService.power(activitiProcess);//检查创建权限
            ActivitiProcessTaskParam activitiProcessTaskParam = new ActivitiProcessTaskParam();
            activitiProcessTaskParam.setTaskName(user.getName() + "新的采购单");
            activitiProcessTaskParam.setQTaskId(entity.getProcurementOrderId());
            activitiProcessTaskParam.setUserId(param.getCreateUser());
            activitiProcessTaskParam.setFormId(entity.getProcurementOrderId());
            activitiProcessTaskParam.setType("procurementOrder");
            activitiProcessTaskParam.setProcessId(activitiProcess.getProcessId());
            Long taskId = activitiProcessTaskService.add(activitiProcessTaskParam);
            //添加log
            activitiProcessLogService.addLog(activitiProcess.getProcessId(), taskId);
            activitiProcessLogService.autoAudit(taskId, 1,LoginContextHolder.getContext().getUserId());
            //添加小铃铛
            wxCpSendTemplate.setSource("procurementOrder");
            wxCpSendTemplate.setSourceId(taskId);
        } else {
            entity.setStatus(99L);
            this.updateById(entity);
            List<Contract> contractList = contractService.query().eq("source_id", entity.getProcurementOrderId()).eq("source", "采购单").list();
            for (Contract contract : contractList) {
                contract.setDisplay(1);
            }
            contractService.updateBatchById(contractList);
        }
    }

    @Override
    @Transactional
    public Long addOrder(ProcurementOrderParam param) throws Exception {
        ProcurementOrder entity = getEntity(param);
        this.save(entity);
        List<ProcurementOrderDetail> details = new ArrayList<>();
        if (ToolUtil.isNotEmpty(param.getContractId())) {
            Contract contract = new Contract();
            contract.setContractId(param.getContractId());
            contract.setSource("采购单");
            contract.setSourceId(entity.getProcurementOrderId());
            contractService.updateById(contract);
            List<ContractDetail> contractDetails = contractDetailService.lambdaQuery().eq(ContractDetail::getContractId, param.getContractId()).list();
            for (ContractDetail contractDetail : contractDetails) {
                ProcurementOrderDetail procurementOrderDetail = new ProcurementOrderDetail();
                procurementOrderDetail.setProcurementOrderId(entity.getProcurementOrderId());
                procurementOrderDetail.setBrandId(contractDetail.getBrandId());
                procurementOrderDetail.setSkuId(contractDetail.getSkuId());
                procurementOrderDetail.setCustomerId(contractDetail.getCustomerId());
                procurementOrderDetail.setMoney(contractDetail.getSalePrice());
                procurementOrderDetail.setNumber(Long.valueOf(contractDetail.getQuantity()));
                details.add(procurementOrderDetail);
            }
        } else {
            List<ProcurementOrderDetailParam> params = param.getDetailParams();
            if (ToolUtil.isNotEmpty(param.getProcurementPlanId())) {
                ProcurementPlan procurementPlan = planService.getById(param.getProcurementPlanId());
                if (ToolUtil.isEmpty(procurementPlan)) {
                    throw new ServiceException(500, "请确认采购计划");
                }

                List<ProcurementPlanDetal> planDetals = new ArrayList<>();
                for (ProcurementOrderDetailParam procurementOrderDetailParam : params) {

                    ProcurementPlanDetal planDetal = new ProcurementPlanDetal();//更改采购计划详情状态
                    planDetal.setDetailId(procurementOrderDetailParam.getDetailId());
                    planDetal.setStatus(procurementOrderDetailParam.getStatus());
                    planDetals.add(planDetal);

                }
                planDetailService.updateBatchById(planDetals);
                planService.updateStatus(procurementPlan.getProcurementPlanId());
            }
            for (ProcurementOrderDetailParam procurementOrderDetailParam : params) {
                procurementOrderDetailParam.setProcurementOrderId(entity.getProcurementOrderId());
                ProcurementOrderDetail procurementOrderDetail = new ProcurementOrderDetail();
                ToolUtil.copyProperties(procurementOrderDetailParam, procurementOrderDetail);
                details.add(procurementOrderDetail);
            }
        }
        detailService.saveBatch(details);

        /**
         * 添加进入流程审批
         */
        LoginUser user = LoginContextHolder.getContext().getUser();
        ActivitiProcess activitiProcess = activitiProcessService.query().eq("type", "purchase").eq("status", 99).eq("module", "purchaseOrder").one();
        if (ToolUtil.isNotEmpty(activitiProcess)) {
            qualityTaskService.power(activitiProcess);//检查创建权限
            ActivitiProcessTaskParam activitiProcessTaskParam = new ActivitiProcessTaskParam();
            activitiProcessTaskParam.setTaskName(user.getName() + "新的采购单");
            activitiProcessTaskParam.setQTaskId(entity.getProcurementOrderId());
            activitiProcessTaskParam.setUserId(param.getCreateUser());
            activitiProcessTaskParam.setFormId(entity.getProcurementOrderId());
            activitiProcessTaskParam.setType("procurementOrder");
            activitiProcessTaskParam.setProcessId(activitiProcess.getProcessId());
            Long taskId = activitiProcessTaskService.add(activitiProcessTaskParam);
            //添加log
            activitiProcessLogService.addLog(activitiProcess.getProcessId(), taskId);
            activitiProcessLogService.autoAudit(taskId, 1,LoginContextHolder.getContext().getUserId());
            //添加小铃铛
            wxCpSendTemplate.setSource("processTask");
            wxCpSendTemplate.setSourceId(taskId);
        } else {
            entity.setStatus(99L);
            this.updateById(entity);
            List<Contract> contractList = contractService.query().eq("source_id", entity.getProcurementOrderId()).eq("source", "采购单").list();
            for (Contract contract : contractList) {
                contract.setDisplay(1);
            }
            contractService.updateBatchById(contractList);
        }

        return entity.getProcurementOrderId();
    }

    @Override
    public void delete(ProcurementOrderParam param) {
        this.removeById(getKey(param));
    }

    @Override
    public void update(ProcurementOrderParam param) {
        ProcurementOrder oldEntity = getOldEntity(param);
        ProcurementOrder newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public ProcurementOrderResult findBySpec(ProcurementOrderParam param) {
        return null;
    }

    @Override
    public List<ProcurementOrderResult> findListBySpec(ProcurementOrderParam param) {
        return null;
    }

    @Override
    public PageInfo<ProcurementOrderResult> findPageBySpec(ProcurementOrderParam param) {
        Page<ProcurementOrderResult> pageContext = getPageContext();
        IPage<ProcurementOrderResult> page = this.baseMapper.customPageList(pageContext, param);
        format(page.getRecords());
        return PageFactory.createPageInfo(page);
    }





    private Serializable getKey(ProcurementOrderParam param) {
        return param.getProcurementOrderId();
    }

    private Page<ProcurementOrderResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private ProcurementOrder getOldEntity(ProcurementOrderParam param) {
        return this.getById(getKey(param));
    }

    private ProcurementOrder getEntity(ProcurementOrderParam param) {
        ProcurementOrder entity = new ProcurementOrder();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

    @Override
    public void updateStatus(ActivitiProcessTask processTask) {
        ProcurementOrder entity = this.getById(processTask.getFormId());
        entity.setStatus(99L);
        this.updateById(entity);
        List<Contract> contractList = contractService.query().eq("source_id", processTask.getFormId()).eq("source", "采购单").list();
        for (Contract contract : contractList) {
            contract.setDisplay(1);
        }
        contractService.updateBatchById(contractList);

    }

    @Override
    public void updateRefuseStatus(ActivitiProcessTask processTask) {
        ProcurementOrder entity = this.getById(processTask.getFormId());
        entity.setStatus(97L);
        this.updateById(entity);
    }

    private void format(List<ProcurementOrderResult> data) {
        List<Long> userIds = new ArrayList<>();

        for (ProcurementOrderResult orderResult : data) {
            userIds.add(orderResult.getCreateUser());
        }
        List<User> users = userIds.size() == 0 ? new ArrayList<>() : userService.listByIds(userIds);

        for (ProcurementOrderResult orderResult : data) {
            for (User user : users) {
                if (orderResult.getCreateUser().equals(user.getUserId())) {
                    orderResult.setUser(user);
                    break;
                }
            }
        }
    }
}
