package cn.atsoft.dasheng.erp.service.impl;

import cn.atsoft.dasheng.app.entity.Brand;
import cn.atsoft.dasheng.app.model.result.BrandResult;
import cn.atsoft.dasheng.app.service.BrandService;
import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.auth.model.LoginUser;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.config.MobileService;
import cn.atsoft.dasheng.erp.entity.*;
import cn.atsoft.dasheng.erp.mapper.QualityTaskMapper;
import cn.atsoft.dasheng.erp.model.params.QualityTaskDetailParam;
import cn.atsoft.dasheng.erp.model.params.QualityTaskParam;
import cn.atsoft.dasheng.erp.model.request.FormDataPojo;
import cn.atsoft.dasheng.erp.model.request.FormValues;
import cn.atsoft.dasheng.erp.model.result.*;
import cn.atsoft.dasheng.erp.pojo.FormDataRequest;
import cn.atsoft.dasheng.erp.pojo.FormDataValueResult;
import cn.atsoft.dasheng.erp.pojo.QualityTaskChild;
import cn.atsoft.dasheng.erp.pojo.TaskComplete;
import cn.atsoft.dasheng.erp.service.*;
import cn.atsoft.dasheng.form.entity.*;
import cn.atsoft.dasheng.form.model.params.ActivitiProcessTaskParam;
import cn.atsoft.dasheng.form.model.result.ActivitiStepsResult;
import cn.atsoft.dasheng.form.model.result.FormDataResult;
import cn.atsoft.dasheng.form.pojo.RuleType;
import cn.atsoft.dasheng.form.service.*;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.orCode.BindParam;
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

import static cn.atsoft.dasheng.form.pojo.RuleType.*;
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
    private QualityTaskDetailService detailService;
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
    private MobileService mobileService;
    @Autowired
    private QualityPlanService planService;
    @Autowired
    private QualityCheckService checkService;
    @Autowired
    private ActivitiProcessTaskService processTaskService;


    @Override
    @Transactional
    public void add(QualityTaskParam param) {
        if (ToolUtil.isEmpty(param.getParentId())) {  //主任务添加
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
        } else {   //子任务添加
            QualityTask task = this.getById(param.getParentId());
            if (ToolUtil.isEmpty(task)) {
                throw new ServiceException(500, "任务不存在");
            }

            //主任务应分配的数量
            Long detailNumber = detailService.getDetails(param.getParentId());
            if (detailNumber == 0) {
                throw new ServiceException(500, "已分派完成!");
            }
            param.setState(1);


        }

        QualityTask entity = getEntity(param);
        this.save(entity);
        //主任务
        if (ToolUtil.isEmpty(param.getParentId())) {
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
                activitiProcessTaskParam.setType("quality_task");
                activitiProcessTaskParam.setProcessId(activitiProcess.getProcessId());
                ActivitiProcessTask activitiProcessTask = new ActivitiProcessTask();
                ToolUtil.copyProperties(activitiProcessTaskParam, activitiProcessTask);
                Long taskId = activitiProcessTaskService.add(activitiProcessTaskParam);
                //添加log
                activitiProcessLogService.addLog(activitiProcess.getProcessId(), taskId);
                activitiProcessLogService.autoAudit(taskId);
            } else {
                throw new ServiceException(500, "请创建质检流程！");
            }
        }

        if (ToolUtil.isNotEmpty(param.getDetails())) {
            if (ToolUtil.isEmpty(param.getParentId())) {    //主任务
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
            } else {
                List<QualityTaskDetail> details = new ArrayList<>();
                List<QualityTaskDetail> ChildDetails = new ArrayList<>();
                for (QualityTaskDetailParam detail : param.getDetails()) {
                    QualityTaskDetail qualityTaskDetail = new QualityTaskDetail();
                    ToolUtil.copyProperties(detail, qualityTaskDetail);

                    if (detail.getNumber() - detail.getRemaining() < 0) {
                        throw new ServiceException(500, "请确定数量");
                    }
                    qualityTaskDetail.setRemaining(qualityTaskDetail.getRemaining() - detail.getNewNumber());
                    details.add(qualityTaskDetail);

                    //添加子任务
                    QualityTaskDetail childDetail = new QualityTaskDetail();
                    childDetail.setSkuId(detail.getSkuId());
                    childDetail.setBrandId(detail.getBrandId());
                    childDetail.setQualityTaskId(entity.getQualityTaskId());
                    childDetail.setBatch(detail.getBatch());
                    childDetail.setQualityPlanId(detail.getQualityPlanId());
                    childDetail.setNumber(detail.getNewNumber());
                    childDetail.setPercentum(detail.getPercentum());
                    ChildDetails.add(childDetail);
                }

                detailService.updateBatchById(details);
                detailService.saveBatch(ChildDetails);
                //主任务详情
                List<QualityTaskDetail> fatherTaskDetail = detailService.query().eq("quality_task_id", param.getParentId()).list();
                //判断任务是否分配完成
                boolean fatherDetail = true;
                for (QualityTaskDetail qualityTaskDetail : fatherTaskDetail) {
                    if (qualityTaskDetail.getRemaining() > 0) {
                        fatherDetail = false;
                        break;
                    }
                }
                //分派数量完成  更新主任务状态
                if (fatherDetail) {
                    QualityTask task = new QualityTask();
                    task.setState(1);
                    this.update(task, new QueryWrapper<QualityTask>() {{
                        eq("quality_task_id", param.getParentId());
                    }});

                    ActivitiProcessTask processTask = activitiProcessTaskService.getByFormId(param.getParentId());
                    activitiProcessLogService.autoAudit(processTask.getProcessTaskId());
                }

                WxCpTemplate wxCpTemplate = new WxCpTemplate();
                String userIds = param.getUserIds();
                List<Long> users = Arrays.asList(userIds.split(",")).stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
                wxCpTemplate.setUserIds(users);
                String url = mobileService.getMobileConfig().getUrl();
                url = url + "/#/Work/Quality?id=" + param.getParentId();
                wxCpTemplate.setUrl(url);
                wxCpTemplate.setDescription("点击查看新质检任务");
                wxCpTemplate.setTitle("您被分派新的任务");
                wxCpTemplate.setType(1);
                wxCpSendTemplate.setWxCpTemplate(wxCpTemplate);
                wxCpSendTemplate.sendTemplate();
            }
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
                        activitiProcessLogService.audit(activitiProcessTask.getProcessTaskId(), 1);
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
        Long formId = bindService.getFormId(formDataPojo.getQrCodeId());
        if (formId == 0L) {
            throw new ServiceException(500, "二维码不正确");
        }
        //添加data
        FormData data = new FormData();
        data.setModule(formDataPojo.getModule());
        data.setFormId(formId);
        //判断抽检
        FormData formData = formDataService.query().eq("form_id", formId).eq("status", 0).one();
        if (ToolUtil.isNotEmpty(formData)) {
            throw new ServiceException(500, "data数据错误");
        }
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
                    FormValues.DataValues dataValues = JSONUtil.toBean(value.getValue(), FormValues.DataValues.class);
                    if (ToolUtil.isEmpty(dataValues.getValue())) {
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


    }

    @Override
    public QualityTaskResult backChildTask(Long id) {
        //子任务
        QualityTask qualityTask = this.getById(id);
        if (ToolUtil.isEmpty(qualityTask)) {
            throw new ServiceException(500, "参数不正确");
        }
        QualityTaskResult taskResult = new QualityTaskResult();
        ToolUtil.copyProperties(qualityTask, taskResult);

        QualityTask fatherTask = this.getById(qualityTask.getParentId());

        List<QualityTaskDetailResult> taskDetailResults = detailService.getTaskDetailResults(qualityTask.getQualityTaskId());

        List<Long> brandIds = new ArrayList<>();
        List<Long> planIds = new ArrayList<>();
        for (QualityTaskDetailResult taskDetail : taskDetailResults) {
            brandIds.add(taskDetail.getBrandId());
            planIds.add(taskDetail.getQualityPlanId());
        }

        List<BrandResult> brandResults = brandService.getBrandResults(brandIds);

        List<QualityPlanResult> planResults = planService.getPlanResults(planIds);

        //组合数据
        for (QualityTaskDetailResult taskDetail : taskDetailResults) {
            for (BrandResult brandResult : brandResults) {
                if (taskDetail.getBrandId().equals(brandResult.getBrandId())) {
                    SkuResult skuResult = skuService.getSku(taskDetail.getSkuId());
                    taskDetail.setSkuResult(skuResult);
                    taskDetail.setBrand(brandResult);
                }
            }
        }
        for (QualityTaskDetailResult detailResult : taskDetailResults) {
            for (QualityPlanResult qualityPlan : planResults) {
                if (detailResult.getQualityPlanId().equals(qualityPlan.getQualityPlanId())) {
                    detailResult.setQualityPlanResult(qualityPlan);
                }
            }

        }
        taskResult.setDetails(taskDetailResults);

        taskResult.setFatherTask(fatherTask);

        User createUser = userService.getById(taskResult.getCreateUser());
        taskResult.setCreateName(createUser.getName());


        //判断是否可以入库
        QualityTask FatherTask = this.getById(taskResult.getParentId());
        ActivitiProcessTask processTask = processTaskService.query().eq("form_id", fatherTask.getQualityTaskId()).one();


        RuleType ruleType = quality_dispatch;
        Boolean isNext = true;
        switch (taskResult.getState()) {
            case 0:
                ruleType = quality_dispatch;
                break;
            case 1:
                ruleType = quality_perform;
                break;
            case 2:
                ruleType = quality_complete;
                break;
            default:
                isNext = false;
        }

        if (isNext) {
            ActivitiProcessTask one = activitiProcessTaskService.getOne(new QueryWrapper<ActivitiProcessTask>() {{
                eq("form_id", taskResult.getParentId());
                eq("type", "quality_task");
            }});
            ActivitiProcessTask Task = activitiProcessTaskService.getById(one.getProcessTaskId());
            isNext = activitiProcessLogService.judgeStatus(Task, ruleType);

        }

        taskResult.setIsNext(isNext);
        return taskResult;
    }

    @Override
    public FormDataRequest valueResults(Long qrcodeId, String type) {
        FormDataRequest formDataRequest = new FormDataRequest();

        //通过二维码找到实物id
        Long formId = bindService.getFormId(qrcodeId);
        if (formId == 0L) {
            throw new ServiceException(500, "二维码不正确");
        }
        //通过实物id找到data
        FormData formData = new FormData();
        switch (type) {
            case "sampling":
                formData = formDataService.query().eq("form_id", formId).eq("status", 0).one();
                break;
            case "fixed":
                formData = formDataService.query().eq("form_id", formId).one();
                break;
        }
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

        for (FormDataValueResult formDataValueResult : formDataValueResults) {
            FormValues.DataValues values = JSONUtil.toBean(formDataValueResult.getValue(), FormValues.DataValues.class);
            formDataValueResult.setDataValues(values);
        }
        formDataRequest.setData(formData);
        formDataRequest.setDataValueResults(formDataValueResults);
        return formDataRequest;
    }

    @Override
    public void updateDataValue(FormValues formValues) {
        FormDataValue value = formDataValueService.query().eq("value_id", formValues.getId()).one();
        if (ToolUtil.isEmpty(value)) {
            throw new ServiceException(500, "参数不正确");
        }
        FormData data = formDataService.query().eq("data_id", value.getDataId()).one();
        if (ToolUtil.isNotEmpty(data) && data.getStatus() == 99) {
            throw new ServiceException(500, "当前质检已完成，不可以修改");
        }

        //TODO 修改datavalue
        FormDataValue dataValue = new FormDataValue();
        dataValue.setValue(JSONUtil.toJsonStr(formValues.getDataValues()));
        formDataValueService.update(dataValue, new QueryWrapper<FormDataValue>() {{
            eq("value_id", formValues.getId());
        }});
    }

    /**
     * 质检完成
     *
     * @param taskComplete
     */
    @Override
    public void taskComplete(TaskComplete taskComplete) {
        List<FormDataValue> valueList = formDataValueService.query().in("value_id", taskComplete.getValueIds()).list();

        List<Long> planIds = new ArrayList<>();
        for (FormDataValue formDataValue : valueList) {
            planIds.add(formDataValue.getField());
        }
        List<QualityPlanDetail> details = qualityPlanDetailService.query().in("plan_detail_id", planIds).eq("is_null", 1).list();
        //判断必填项
        Boolean t = judge(valueList, details);
        if (!t) {
            throw new ServiceException(500, "请检查必填项");
        }
        FormData formData = new FormData();
        formData.setStatus(taskComplete.getStatus());
        formDataService.update(formData, new QueryWrapper<FormData>() {{
            eq("data_id", valueList.get(0).getDataId());
        }});
        //修改质检任务详情
        QualityTaskDetail detail = detailService.query().eq("quality_task_detail_id", taskComplete.getTaskDetailId()).one();
        if (detail.getNumber() > detail.getRemaining()) {
            detail.setRemaining(detail.getRemaining() + 1);
            detailService.updateById(detail);
        }
    }

    /**
     * 更新子任务
     *
     * @param taskId
     */
    @Override
    public void updateChildTask(Long taskId, Integer state) {
        //更新当前子任务
        QualityTask task = this.query().eq("quality_task_id", taskId).one();

        if (task.getState() == (state - 1)) {
            task.setState(state);
            this.updateById(task);
        }
        //判断所有子任务
        List<QualityTask> tasks = this.query().eq("parent_id", task.getParentId()).list();

        boolean t = true;
        for (QualityTask qualityTask : tasks) {
            if (!qualityTask.getState().equals(state)) {
                t = false;
            }
        }

        List<QualityTaskDetail> taskDetails = detailService.query().eq("quality_task_id", task.getParentId()).list();
        switch (state) {
            case 2:
                //判断父级任务分配数量
                for (QualityTaskDetail taskDetail : taskDetails) {
                    if (taskDetail.getRemaining() != 0) {
                        t = false;
                    }
                }
                break;
            default:
                break;
        }

        //子任务完成更新父级任务
        if (t) {
            QualityTask fathTask = new QualityTask();
            fathTask.setState(state);
            this.update(fathTask, new QueryWrapper<QualityTask>() {{
                eq("quality_task_id", task.getParentId());
            }});
            /**
             * TODO id错误
             */
            ActivitiProcessTask processTask = activitiProcessTaskService.getByFormId(task.getParentId());
            activitiProcessLogService.autoAudit(processTask.getProcessTaskId());
        }
    }

    /**
     * 返回主任务
     *
     * @param id
     * @return
     */
    @Override
    public QualityTaskResult getTask(Long id) {
        QualityTask task = this.getById(id);
        if (ToolUtil.isEmpty(task)) {
            throw new ServiceException(500, "参数不正确");
        }

        QualityTaskResult qualityTaskResult = new QualityTaskResult();
        ToolUtil.copyProperties(task, qualityTaskResult);
        //查询创建者
        User user = userService.getById(qualityTaskResult.getCreateUser());
        qualityTaskResult.setUser(user);
        //查询子任务
        List<QualityTask> childTasks = this.query().eq("parent_id", task.getQualityTaskId()).list();

        List<QualityTaskResult> taskResults = new ArrayList<>();


        //查询子任务的负责人
        List<Long> userIds = new ArrayList<>();
        for (QualityTask childTask : childTasks) {
            QualityTaskResult childTaskResult = new QualityTaskResult();
            ToolUtil.copyProperties(childTask, childTaskResult);
            taskResults.add(childTaskResult);
            String[] userId = childTask.getUserIds().split(",");
            for (String uId : userId) {
                userIds.add(Long.valueOf(uId));
            }
            userIds.add(childTask.getCreateUser());
        }

        List<User> users = userService.listByIds(userIds);

        for (QualityTaskResult taskResult : taskResults) {
            for (User createUser : users) {
                if (taskResult.getCreateUser().equals(createUser.getUserId())) {
                    taskResult.setUser(createUser);
                }

                //比对多个负责人
                String[] userId = taskResult.getUserIds().split(",");
                List<User> persons = getusers(users, userId);
                taskResult.setUsers(persons);
            }
        }
//        RuleType ruleType = quality_dispatch;
//        Long taskId = 0L;
//        Boolean isNext = true;
//        switch (qualityTaskResult.getState()) {
//            case 0:
//                ruleType = quality_dispatch;
//                break;
//            case 1:
//                ruleType = quality_perform;
//                break;
//            case 2:
//                ruleType = quality_complete;
//                break;
//            default:
//                isNext =false;
//        }
//        if(isNext){
//            ActivitiProcessTask one = activitiProcessTaskService.getOne(new QueryWrapper<ActivitiProcessTask>() {{
//                eq("form_id", task.getQualityTaskId());
//                eq("type", "quality_task");
//            }});
//            ActivitiProcessTask processTask = activitiProcessTaskService.getById(one.getProcessTaskId());
//            isNext = activitiProcessLogService.judgeStatus(processTask, ruleType);
//        }

        qualityTaskResult.setChildTasks(taskResults);
        return qualityTaskResult;
    }

    /**
     * 返回当前data
     *
     * @param bindParam
     * @return
     */
    @Override
    public List<FormDataResult> getDetail(BindParam bindParam) {
        //查询绑定信息
        List<Long> formIds = bindService.backValue(bindParam.getQrcodeIds());
        //查询data
        List<FormDataResult> dataResults = formDataService.getDataIds(formIds);
        List<Long> dataIds = new ArrayList<>();
        for (FormDataResult dataResult : dataResults) {
            dataIds.add(dataResult.getDataId());
        }
        //查询dataValue
        List<FormDataValueResult> results = getDataValuesByDataId(dataIds);
        List<Long> planIds = new ArrayList<>();
        for (FormDataValueResult result : results) {
            planIds.add(result.getField());
        }
        //查询质检方案详情
        List<QualityPlanDetailResult> planDetailResults = qualityPlanDetailService.getPlanDetailResults(planIds);

        List<Long> checkIds = new ArrayList<>();
        for (QualityPlanDetailResult planDetailResult : planDetailResults) {
            checkIds.add(planDetailResult.getQualityCheckId());
        }
        //查询质检项
        List<QualityCheckResult> checkResults = checkService.getChecks(checkIds);
        //比对数据
        for (QualityPlanDetailResult planDetailResult : planDetailResults) {
            for (QualityCheckResult checkResult : checkResults) {
                if (planDetailResult.getQualityCheckId().equals(checkResult.getQualityCheckId())) {
                    planDetailResult.setQualityCheckResult(checkResult);
                    break;
                }
            }
        }

        for (FormDataValueResult result : results) {
            for (QualityPlanDetailResult planDetailResult : planDetailResults) {
                if (result.getField().equals(planDetailResult.getPlanDetailId())) {
                    result.setQualityPlanDetailResult(planDetailResult);
                    break;
                }
            }
        }


        for (FormDataResult dataResult : dataResults) {
            List<Object> dataValueResults = new ArrayList<>();
            for (FormDataValueResult result : results) {
                if (dataResult.getDataId().equals(result.getDataId())) {
                    dataValueResults.add(result);
                }
            }
            dataResult.setValues(dataValueResults);
        }

        return dataResults;
    }

    /**
     * 返回可以入库data
     *
     * @param bindParam
     * @return
     */
    @Override
    public List<FormDataResult> inStockDetail(BindParam bindParam) {
        //查询绑定信息
        List<OrCodeBind> codeBinds = bindService.query().in("qr_code_id", bindParam.getQrcodeIds()).list();
        List<Long> formIds = new ArrayList<>();
        for (OrCodeBind codeBind : codeBinds) {
            formIds.add(codeBind.getFormId());
        }

        //查询data
        List<FormDataResult> dataResults = formDataService.getDataIds(formIds);
        List<Long> dataIds = new ArrayList<>();
        List<Long> inKindIds = new ArrayList<>();
        for (FormDataResult dataResult : dataResults) {
            dataIds.add(dataResult.getDataId());
            inKindIds.add(dataResult.getFormId());
        }

        //查询dataValue
        List<FormDataValueResult> results = getDataValuesByDataId(dataIds);

        //查询实物
        List<InkindResult> inKindResults = inkindService.getInKinds(inKindIds);
        Map<Long, Long> map = new HashMap<>();
        for (InkindResult inKindResult : inKindResults) {
            for (OrCodeBind codeBind : codeBinds) {
                if (codeBind.getFormId().equals(inKindResult.getInkindId())) {
                    map.put(inKindResult.getInkindId(), codeBind.getOrCodeId());
                }
            }

        }
        for (FormDataResult dataResult : dataResults) {
            Long qrcodeId = map.get(dataResult.getFormId());
            dataResult.setQrcodeId(qrcodeId);
            List<Object> dataValueResults = new ArrayList<>();
            for (FormDataValueResult result : results) {
                if (result.getDataId().equals(dataResult.getDataId())) {
                    dataValueResults.add(result);
                }
            }
            dataResult.setValues(dataValueResults);
        }
        return dataResults;
    }


    public List<FormDataValueResult> getDataValuesByDataId(List<Long> dataId) {
        if (ToolUtil.isEmpty(dataId)) {
            return new ArrayList<>();
        }
        List<FormDataValue> dataValues = formDataValueService.query().in("data_id", dataId).list();

        List<FormDataValueResult> results = new ArrayList<>();

        for (FormDataValue dataValue : dataValues) {
            FormDataValueResult dataValueResult = new FormDataValueResult();
            ToolUtil.copyProperties(dataValue, dataValueResult);
            FormValues.DataValues values = JSONUtil.toBean(dataValue.getValue(), FormValues.DataValues.class);
            dataValueResult.setDataValues(values);
            results.add(dataValueResult);
        }
        return results;
    }


    /**
     * 返回多个负责人
     *
     * @param users
     * @param userIds
     * @return
     */
    private List<User> getusers(List<User> users, String[] userIds) {
        List<User> userList = new ArrayList<>();
        for (User user : users) {
            for (String userId : userIds) {
                if (user.getUserId().toString().equals(userId)) {
                    userList.add(user);
                }
            }
        }


        return userList;
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
