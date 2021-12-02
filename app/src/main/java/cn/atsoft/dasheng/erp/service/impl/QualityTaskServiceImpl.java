package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.app.entity.Brand;
import cn.atsoft.dasheng.app.service.BrandService;
import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.auth.model.LoginUser;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.entity.*;
import cn.atsoft.dasheng.erp.mapper.QualityTaskMapper;
import cn.atsoft.dasheng.erp.model.params.QualityTaskDetailParam;
import cn.atsoft.dasheng.erp.model.params.QualityTaskParam;
import cn.atsoft.dasheng.erp.model.request.FormDataPojo;
import cn.atsoft.dasheng.erp.model.request.FormValues;
import cn.atsoft.dasheng.erp.model.result.*;
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
    SkuService skuService;
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
    private ActivitiStepsService activitiStepsService;

    @Autowired
    private ActivitiProcessLogService activitiProcessLogService;

    @Autowired
    private ActivitiProcessService activitiProcessService;

    @Autowired
    private ActivitiStepsService stepsService;

    @Autowired
    private ActivitiAuditService auditService;

    @Autowired
    private QualityTaskDetailService taskDetailService;

    @Override
    @Transactional
    public void add(QualityTaskParam param) {

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

            this.checkUser(activitiProcess.getProcessId()); //判断是否有权限操作

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
            activitiProcessLogService.add(taskId, 1);
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

    private void checkUser(Long processId) {
        ActivitiSteps one1 = stepsService.query().eq("process_id", processId).eq("type", 0).one();
        ActivitiAudit one = auditService.query().eq("setps_id", one1.getSetpsId()).one();
        activitiProcessLogService.checkUser(one.getRule());
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


        if (ToolUtil.isNotEmpty(param.getState())) {
            switch (param.getState()) {
                case 1:
                    // 主任务完成状态
                    ActivitiProcessTask activitiProcessTask = activitiProcessTaskService.query().eq("form_id", oldEntity.getQualityTaskId()).one();
                    if (ToolUtil.isNotEmpty(activitiProcessTask)) {
                        activitiProcessLogService.add(activitiProcessTask.getProcessTaskId(), 1);
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


        ToolUtil.copyProperties(newEntity, oldEntity);


        this.updateById(newEntity);
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
    @Transactional
    public void addFormData(FormDataPojo formDataPojo) {
        //通过二维码查询实物id
        OrCodeBind codeId = bindService.query().eq("qr_code_id", formDataPojo.getFormId()).one();
        FormData data = formDataService.query().eq("form_id", codeId.getFormId()).one();

        boolean t = true;
        if (ToolUtil.isNotEmpty(data)) {
            formDataValueService.remove(new QueryWrapper<FormDataValue>() {{
                eq("data_id", data.getDataId());
            }});
            formDataService.removeById(data);
        }

        FormData formData = new FormData();
        formData.setModule(formDataPojo.getModule());
        formData.setFormId(codeId.getFormId());
        formData.setMainId(0L);
        formDataService.save(formData);
        //添加绑定关系
        QualityTaskBind taskBind = new QualityTaskBind();
        taskBind.setQualityTaskId(formDataPojo.getTaskId());
        taskBind.setInkindId(codeId.getFormId());
        taskBindService.save(taskBind);

        List<FormValues> formValues = formDataPojo.getFormValues();
        List<FormDataValue> formValuesList = new ArrayList<>();

        //查询任务质检详情 判断绑定的
        QualityTaskDetail TaskDetail = taskDetailService.getOne(new QueryWrapper<QualityTaskDetail>() {{
            eq("quality_task_detail_id", formDataPojo.getQualityTaskDetailId());
        }});
        if (ToolUtil.isEmpty(TaskDetail)) {
            throw new ServiceException(500, "请确定质检任务详情");
        }
        if (ToolUtil.isEmpty(TaskDetail.getInkindId())) {
            t = false;
        }
        //判断绑定数量
        List<String> inKinds = new ArrayList<>();
        Integer number = TaskDetail.getNumber();
        if (TaskDetail.getBatch() == 0) {
            String[] strings = TaskDetail.getInkindId().split(",");
            int length = strings.length;
            if (number != length) {  //判断绑定的实物 是否和方案详情数量相等  如果不等 就是没填全
                t = false;
            } else {
                inKinds.addAll(Arrays.asList(strings));
            }
        }
        //查询方案必填项
        List<QualityPlanDetail> details = qualityPlanDetailService.list(new QueryWrapper<QualityPlanDetail>() {{
            eq("is_null", 1);
        }});

        for (FormValues formValue : formValues) {
            Boolean aBoolean = backBoolean(details, formValue);
            if (!aBoolean) {
                t = false;
            }
            FormDataValue formDataValue = new FormDataValue();
            formDataValue.setValue(JSONUtil.toJsonStr(formValue.getDataValues()));
            formDataValue.setDataId(formData.getDataId());
            formDataValue.setField(formValue.getField());
            formValuesList.add(formDataValue);
        }
        //为真  判断是否必填
        if (t) {
            List<OrCodeBind> qrCodeId = inKinds.size() == 0 ? new ArrayList<>() : bindService.list(new QueryWrapper<OrCodeBind>() {{
                in("qr_code_id", inKinds);
            }});
            List<Long> formIds = new ArrayList<>();
            if (ToolUtil.isNotEmpty(qrCodeId)) {
                for (OrCodeBind orCodeBind : qrCodeId) {
                    formIds.add(orCodeBind.getFormId());
                }
            }
            List<FormData> dataList = formIds.size() == 0 ? new ArrayList<>() : formDataService.list(new QueryWrapper<FormData>() {{
                in("form_id", formIds);
            }});

            Boolean judge = judge(dataList, details);
            if (!judge) {
                t = false;
            }
        }
        formDataValueService.saveBatch(formValuesList);
        if (t) {
            TaskDetail.setStatus("完成");
            taskDetailService.updateById(TaskDetail);
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
    public Boolean judge(List<FormData> data, List<QualityPlanDetail> details) {
        List<Long> dataIds = new ArrayList<>();
        for (FormData datum : data) {
            dataIds.add(datum.getDataId());
        }
        List<FormDataValue> values = formDataValueService.list(new QueryWrapper<FormDataValue>() {{
            in("data_id", dataIds);
        }});

        for (QualityPlanDetail detail : details) {
            for (FormDataValue value : values) {
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

                                        if (qualityCheck.getType() == 1 || qualityCheck.getType() == 5) {
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
    public void detailFormat(QualityTaskResult param) {

        LoginUser loginUser = LoginContextHolder.getContext().getUser();

        List<QualityTaskDetail> qualityTaskDetails = detailService.lambdaQuery().in(QualityTaskDetail::getQualityTaskId, param.getQualityTaskId()).and(i -> i.eq(QualityTaskDetail::getDisplay, 1)).list();
        List<QualityTaskDetailResult> qualityTaskDetailResults = new ArrayList<>();
        for (QualityTaskDetail qualityTaskDetail : qualityTaskDetails) {
            QualityTaskDetailResult qualityTaskDetailResult = new QualityTaskDetailResult();
            ToolUtil.copyProperties(qualityTaskDetail, qualityTaskDetailResult);
            if (ToolUtil.isNotEmpty(qualityTaskDetailResult.getUserIds())) {
                String[] strings = qualityTaskDetailResult.getUserIds().split(",");
                for (String id : strings) {
                    if (loginUser.getId().toString().equals(id)) {
                        qualityTaskDetailResults.add(qualityTaskDetailResult);
                    }
                }
            }
        }

        detailService.format(qualityTaskDetailResults);
        param.setDetails(qualityTaskDetailResults);
        User byId = userService.getById(param.getUserId());
        if (ToolUtil.isNotEmpty(byId)) {
            param.setUserName(byId.getName());
        }

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
