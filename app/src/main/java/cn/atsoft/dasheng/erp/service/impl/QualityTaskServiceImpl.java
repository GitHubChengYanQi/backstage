package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.app.entity.Brand;
import cn.atsoft.dasheng.app.model.result.BrandResult;
import cn.atsoft.dasheng.app.service.BrandService;
import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.auth.model.LoginUser;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.crm.entity.Data;
import cn.atsoft.dasheng.erp.entity.*;
import cn.atsoft.dasheng.erp.mapper.QualityTaskMapper;
import cn.atsoft.dasheng.erp.model.params.QualityTaskDetailParam;
import cn.atsoft.dasheng.erp.model.params.QualityTaskParam;
import cn.atsoft.dasheng.erp.model.request.FormDataPojo;
import cn.atsoft.dasheng.erp.model.request.FormValues;
import cn.atsoft.dasheng.erp.model.result.*;
import cn.atsoft.dasheng.erp.pojo.FormDataValueResult;
import cn.atsoft.dasheng.erp.pojo.QualityTaskChild;
import cn.atsoft.dasheng.erp.pojo.TaskComplete;
import cn.atsoft.dasheng.erp.service.*;
import cn.atsoft.dasheng.form.entity.*;
import cn.atsoft.dasheng.form.model.params.ActivitiProcessTaskParam;
import cn.atsoft.dasheng.form.model.result.FormDataResult;

import cn.atsoft.dasheng.form.service.*;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.orCode.entity.OrCodeBind;
import cn.atsoft.dasheng.orCode.model.result.BackCodeRequest;
import cn.atsoft.dasheng.orCode.service.OrCodeBindService;
import cn.atsoft.dasheng.orCode.service.OrCodeService;
import cn.atsoft.dasheng.sendTemplate.WxCpSendTemplate;
import cn.atsoft.dasheng.sendTemplate.WxCpTemplate;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

import static cn.atsoft.dasheng.form.pojo.StepsType.START;

/**
 * <p>
 * 质检任务 服务实现类
 * </p>
 *
 * @author
 * @since 2021-11-16
 */
@Service
public class QualityTaskServiceImpl extends ServiceImpl<QualityTaskMapper, QualityTask> implements QualityTaskService {
    @Autowired
    QualityTaskDetailService detailService;
    @Autowired
    private SkuService skuService;
    @Autowired
    private FormDataService formDataService;

    @Autowired
    private FormDataValueService formDataValueService;

    @Autowired
    private QualityCheckService qualityCheckService;

    @Autowired
    private OrCodeBindService bindService;

    @Autowired
    private WxCpSendTemplate wxCpSendTemplate;

    @Autowired
    private OrCodeService orCodeService;

    @Autowired
    private InkindService inkindService;

    @Autowired
    private QualityPlanDetailService qualityPlanDetailService;

    @Autowired
    private UserService userService;

    @Autowired
    private CodingRulesService rulesService;

    @Autowired
    private QualityTaskBindService taskBindService;

    @Autowired
    private BrandService brandService;

    @Autowired
    private ActivitiProcessTaskService activitiProcessTaskService;

    @Autowired
    private ActivitiProcessLogService activitiProcessLogService;

    @Autowired
    private ActivitiProcessService activitiProcessService;

    @Autowired
    private ActivitiStepsService stepsService;

    @Autowired
    private ActivitiAuditService auditService;

    @Autowired
    private QualityPlanService planService;

    @Autowired
    private QualityCheckService checkService;


    @Override
    @Transactional
    public void add(QualityTaskParam param) {
        //创建编码规则
        CodingRules rules = rulesService.query().eq("coding_rules_id", param.getCoding()).one();
        if (ToolUtil.isNotEmpty(rules)) {
            String backCoding = rulesService.backCoding(Long.valueOf(param.getCoding()));
            String replace = "";
            if (param.getType().equals("入厂")) {
                replace = backCoding.replace("${type}", "in");
            }
            if (param.getType().equals("出厂")) {
                replace = backCoding.replace("${type}", "out");
            }
            param.setCoding(replace);
        }

        QualityTask entity = getEntity(param);
        this.save(entity);

        if (ToolUtil.isNotEmpty(param.getDetails())) {
            List<Long> skuIds = new ArrayList<>();
            Map<Long, QualityTaskDetailParam> maps = new HashMap<>();
            List<QualityTaskDetail> details = new ArrayList<>();
            for (QualityTaskDetailParam detailParam : param.getDetails()) {
                skuIds.add(detailParam.getSkuId());

                maps.put(detailParam.getSkuId(), detailParam);

                QualityTaskDetail detail = new QualityTaskDetail();
                detailParam.setQualityTaskId(entity.getQualityTaskId());
                ToolUtil.copyProperties(detailParam, detail);
                detail.setRemaining(detailParam.getNumber());
                details.add(detail);
            }
            detailService.saveBatch(details);
            //回填sku质检项
            List<Sku> skus = skuService.query().in("sku_id", skuIds).list();
            for (Sku sku : skus) {
                QualityTaskDetailParam qualityTaskDetailParam = maps.get(sku.getSkuId());
                sku.setQualityPlanId(qualityTaskDetailParam.getQualityPlanId());
                sku.setBatch(qualityTaskDetailParam.getBatch());
            }
            skuService.updateBatchById(skus);
        }

        BackCodeRequest backCodeRequest = new BackCodeRequest();
        backCodeRequest.setId(entity.getQualityTaskId());
        backCodeRequest.setSource("quality");
        Long aLong = orCodeService.backCode(backCodeRequest);
        String url = param.getUrl().replace("codeId", aLong.toString());

        String type2Activiti = null;
        if (param.getType().equals("出厂")) {
            type2Activiti = "outQuality";
        } else if (param.getType().equals("入厂")) {
            type2Activiti = "inQuality";
        }
        ActivitiProcess activitiProcess = activitiProcessService.query().eq("type", "audit").eq("status", 99).eq("module", type2Activiti).one();
        if (ToolUtil.isNotEmpty(activitiProcess)) {
            this.power(activitiProcess);//检查创建权限
            LoginUser user = LoginContextHolder.getContext().getUser();
            ActivitiProcessTaskParam activitiProcessTaskParam = new ActivitiProcessTaskParam();
            activitiProcessTaskParam.setTaskName(user.getName() + "发起的质检任务 (" + param.getCoding() + ")");
            activitiProcessTaskParam.setQTaskId(entity.getQualityTaskId());
            activitiProcessTaskParam.setUserId(param.getUserId());
            activitiProcessTaskParam.setFormId(entity.getQualityTaskId());
            activitiProcessTaskParam.setProcessId(activitiProcess.getProcessId());
            ActivitiProcessTask activitiProcessTask = new ActivitiProcessTask();
            ToolUtil.copyProperties(activitiProcessTaskParam, activitiProcessTask);
            Long taskId = activitiProcessTaskService.add(activitiProcessTaskParam);
            //添加log
            activitiProcessLogService.addLog(activitiProcess.getProcessId(), taskId);
            activitiProcessLogService.audit(taskId, 1, false);
        } else if (ToolUtil.isEmpty(activitiProcess) || ToolUtil.isEmpty(activitiProcess)) {
            WxCpTemplate wxCpTemplate = new WxCpTemplate();
            List<Long> userIds = new ArrayList<>();
            userIds.add(param.getUserId());
            wxCpTemplate.setUserIds(userIds);
            wxCpTemplate.setUrl(url);
            wxCpTemplate.setTitle("质检任务提醒");
            wxCpTemplate.setDescription("有新的质检任务");
            wxCpSendTemplate.setWxCpTemplate(wxCpTemplate);
            wxCpSendTemplate.sendTemplate();
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
    public void delete(QualityTaskParam param) {
        param.setDisplay(0);
        QualityTask qualityTask = new QualityTask();
        ToolUtil.copyProperties(param, qualityTask);
        this.updateById(qualityTask);
    }

    @Override
    public void update(QualityTaskParam param) {
        QualityTask oldEntity = getOldEntity(param);
        QualityTask newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Transactional
    @Override
    public void checkOver(QualityTaskParam param) {
        QualityTask oldEntity = getOldEntity(param);
        QualityTask newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);

        this.updateById(newEntity);

        if (ToolUtil.isNotEmpty(param.getState())) {
            switch (param.getState()) {
                case 1:
                    // 主任务完成状态
                    ActivitiProcessTask activitiProcessTask = activitiProcessTaskService.query().eq("form_id", oldEntity.getQualityTaskId()).one();
                    if (ToolUtil.isNotEmpty(activitiProcessTask)) {
                        activitiProcessLogService.audit(activitiProcessTask.getProcessTaskId(), 1, false);
                        newEntity.setState(1);
                    } else {
                        newEntity.setState(2);
                    }
                    break;
                case 2:
                    // 质检审批完成状态
                    break;
            }
        }


    }

    @Override
    public QualityTaskResult findBySpec(QualityTaskParam param) {
        return null;
    }

    @Override
    public List<QualityTaskResult> findListBySpec(QualityTaskParam param) {
        return null;
    }

    @Override
    public PageInfo<QualityTaskResult> findPageBySpec(QualityTaskParam param) {


        Page<QualityTaskResult> pageContext = getPageContext();
        IPage<QualityTaskResult> page = this.baseMapper.customPageList(pageContext, param);
        this.format(page.getRecords());
        return PageFactory.createPageInfo(page);
    }

    private void format(List<QualityTaskResult> param) {
        List<Long> userIds = new ArrayList<>();
        for (QualityTaskResult qualityTaskResult : param) {
            userIds.add(qualityTaskResult.getUserId());
        }
        List<User> users = userIds.size() == 0 ? new ArrayList<>() : userService.lambdaQuery().in(User::getUserId, userIds).and(i -> i.eq(User::getStatus, "ENABLE")).list();
        for (QualityTaskResult qualityTaskResult : param) {
            for (User user : users) {
                if (ToolUtil.isNotEmpty(qualityTaskResult.getUserId())) {
                    if (qualityTaskResult.getUserId().equals(user.getUserId())) {
                        qualityTaskResult.setUserName(user.getName());
                    }
                }
            }
        }

    }


    @Override
    public void addFormData(FormDataPojo formDataPojo) {
        //通过二维码查询实物id
        OrCodeBind codeId = bindService.query().eq("qr_code_id", formDataPojo.getQrCodeId()).one();
        if (ToolUtil.isEmpty(codeId)) {
            throw new ServiceException(500, "二维码不正确");
        }
        //添加data
        FormData data = new FormData();
        data.setModule(formDataPojo.getModule());
        data.setFormId(codeId.getFormId());
        formDataService.save(data);

        //通过质检项详情添加dataValue
        List<QualityPlanDetail> details = qualityPlanDetailService.query().eq("plan_id", formDataPojo.getPlanId()).list();
        if (ToolUtil.isNotEmpty(details)) {

            List<FormDataValue> dataValues = new ArrayList<>();
            for (QualityPlanDetail detail : details) {
                FormDataValue dataValue = new FormDataValue();
                dataValue.setDataId(data.getDataId());
                dataValue.setField(detail.getPlanDetailId());
                dataValues.add(dataValue);
            }
            formDataValueService.saveBatch(dataValues);
        }


    }

    /**
     * 判断质检是否必填
     *
     * @param details
     * @param formValue
     * @return
     */
    public Boolean backBoolean(List<QualityPlanDetail> details, FormValues formValue) {
        for (QualityPlanDetail detail : details) {
            if (formValue.getField().equals(detail.getPlanDetailId())) {
                if (ToolUtil.isEmpty(formValue.getDataValues())) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 判断多个实物是否添加完成
     *
     * @param data
     * @param details
     * @return
     */
    public Boolean judge(List<FormDataValue> data, List<QualityPlanDetail> details) {

        for (QualityPlanDetail detail : details) {
            for (FormDataValue value : data) {
                if (detail.getPlanDetailId().equals(value.getField())) {
                    if (ToolUtil.isEmpty(value.getValue())) {
                        return false;
                    }
                }
            }
        }
        return true;
    }


    @Override
    public List<TaskCount> backIkind(Long id) {
        //通过二维码找出ikinds
        List<QualityTaskBind> taskBindList = taskBindService.query().eq("quality_task_id", id).list();
        List<Long> iKinds = new ArrayList<>();
        for (QualityTaskBind taskBind : taskBindList) {
            iKinds.add(taskBind.getInkindId());

        }
        if (ToolUtil.isNotEmpty(iKinds)) {
            //通过inkind获取sku
            List<Inkind> inkinds = iKinds.size() == 0 ? new ArrayList<>() : inkindService.query().in("inkind_id", iKinds).list();
            Map<Long, Integer> skuMap = new HashMap<>();
            List<TaskCount> taskCounts = new ArrayList<>();
            Integer i;
            for (Inkind inkind : inkinds) {
                Sku sku = skuService.query().eq("sku_id", inkind.getSkuId()).one();
                i = skuMap.get(sku.getSkuId());
                if (ToolUtil.isEmpty(i)) {
                    i = 0;
                    skuMap.put(sku.getSkuId(), 0);
                }
                Integer count = formDataService.query().in("form_id", inkind.getInkindId()).count();

                skuMap.put(sku.getSkuId(), i + count);

            }
            for (Map.Entry<Long, Integer> longIntegerEntry : skuMap.entrySet()) {
                TaskCount taskCount = new TaskCount();
                taskCount.setSkuId(longIntegerEntry.getKey());
                taskCount.setCount(longIntegerEntry.getValue());
                taskCounts.add(taskCount);
            }
            return taskCounts;
        }
        return null;
    }

    /**
     * 添加子任务
     *
     * @param
     */
    @Override
    public void addChild(QualityTaskChild child) {

        QualityTaskParam params = child.getTaskParams();
        params.setParentId(params.getQualityTaskId());
        params.setQualityTaskId(null);

        QualityTask qualityTask = new QualityTask();
        ToolUtil.copyProperties(params, qualityTask);

        this.save(qualityTask);

        List<QualityTaskDetail> details = new ArrayList<>();

        List<QualityTaskDetail> ChildDetails = new ArrayList<>();

        for (QualityTaskDetailParam detail : params.getDetails()) {
            QualityTaskDetail qualityTaskDetail = new QualityTaskDetail();
            ToolUtil.copyProperties(detail, qualityTaskDetail);
            qualityTaskDetail.setRemaining(qualityTaskDetail.getRemaining() - detail.getNewNumber());
            details.add(qualityTaskDetail);

            //添加子任务
            QualityTaskDetail childDetail = new QualityTaskDetail();
            childDetail.setSkuId(detail.getSkuId());
            childDetail.setBrandId(detail.getBrandId());
            childDetail.setQualityTaskId(qualityTask.getQualityTaskId());
            childDetail.setBatch(detail.getBatch());
            childDetail.setQualityPlanId(detail.getQualityPlanId());
            childDetail.setNumber(detail.getNewNumber());
            ChildDetails.add(childDetail);

        }
        detailService.updateBatchById(details);

        detailService.saveBatch(ChildDetails);

    }

    @Override
    public QualityTaskResult backChildTask(Long id) {
        //子任务
        QualityTask qualityTask = this.query().eq("quality_task_id", id).one();
        if (ToolUtil.isEmpty(qualityTask)) {
            throw new ServiceException(500, "参数不正确");
        }

        QualityTaskResult taskResult = new QualityTaskResult();
        ToolUtil.copyProperties(qualityTask, taskResult);


        QualityTask fatherTask = this.query().eq("quality_task_id", qualityTask.getParentId()).one();


        List<QualityTaskDetail> taskDetails = detailService.query().eq("quality_task_id", qualityTask.getQualityTaskId()).list();

        List<Long> brandIds = new ArrayList<>();
        List<Long> planIds = new ArrayList<>();
        for (QualityTaskDetail taskDetail : taskDetails) {
            brandIds.add(taskDetail.getBrandId());
            planIds.add(taskDetail.getQualityPlanId());
        }

        List<Brand> brands = brandService.query().in("brand_id", brandIds).list();
        List<QualityPlan> qualityPlans = planService.query().in("quality_plan_id", planIds).list();

        List<QualityTaskDetailResult> detailResults = new ArrayList<>();


        for (QualityTaskDetail taskDetail : taskDetails) {
            for (Brand brand : brands) {
                if (taskDetail.getBrandId().equals(brand.getBrandId())) {

                    BrandResult brandResult = new BrandResult();
                    ToolUtil.copyProperties(brand, brandResult);

                    QualityTaskDetailResult detailResult = new QualityTaskDetailResult();
                    ToolUtil.copyProperties(taskDetail, detailResult);
                    SkuResult skuResult = skuService.getSku(taskDetail.getSkuId());
                    detailResult.setSkuResult(skuResult);
                    detailResult.setBrand(brandResult);
                    detailResults.add(detailResult);

                }
            }

        }

        for (QualityTaskDetailResult detailResult : detailResults) {
            for (QualityPlan qualityPlan : qualityPlans) {
                if (detailResult.getQualityPlanId().equals(qualityPlan.getQualityPlanId())) {
                    QualityPlanResult qualityPlanResult = new QualityPlanResult();
                    ToolUtil.copyProperties(qualityPlan, qualityPlanResult);
                    detailResult.setQualityPlanResult(qualityPlanResult);
                }
            }

        }
        String[] split = taskResult.getUserIds().split(",");

        List<User> users = userService.query().in("user_id", split).list();
        List<String> userName = new ArrayList<>();

        for (User user : users) {
            userName.add(user.getName());
        }

        taskResult.setUsers(users);

        taskResult.setNames(userName);

        taskResult.setDetails(detailResults);

        taskResult.setFatherTask(fatherTask);

        return taskResult;
    }

    @Override
    public List<FormDataValueResult> valueResults(Long qrcodeId) {
        //通过二维码找到实物id
        OrCodeBind codeId = bindService.query().eq("qr_code_id", qrcodeId).one();
        if (ToolUtil.isEmpty(codeId)) {
            throw new ServiceException(500, "二维码不正确");
        }
        //通过实物id找到data
        FormData formData = formDataService.query().eq("form_id", codeId.getFormId()).one();
        //通过data找value
        List<FormDataValue> dataValues = formDataValueService.query().eq("data_id", formData.getDataId()).list();


        List<Long> planId = new ArrayList<>();
        for (FormDataValue dataValue : dataValues) {
            planId.add(dataValue.getField());
        }

        List<QualityPlanDetail> planDetails = qualityPlanDetailService.query().in("plan_detail_id", planId).list();

        List<Long> checkIds = new ArrayList<>();
        List<QualityPlanDetailResult> detailResults = new ArrayList<>();
        //查plan详情
        for (QualityPlanDetail planDetail : planDetails) {
            checkIds.add(planDetail.getQualityCheckId());
            QualityPlanDetailResult detailResult = new QualityPlanDetailResult();
            ToolUtil.copyProperties(planDetail, detailResult);
            detailResults.add(detailResult);
        }

        //查找项
        List<QualityCheck> checks = checkService.query().in("quality_check_id", checkIds).list();

        for (QualityPlanDetailResult detailResult : detailResults) {
            for (QualityCheck check : checks) {
                if (detailResult.getQualityCheckId().equals(check.getQualityCheckId())) {
                    QualityCheckResult checkResult = new QualityCheckResult();
                    ToolUtil.copyProperties(check, checkResult);
                    detailResult.setQualityCheckResult(checkResult);
                    break;
                }
            }
        }


        List<FormDataValueResult> formDataValueResults = new ArrayList<>();

        for (FormDataValue dataValue : dataValues) {
            for (QualityPlanDetailResult detailResult : detailResults) {
                if (dataValue.getField().equals(detailResult.getPlanDetailId())) {
                    FormDataValueResult dataValueResult = new FormDataValueResult();
                    ToolUtil.copyProperties(dataValue, dataValueResult);
                    dataValueResult.setQualityPlanDetailResult(detailResult);
                    formDataValueResults.add(dataValueResult);
                }
            }
        }

        return formDataValueResults;
    }

    @Override
    public void updateDataValue(Long id, String value) {
        FormDataValue dataValue = new FormDataValue();
        dataValue.setValue(value);
        formDataValueService.update(dataValue, new QueryWrapper<FormDataValue>() {{
            eq("value_id", id);
        }});
    }

    @Override
    public void taskComplete(TaskComplete taskComplete) {
        List<FormDataValue> valueList = formDataValueService.query().in("value_id", taskComplete.getValueIds()).list();

        List<Long> planIds = new ArrayList<>();
        for (FormDataValue formDataValue : valueList) {
            planIds.add(formDataValue.getField());
        }
        List<QualityPlanDetail> details = qualityPlanDetailService.query().in("plan_detail_id", planIds).list();

        Boolean t = judge(valueList, details);

        if (!t) {
            throw new ServiceException(500, "请检查必填项");
        }

    }


    @Override
    public void formDataFormat(FormDataResult param) {

    }

    @Override
    public void formDataFormat1(List<FormDataResult> param) {
        List<Long> formIds = new ArrayList<>();
        List<Long> dataIds = new ArrayList<>();
        for (FormDataResult formDataResult : param) {
            formIds.add(formDataResult.getFormId());
            dataIds.add(formDataResult.getDataId());
        }
        List<Inkind> inkinds = inkindService.lambdaQuery().in(Inkind::getInkindId, formIds).and(i -> i.eq(Inkind::getDisplay, 1)).list();
        List<Long> skuIds = new ArrayList<>();
        List<Long> brandIds = new ArrayList<>();
        for (Inkind inkind : inkinds) {
            skuIds.add(inkind.getSkuId());
            brandIds.add(inkind.getBrandId());
        }
        List<Brand> brandList = brandIds.size() == 0 ? new ArrayList<>() : brandService.lambdaQuery().in(Brand::getBrandId, brandIds).and(i -> i.eq(Brand::getDisplay, 1)).list();

        List<Sku> skus = skuIds.size() == 0 ? new ArrayList<>() : skuService.lambdaQuery().in(Sku::getSkuId, skuIds).and(i -> i.eq(Sku::getDisplay, 1)).list();
        List<SkuResult> skuResults = new ArrayList<>();
        for (Sku sku : skus) {
            SkuResult skuResult = new SkuResult();
            ToolUtil.copyProperties(sku, skuResult);
            skuResults.add(skuResult);
        }
        skuService.format(skuResults);
        List<FormDataValue> formDataValues = formDataValueService.lambdaQuery().in(FormDataValue::getDataId, dataIds).and(i -> i.eq(FormDataValue::getDisplay, 1)).list();
        List<Long> planIds = new ArrayList<>();

        for (FormDataValue formDataValue : formDataValues) {
            planIds.add(formDataValue.getField());
        }
        List<QualityPlanDetail> planDetails = planIds.size() == 0 ? new ArrayList<>() : qualityPlanDetailService.lambdaQuery().in(QualityPlanDetail::getPlanDetailId, planIds).and(i -> i.eq(QualityPlanDetail::getDisplay, 1)).list();
        List<Long> checkIds = new ArrayList<>();
        for (QualityPlanDetail planDetail : planDetails) {
            checkIds.add(planDetail.getQualityCheckId());
        }
        List<QualityCheck> qualityChecklist = checkIds.size() == 0 ? new ArrayList<>() : qualityCheckService.lambdaQuery().in(QualityCheck::getQualityCheckId, checkIds).eq(QualityCheck::getDisplay, 1).list();
        List<QualityCheckResult> qualityCheckResults = new ArrayList<>();
        for (QualityCheck qualityCheck : qualityChecklist) {
            QualityCheckResult qualityCheckResult = new QualityCheckResult();
            ToolUtil.copyProperties(qualityCheck, qualityCheckResult);
            qualityCheckResults.add(qualityCheckResult);
        }

        for (FormDataResult formDataResult : param) {

            for (Inkind inkind : inkinds) {
                if (formDataResult.getFormId().equals(inkind.getInkindId())) {
                    formDataResult.setInkind(inkind);
                    for (SkuResult skuResult : skuResults) {
                        if (skuResult.getSkuId().equals(inkind.getSkuId())) {
                            formDataResult.setSku(skuResult);

                        }
                    }
                    for (Brand brand : brandList) {
                        if (inkind.getBrandId().equals(brand.getBrandId())) {
                            formDataResult.setBrand(brand);
                        }
                    }
                }
            }
            List<Map<String, Object>> maps = new ArrayList<>();
            for (FormDataValue formDataValue : formDataValues) {

                if (formDataResult.getDataId().equals(formDataValue.getDataId())) {
                    for (QualityPlanDetail planDetail : planDetails) {
                        if (formDataValue.getField().equals(planDetail.getPlanDetailId())) {
                            for (QualityCheckResult qualityCheck : qualityCheckResults) {
                                if (qualityCheck.getQualityCheckId().equals(planDetail.getQualityCheckId())) {
                                    Map<String, Object> map = new HashMap<>();
                                    map.put("name", qualityCheck.getName());

                                    if (ToolUtil.isNotEmpty(formDataValue.getValue())) {
                                        FormValues.DataValues dataValues = JSONUtil.toBean(formDataValue.getValue(), FormValues.DataValues.class);
                                        map.put("value", dataValues);
                                        Boolean flag = false;

                                        if (qualityCheck.getType() == 1 || qualityCheck.getType() == 4) {
                                            flag = false;
                                            if (planDetail.getIsNull() == 0 || ToolUtil.isNotEmpty(dataValues.getValue())) {
                                                if (ToolUtil.isNotEmpty(dataValues.getValue())) {
                                                    if (ToolUtil.isNotEmpty(planDetail.getOperator()) || ToolUtil.isNotEmpty(planDetail.getStandardValue())) {
                                                        flag = this.mathData(planDetail.getStandardValue(), planDetail.getOperator(), dataValues.getValue());
                                                    }
                                                } else {
                                                    flag = true;
                                                }
                                            }
                                        } else if (qualityCheck.getType() == 3) {
                                            if (planDetail.getIsNull() == 0 || ToolUtil.isNotEmpty(dataValues.getValue())) {
                                                if (ToolUtil.isNotEmpty(dataValues.getValue())) {
                                                    if (dataValues.getValue().equals("1")) {
                                                        flag = true;
                                                    } else {
                                                        flag = false;
                                                    }
                                                } else {
                                                    flag = true;
                                                }
                                            }
                                        } else {
                                            flag = true;
                                        }
                                        map.put("standar", flag);
                                        map.put("field", planDetail);
                                        map.put("type", qualityCheck.getType());
                                        maps.add(map);

                                    }
                                }

                            }
                        }
                    }
                }
            }
            formDataResult.setValueResults(maps);

        }
    }

    private Boolean mathData(String standardValue, Long operator, String value) {
        Boolean flag = false;
        if (ToolUtil.isNotEmpty(standardValue)) {
            if (ToolUtil.isNotEmpty(operator)) {
                switch (operator.toString()) {
                    case "1":
                        if (Double.valueOf(value).equals(Double.valueOf(standardValue)))
                            flag = true;
                        break;
                    case "2":
                        if (Double.parseDouble(value) >= Double.parseDouble(standardValue))
                            flag = true;
                        break;
                    case "3":
                        if (Double.parseDouble(value) <= Double.parseDouble(standardValue))
                            flag = true;
                        break;
                    case "4":
                        if (Double.parseDouble(value) > Double.parseDouble(standardValue))
                            flag = true;
                        break;
                    case "5":
                        if (Double.parseDouble(value) < Double.parseDouble(standardValue))
                            flag = true;
                        break;
                    case "6":
                        List<String> result = Arrays.asList(standardValue.split(","));
                        if (Double.parseDouble(value) >= Double.parseDouble(result.get(0)) && Double.parseDouble(value) <= Double.parseDouble(result.get(1)))
                            flag = true;
                        break;
                }
            }

        }
        return flag;
    }

    @Override
    public void detailFormat(QualityTaskResult result) {

        LoginUser loginUser = LoginContextHolder.getContext().getUser();

        List<QualityTask> tasks = this.query().eq("parent_id", result.getQualityTaskId()).list();


//        List<QualityTaskDetail> qualityTaskDetails = detailService.lambdaQuery().in(QualityTaskDetail::getQualityTaskId, param.getQualityTaskId()).and(i -> i.eq(QualityTaskDetail::getDisplay, 1)).list();
//        List<QualityTaskDetailResult> qualityTaskDetailResults = new ArrayList<>();
//        for (QualityTaskDetail qualityTaskDetail : qualityTaskDetails) {
//            QualityTaskDetailResult qualityTaskDetailResult = new QualityTaskDetailResult();
//            ToolUtil.copyProperties(qualityTaskDetail, qualityTaskDetailResult);
//            if (ToolUtil.isNotEmpty(qualityTaskDetailResult.getUserIds())) {
//                String[] strings = qualityTaskDetailResult.getUserIds().split(",");
//                for (String id : strings) {
//                    if (loginUser.getId().toString().equals(id)) {
//                        qualityTaskDetailResults.add(qualityTaskDetailResult);
//                    }
//                }
//            }
//        }

//        detailService.format(qualityTaskDetailResults);
//        param.setDetails(qualityTaskDetailResults);
//        User byId = userService.getById(param.getUserId());
//        if (ToolUtil.isNotEmpty(byId)) {
//            param.setUserName(byId.getName());
//        }

    }

    private Serializable getKey(QualityTaskParam param) {
        return param.getQualityTaskId();
    }

    private Page<QualityTaskResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private QualityTask getOldEntity(QualityTaskParam param) {
        return this.getById(getKey(param));
    }

    private QualityTask getEntity(QualityTaskParam param) {
        QualityTask entity = new QualityTask();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }


}
