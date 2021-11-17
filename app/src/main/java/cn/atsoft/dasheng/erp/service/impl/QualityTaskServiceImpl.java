package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.*;
import cn.atsoft.dasheng.erp.mapper.QualityTaskMapper;
import cn.atsoft.dasheng.erp.model.params.QualityTaskDetailParam;
import cn.atsoft.dasheng.erp.model.params.QualityTaskParam;
import cn.atsoft.dasheng.erp.model.request.FormDataPojo;
import cn.atsoft.dasheng.erp.model.request.FormValues;
import cn.atsoft.dasheng.erp.model.result.*;
import cn.atsoft.dasheng.erp.service.QualityCheckService;
import cn.atsoft.dasheng.erp.service.QualityTaskDetailService;
import cn.atsoft.dasheng.erp.service.QualityTaskService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.service.SkuService;
import cn.atsoft.dasheng.form.entity.FormData;
import cn.atsoft.dasheng.form.entity.FormDataValue;
import cn.atsoft.dasheng.form.model.params.FormDataParam;
import cn.atsoft.dasheng.form.model.result.FormDataResult;
import cn.atsoft.dasheng.form.service.FormDataService;
import cn.atsoft.dasheng.form.service.FormDataValueService;
import cn.atsoft.dasheng.orCode.entity.OrCodeBind;
import cn.atsoft.dasheng.orCode.model.result.BackCodeRequest;
import cn.atsoft.dasheng.orCode.service.OrCodeBindService;
import cn.atsoft.dasheng.orCode.service.OrCodeService;
import cn.atsoft.dasheng.portal.remind.model.params.WxTemplateData;
import cn.atsoft.dasheng.sendTemplate.WxCpSendTemplate;
import cn.atsoft.dasheng.sendTemplate.WxCpTemplate;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
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

    @Override
    @Transactional
    public void add(QualityTaskParam param) {
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
        backCodeRequest.setSource("qualitytask");
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
        return PageFactory.createPageInfo(page);
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
    public void formDataFormat(FormDataResult param) {
        Long dataId = param.getDataId();
        List<FormDataValue> formDataValues = formDataValueService.lambdaQuery().eq(FormDataValue::getDataId, dataId).and(i -> i.eq(FormDataValue::getDisplay, 1)).list();
        List<Long> checkIds = new ArrayList<>();
        for (FormDataValue formDataValue : formDataValues) {
            checkIds.add(formDataValue.getField());
        }
        List<QualityCheck> qualityChecklist = qualityCheckService.lambdaQuery().in(QualityCheck::getQualityCheckId, checkIds).eq(QualityCheck::getDisplay, 1).list();
        List<QualityCheckResult> qualityCheckResults = new ArrayList<>();
        for (QualityCheck qualityCheck : qualityChecklist) {
            QualityCheckResult qualityCheckResult = new QualityCheckResult();
            ToolUtil.copyProperties(qualityCheck, qualityCheckResult);
            qualityCheckResults.add(qualityCheckResult);
        }
        List<Map<String, Object>> maps = new ArrayList<>();
        for (FormDataValue formDataValue : formDataValues) {
            for (QualityCheckResult qualityCheck : qualityCheckResults) {
                if (qualityCheck.getQualityCheckId().equals(formDataValue.getField())) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("name", qualityCheck.getName());
                    map.put("value", formDataValue.getValue());
                    map.put("field", qualityCheck);
                    maps.add(map);
                }

            }
        }
        param.setValueResults(maps);
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
