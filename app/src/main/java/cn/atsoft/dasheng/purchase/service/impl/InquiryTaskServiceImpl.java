package cn.atsoft.dasheng.purchase.service.impl;


import cn.atsoft.dasheng.app.entity.CrmCustomerLevel;
import cn.atsoft.dasheng.app.model.result.CustomerResult;
import cn.atsoft.dasheng.app.service.CrmCustomerLevelService;
import cn.atsoft.dasheng.app.service.CustomerService;
import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.auth.model.LoginUser;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.crm.service.SupplyService;
import cn.atsoft.dasheng.erp.config.MobileService;
import cn.atsoft.dasheng.erp.model.result.SkuResult;
import cn.atsoft.dasheng.erp.service.QualityTaskService;
import cn.atsoft.dasheng.form.entity.ActivitiProcess;
import cn.atsoft.dasheng.form.entity.ActivitiProcessTask;
import cn.atsoft.dasheng.form.model.params.ActivitiProcessTaskParam;
import cn.atsoft.dasheng.form.service.ActivitiProcessLogService;
import cn.atsoft.dasheng.form.service.ActivitiProcessService;
import cn.atsoft.dasheng.form.service.ActivitiProcessTaskService;
import cn.atsoft.dasheng.message.entity.MarkDownTemplate;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.purchase.entity.InquiryTask;
import cn.atsoft.dasheng.purchase.entity.InquiryTaskDetail;
import cn.atsoft.dasheng.purchase.entity.ProcurementOrder;
import cn.atsoft.dasheng.purchase.mapper.InquiryTaskMapper;
import cn.atsoft.dasheng.purchase.model.params.InquiryTaskDetailParam;
import cn.atsoft.dasheng.purchase.model.params.InquiryTaskParam;

import cn.atsoft.dasheng.purchase.model.result.InquiryTaskDetailResult;
import cn.atsoft.dasheng.purchase.model.result.InquiryTaskResult;
import cn.atsoft.dasheng.purchase.model.result.PurchaseQuotationResult;

import cn.atsoft.dasheng.purchase.service.InquiryTaskDetailService;
import cn.atsoft.dasheng.purchase.service.InquiryTaskService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.purchase.service.PurchaseQuotationService;
import cn.atsoft.dasheng.sendTemplate.WxCpSendTemplate;
import cn.atsoft.dasheng.sendTemplate.WxCpTemplate;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 询价任务 服务实现类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2021-12-23
 */
@Service
public class InquiryTaskServiceImpl extends ServiceImpl<InquiryTaskMapper, InquiryTask> implements InquiryTaskService {
    @Autowired
    private InquiryTaskDetailService detailService;

    @Autowired
    private UserService userService;

    @Autowired
    private CrmCustomerLevelService levelService;

    @Autowired
    private SupplyService supplyService;
    @Autowired
    private PurchaseQuotationService quotationService;

    @Autowired
    private QualityTaskService qualityTaskService;

    @Autowired
    private ActivitiProcessService activitiProcessService;

    @Autowired
    private ActivitiProcessTaskService activitiProcessTaskService;

    @Autowired
    private ActivitiProcessLogService activitiProcessLogService;

    @Autowired
    private WxCpSendTemplate wxCpSendTemplate;

    @Autowired
    private MobileService mobileService;

    @Override
    @Transactional
    public void add(InquiryTaskParam param) {
        InquiryTask entity = getEntity(param);







        this.save(entity);

        List<Long> skuIds = new ArrayList<>();
        for (InquiryTaskDetailParam detailParam : param.getDetailParams()) {
            skuIds.add(detailParam.getSkuId()+detailParam.getBrandId());
        }
        long count = skuIds.stream().distinct().count();
        if (param.getDetailParams().size() != count) {
            throw new ServiceException(500, "请不要添加重复物料");
        }

        List<InquiryTaskDetail> details = new ArrayList<>();
        for (InquiryTaskDetailParam detailParam : param.getDetailParams()) {
            InquiryTaskDetail taskDetail = new InquiryTaskDetail();
            ToolUtil.copyProperties(detailParam, taskDetail);
            taskDetail.setInquiryTaskId(entity.getInquiryTaskId());
            details.add(taskDetail);
        }
        detailService.saveBatch(details);


        LoginUser user = LoginContextHolder.getContext().getUser();
        ActivitiProcess activitiProcess = activitiProcessService.query().eq("type", "purchase").eq("status", 99).eq("module", "inquiry").one();
        if (ToolUtil.isNotEmpty(activitiProcess)) {
            qualityTaskService.power(activitiProcess);//检查创建权限
            ActivitiProcessTaskParam activitiProcessTaskParam = new ActivitiProcessTaskParam();
            activitiProcessTaskParam.setTaskName(user.getName() + "提交的询价任务"+param.getInquiryTaskCode());
            activitiProcessTaskParam.setQTaskId(entity.getInquiryTaskId());
            activitiProcessTaskParam.setUserId(param.getCreateUser());
            activitiProcessTaskParam.setFormId(entity.getInquiryTaskId());
            activitiProcessTaskParam.setType("inquiry");
            activitiProcessTaskParam.setProcessId(activitiProcess.getProcessId());
            Long taskId = activitiProcessTaskService.add(activitiProcessTaskParam);
            //添加log
            activitiProcessLogService.addLog(activitiProcess.getProcessId(), taskId);
            activitiProcessLogService.autoAudit(taskId, 1,LoginContextHolder.getContext().getUserId());
            //添加小铃铛
            wxCpSendTemplate.setSource("inquiry");
            wxCpSendTemplate.setSourceId(taskId);
        } else {
            entity.setStatus(98);
            this.updateById(entity);

            String url = mobileService.getMobileConfig().getUrl() + "/#/Receipts/ReceiptsDetail?id=" ;




            wxCpSendTemplate.sendMarkDownTemplate(new MarkDownTemplate() {{
                setTitle("新的采购单");
                setUrl(url);
                setDescription(user.getName() + "提交的采购申请");
                setCreateUser(entity.getCreateUser());
                setType(0);

                setUserIds(new ArrayList<Long>() {{
                    add(entity.getUserId());
                }});
            }});

        }
    }

    @Override
    public void delete(InquiryTaskParam param) {
        this.removeById(getKey(param));
    }

    @Override
    public void update(InquiryTaskParam param) {
        InquiryTask oldEntity = getOldEntity(param);
        InquiryTask newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public InquiryTaskResult findBySpec(InquiryTaskParam param) {
        return null;
    }

    @Override
    public List<InquiryTaskResult> findListBySpec(InquiryTaskParam param) {
        return null;
    }

    @Override
    public PageInfo<InquiryTaskResult> findPageBySpec(InquiryTaskParam param) {
        Page<InquiryTaskResult> pageContext = getPageContext();
        IPage<InquiryTaskResult> page = this.baseMapper.customPageList(pageContext, param);
        format(page.getRecords());
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(InquiryTaskParam param) {
        return param.getInquiryTaskId();
    }

    private Page<InquiryTaskResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private InquiryTask getOldEntity(InquiryTaskParam param) {
        return this.getById(getKey(param));
    }

    private InquiryTask getEntity(InquiryTaskParam param) {
        InquiryTask entity = new InquiryTask();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

    private void format(List<InquiryTaskResult> data) {
        List<Long> userIds = new ArrayList<>();
        List<Long> levelId = new ArrayList<>();
        List<Long> id = new ArrayList<>();
        for (InquiryTaskResult datum : data) {
            userIds.add(datum.getCreateUser());
            levelId.add(datum.getSupplierLevel());
            id.add(datum.getInquiryTaskId());
        }

        List<User> users = userIds.size() == 0 ? new ArrayList<>() : userService.listByIds(userIds);
        List<CrmCustomerLevel> crmCustomerLevels = levelId.size() == 0 ? new ArrayList<>() : levelService.listByIds(levelId);
        List<InquiryTaskDetail> taskDetails = id.size() == 0 ? new ArrayList<>() : detailService.lambdaQuery().in(InquiryTaskDetail::getInquiryTaskId, id).eq(InquiryTaskDetail::getDisplay, 1).list();

        for (InquiryTaskResult datum : data) {

            Integer number = 0;
            Integer type = 0;
            for (InquiryTaskDetail taskDetail : taskDetails) {
                if (taskDetail.getInquiryTaskId().equals(datum.getInquiryTaskId())) {
                    number = number + taskDetail.getTotal();
                    type++;
                }
            }
            datum.setNumber(number);
            datum.setType(type);

            for (User user : users) {
                if (ToolUtil.isNotEmpty(datum.getUserId()) && datum.getUserId().equals(user.getUserId())) {
                    datum.setUser(user);
                    break;
                }
            }
            for (CrmCustomerLevel crmCustomerLevel : crmCustomerLevels) {
                if (ToolUtil.isNotEmpty(datum.getSupplierLevel()) && crmCustomerLevel.getCustomerLevelId().equals(datum.getSupplierLevel())) {
                    datum.setCrmCustomerLevel(crmCustomerLevel);
                    break;
                }
            }
        }

    }

    @Override
    public InquiryTaskResult detail(Long taskId) {

        InquiryTask inquiryTask = this.getById(taskId);
        InquiryTaskResult taskResult = new InquiryTaskResult();
        ToolUtil.copyProperties(inquiryTask, taskResult);

        List<PurchaseQuotationResult> bySource = quotationService.getListBySource(taskResult.getInquiryTaskId());
        taskResult.setQuotationResults(bySource);


        User user = userService.getById(taskResult.getUserId());
        taskResult.setUser(user);

        User createUser = userService.getById(taskResult.getCreateUser());
        taskResult.setFounder(createUser);

        List<InquiryTaskDetailResult> detail = detailService.getDetailByInquiryId(taskResult.getInquiryTaskId()); //询价详情

        taskResult.setDetailResults(detail);

        return taskResult;
    }

    @Override
    public void updateStatus(ActivitiProcessTask processTask) {
        InquiryTask entity = this.getById(processTask.getFormId());
        entity.setStatus(98);
        this.updateById(entity);
    }

    @Override
    public void updateRefuseStatus(ActivitiProcessTask processTask) {
        InquiryTask entity = this.getById(processTask.getFormId());
        entity.setStatus(97);
        this.updateById(entity);
    }
}
