package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.app.entity.Brand;
import cn.atsoft.dasheng.app.service.BrandService;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.*;
import cn.atsoft.dasheng.erp.mapper.QualityTaskMapper;
import cn.atsoft.dasheng.erp.model.params.QualityTaskDetailParam;
import cn.atsoft.dasheng.erp.model.params.QualityTaskParam;
import cn.atsoft.dasheng.erp.model.request.FormDataPojo;
import cn.atsoft.dasheng.erp.model.request.FormValues;
import cn.atsoft.dasheng.erp.model.result.*;
import cn.atsoft.dasheng.erp.service.*;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.form.entity.FormData;
import cn.atsoft.dasheng.form.entity.FormDataValue;
import cn.atsoft.dasheng.form.model.result.FormDataResult;
import cn.atsoft.dasheng.form.service.FormDataService;
import cn.atsoft.dasheng.form.service.FormDataValueService;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.orCode.entity.OrCodeBind;
import cn.atsoft.dasheng.orCode.model.result.BackCodeRequest;
import cn.atsoft.dasheng.orCode.service.OrCodeBindService;
import cn.atsoft.dasheng.orCode.service.OrCodeService;
import cn.atsoft.dasheng.sendTemplate.WxCpSendTemplate;
import cn.atsoft.dasheng.sendTemplate.WxCpTemplate;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.sf.jsqlparser.expression.LongValue;
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
            List<QualityTaskDetail> details = new ArrayList<>();
            for (QualityTaskDetailParam detailParam : param.getDetails()) {
                QualityTaskDetail detail = new QualityTaskDetail();
                detailParam.setQualityTaskId(entity.getQualityTaskId());
                ToolUtil.copyProperties(detailParam, detail);
                details.add(detail);
            }

            detailService.saveBatch(details);
        }
        WxCpTemplate wxCpTemplate = new WxCpTemplate();
        List<Long> userIds = new ArrayList<>();
        userIds.add(param.getUserId());
        wxCpTemplate.setUserIds(userIds);
        BackCodeRequest backCodeRequest = new BackCodeRequest();
        backCodeRequest.setId(entity.getQualityTaskId());
        backCodeRequest.setSource("quality");
        Long aLong = orCodeService.backCode(backCodeRequest);
        String url = param.getUrl().replace("codeId", aLong.toString());
        wxCpTemplate.setUrl(url);
        wxCpTemplate.setTitle("质检任务提醒");
        wxCpTemplate.setDescription("有新的质检任务");
        wxCpSendTemplate.setWxCpTemplate(wxCpTemplate);
        wxCpSendTemplate.sendTemplate();
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
        List<User> users = userService.lambdaQuery().in(User::getUserId, userIds).and(i -> i.eq(User::getStatus, "ENABLE")).list();
        for (QualityTaskResult qualityTaskResult : param) {
            for (User user : users) {
                if (qualityTaskResult.getUserId().equals(user.getUserId())) {
                    qualityTaskResult.setUserName(user.getName());
                }
            }
        }

    }


    @Override
    public void addFormData(FormDataPojo formDataPojo) {
        OrCodeBind codeId = bindService.query().eq("qr_code_id", formDataPojo.getFormId()).one();
        if (codeId.getSource().equals("item")) {

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
            for (FormValues formValue : formValues) {
                FormDataValue formDataValue = new FormDataValue();
                formDataValue.setValue(formValue.getValue());
                formDataValue.setDataId(formData.getDataId());
                formDataValue.setField(formValue.getField());
                formValuesList.add(formDataValue);

            }
            formDataValueService.saveBatch(formValuesList);
        }


    }

    @Override
    public List<TaskCount> backIkind(Long id) {
        //通过绑定找出ikinds
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
//        Long formId = param.getFormId();
//        Inkind one = inkindService.lambdaQuery().eq(Inkind::getInkindId, formId).and(i -> i.eq(Inkind::getDisplay, 1)).one();
//        param.setInkind(one);
//        Long dataId = param.getDataId();
//        List<FormDataValue> formDataValues = formDataValueService.lambdaQuery().eq(FormDataValue::getDataId, dataId).and(i -> i.eq(FormDataValue::getDisplay, 1)).list();
//        List<Long> planIds = new ArrayList<>();
//        for (FormDataValue formDataValue : formDataValues) {
//            planIds.add(formDataValue.getField());
//        }
//        List<QualityPlanDetail> planDetails = planIds.size() == 0 ? new ArrayList<>() : qualityPlanDetailService.lambdaQuery().in(QualityPlanDetail::getPlanDetailId, planIds).and(i -> i.eq(QualityPlanDetail::getDisplay, 1)).list();
//        List<Long> checkIds = new ArrayList<>();
//        for (QualityPlanDetail planDetail : planDetails) {
//            checkIds.add(planDetail.getQualityCheckId());
//        }
//
//
//        List<QualityCheck> qualityChecklist = checkIds.size() == 0 ? new ArrayList<>() : qualityCheckService.lambdaQuery().in(QualityCheck::getQualityCheckId, checkIds).eq(QualityCheck::getDisplay, 1).list();
//        List<QualityCheckResult> qualityCheckResults = new ArrayList<>();
//        for (QualityCheck qualityCheck : qualityChecklist) {
//            QualityCheckResult qualityCheckResult = new QualityCheckResult();
//            ToolUtil.copyProperties(qualityCheck, qualityCheckResult);
//            qualityCheckResults.add(qualityCheckResult);
//        }
//        List<Map<String, Object>> maps = new ArrayList<>();
//        for (FormDataValue formDataValue : formDataValues) {
//            for (QualityPlanDetail planDetail : planDetails) {
//                if (formDataValue.getField().equals(planDetail.getPlanDetailId())) {
//                    for (QualityCheckResult qualityCheck : qualityCheckResults) {
//                        if (qualityCheck.getQualityCheckId().equals(planDetail.getQualityCheckId())) {
//                            Map<String, Object> map = new HashMap<>();
//                            map.put("name", qualityCheck.getName());
//                            map.put("value", formDataValue.getValue());
//                            map.put("field", qualityCheck);
//                            maps.add(map);
//                        }
//
//                    }
//                }
//            }
//
//        }
//        param.setValueResults(maps);
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
        List<Brand> brandList =brandIds.size() == 0 ? new ArrayList<>() : brandService.lambdaQuery().in(Brand::getBrandId, brandIds).and(i -> i.eq(Brand::getDisplay, 1)).list();

        List<Sku> skus = skuService.lambdaQuery().in(Sku::getSkuId, skuIds).and(i -> i.eq(Sku::getDisplay, 1)).list();
        List<SkuResult> skuResults = new ArrayList<>();
        for (Sku sku : skus) {
            SkuResult skuResult = new SkuResult();
            ToolUtil.copyProperties(sku,skuResult);
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
                        if (skuResult.getSkuId().equals(inkind.getSkuId())){
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
                                    map.put("value", formDataValue.getValue());
                                    Boolean flag = false;
                                    if (ToolUtil.isEmpty(planDetail.getOperator())||ToolUtil.isEmpty(planDetail.getStandardValue())) {
                                        flag = false;
                                    }else {
                                        flag = this.mathData(planDetail.getStandardValue(), planDetail.getOperator(), Long.valueOf(formDataValue.getValue()));
                                    }

                                    map.put("standar",flag);
                                    map.put("field", planDetail);
                                    map.put("type",qualityCheck.getType());
                                    maps.add(map);
                                }

                            }
                        }
                    }
                }
            }
            formDataResult.setValueResults(maps);

        }
    }
    private Boolean mathData (String standardValue,Long operator,Long value){
        Boolean flag = false;
        switch (operator.toString()){
            case "1":
                if (value == Long.parseLong(standardValue))
                    flag = true;
                break;
            case "2":
                if (value >= Long.parseLong(standardValue) )
                    flag = true;
                break;
            case "3":
                if (value <= Long.parseLong(standardValue) )
                    flag = true;
                break;
            case "4":
                if (value > Long.parseLong(standardValue) )
                    flag = true;
                break;
            case "5":
                if (value < Long.parseLong(standardValue) )
                    flag = true;
                break;
            case "6":
                List<String> result = Arrays.asList(standardValue.split(","));
                if (value >= Long.parseLong(result.get(0)) &&  value <= Long.parseLong(result.get(1)))
                    flag = true;
                break;

        }
        return flag;
    }
    @Override
    public void detailFormat(QualityTaskResult param) {
//        List<String> skuIds = new ArrayList<>();
//        for (QualityIssuessResult qualityIssuessResult : param) {
//            if (ToolUtil.isNotEmpty(qualityIssuessResult.getSkuIds())) {
//                skuIds = Arrays.asList(qualityIssuessResult.getSkuIds().split(","));
//            }
//        }
//        List<Sku> skuList = skuIds.size() == 0 ? new ArrayList<>() : skuService.lambdaQuery().in(Sku::getSkuId, skuIds).and(i -> i.eq(Sku::getDisplay, 1)).list();
//        List<SkuResult> skuResultList = new ArrayList<>();
//        for (Sku sku : skuList) {
//            SkuResult skuResult = new SkuResult();
//            ToolUtil.copyProperties(sku,skuResult);
//            skuResultList.add(skuResult);
//        }
//        skuService.format(skuResultList);
//        for (QualityIssuessResult qualityIssuessResult : param) {
//            if (ToolUtil.isNotEmpty(qualityIssuessResult.getSkuIds())) {
//                skuIds = Arrays.asList(qualityIssuessResult.getSkuIds().split(","));
//            }
//            List<SkuResult> skuResults = new ArrayList<>();
//            for (String skuId : skuIds) {
//                for (SkuResult sku : skuResultList) {
//                    if (skuId.equals(sku.getSkuId().toString())) {
//                        SkuResult skuResult = new SkuResult();
//                        ToolUtil.copyProperties(sku,skuResult);
//                        skuResults.add(skuResult);
//                    }
//                }
//            }
//            qualityIssuessResult.setSkuResults(skuResults);
//        }
//
        List<Long> skuids = new ArrayList<>();
        List<QualityTaskDetail> qualityTaskDetails = detailService.lambdaQuery().in(QualityTaskDetail::getQualityTaskId, param.getQualityTaskId()).and(i -> i.eq(QualityTaskDetail::getDisplay, 1)).list();
        List<QualityTaskDetailResult> qualityTaskDetailResults = new ArrayList<>();
        for (QualityTaskDetail qualityTaskDetail : qualityTaskDetails) {
            QualityTaskDetailResult qualityTaskDetailResult = new QualityTaskDetailResult();
            ToolUtil.copyProperties(qualityTaskDetail, qualityTaskDetailResult);
            qualityTaskDetailResults.add(qualityTaskDetailResult);
        }
        detailService.format(qualityTaskDetailResults);
        param.setDetails(qualityTaskDetailResults);


        //        for (QualityTaskDetail qualityTaskDetail : qualityTaskDetails) {
//            skuids.add(qualityTaskDetail.getSkuId());
//        }
//        List<SkuResult> skuResults = new ArrayList<>();
//        List<Sku> skus = skuService.lambdaQuery().in(Sku::getSkuId, skuids).and(i -> i.eq(Sku::getDisplay, 1)).list();
//        for (Sku sku : skus) {
//            SkuResult skuResult = new SkuResult();
//            ToolUtil.copyProperties(sku,skuResult);
//            skuResults.add(skuResult);
//        }
//        skuService.format(skuResults);
//        List<QualityTaskDetailResult> detailResults = new ArrayList<>();
//        for (QualityTaskDetail qualityTaskDetail : qualityTaskDetails) {
//            for (SkuResult skuResult : skuResults) {
//                if (qualityTaskDetail.getSkuId().equals(skuResult.getSkuId())) {
//                    QualityTaskDetailResult detailResult = new QualityTaskDetailResult();
//                    ToolUtil.copyProperties(qualityTaskDetail,detailResult);
//                    detailResult.setSkuResult(skuResult);
//                    detailResults.add(detailResult);
//                }
//            }
//        }
//
//        param.setDetails(detailResults);

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
