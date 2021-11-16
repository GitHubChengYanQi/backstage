package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.QualityTask;
import cn.atsoft.dasheng.erp.entity.QualityTaskDetail;
import cn.atsoft.dasheng.erp.entity.Sku;
import cn.atsoft.dasheng.erp.entity.Tool;
import cn.atsoft.dasheng.erp.mapper.QualityTaskMapper;
import cn.atsoft.dasheng.erp.model.params.QualityTaskDetailParam;
import cn.atsoft.dasheng.erp.model.params.QualityTaskParam;
import cn.atsoft.dasheng.erp.model.request.FormDataPojo;
import cn.atsoft.dasheng.erp.model.result.QualityIssuessResult;
import cn.atsoft.dasheng.erp.model.result.QualityTaskDetailResult;
import cn.atsoft.dasheng.erp.model.result.QualityTaskResult;
import cn.atsoft.dasheng.erp.model.result.SkuResult;
import cn.atsoft.dasheng.erp.service.QualityTaskDetailService;
import cn.atsoft.dasheng.erp.service.QualityTaskService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.service.SkuService;
import cn.atsoft.dasheng.form.entity.FormData;
import cn.atsoft.dasheng.form.entity.FormDataValue;
import cn.atsoft.dasheng.form.service.FormDataService;
import cn.atsoft.dasheng.form.service.FormDataValueService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        FormData formData = new FormData();
        formData.setModule(formDataPojo.getModule());
        formData.setFormId(formDataPojo.getFormId());
        formData.setMainId(0L);
        formDataService.save(formData);
        FormDataValue formDataValue = new FormDataValue();
        formDataValue.setValue(formDataPojo.getValue());
        formDataValue.setDataId(formData.getDataId());
        formDataValue.setField(formDataPojo.getField());
        formDataValueService.save(formDataValue);
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
