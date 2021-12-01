package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.app.entity.Brand;
import cn.atsoft.dasheng.app.entity.Unit;
import cn.atsoft.dasheng.app.model.result.BrandResult;
import cn.atsoft.dasheng.app.service.BrandService;
import cn.atsoft.dasheng.app.service.UnitService;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.crm.entity.Data;
import cn.atsoft.dasheng.erp.config.MobileService;
import cn.atsoft.dasheng.erp.entity.*;
import cn.atsoft.dasheng.erp.mapper.QualityTaskDetailMapper;
import cn.atsoft.dasheng.erp.model.params.QualityTaskDetailParam;
import cn.atsoft.dasheng.erp.model.result.*;
import cn.atsoft.dasheng.erp.service.*;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.form.entity.FormData;
import cn.atsoft.dasheng.form.entity.FormDataValue;
import cn.atsoft.dasheng.form.model.result.FormDataValueResult;
import cn.atsoft.dasheng.form.service.FormDataService;
import cn.atsoft.dasheng.form.service.FormDataValueService;
import cn.atsoft.dasheng.orCode.entity.OrCodeBind;
import cn.atsoft.dasheng.orCode.service.OrCodeBindService;
import cn.atsoft.dasheng.orCode.entity.OrCodeBind;
import cn.atsoft.dasheng.orCode.service.OrCodeBindService;
import cn.atsoft.dasheng.sendTemplate.WxCpSendTemplate;
import cn.atsoft.dasheng.sendTemplate.WxCpTemplate;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 质检任务详情 服务实现类
 * </p>
 *
 * @author
 * @since 2021-11-16
 */
@Service
public class QualityTaskDetailServiceImpl extends ServiceImpl<QualityTaskDetailMapper, QualityTaskDetail> implements QualityTaskDetailService {

    @Autowired
    private SkuService skuService;
    @Autowired
    private BrandService brandService;
    @Autowired
    private QualityPlanService qualityPlanService;
    @Autowired
    private QualityPlanDetailService qualityPlanDetailService;
    @Autowired
    private QualityCheckService qualityCheckService;
    @Autowired
    private UnitService unitService;
    @Autowired
    private FormDataService dataService;
    @Autowired
    private FormDataValueService valueService;
    @Autowired
    private OrCodeBindService bindService;

    private WxCpSendTemplate wxCpSendTemplate;
    @Autowired
    private MobileService mobileService;
    @Autowired
    private OrCodeBindService bindService;
    @Override
    public void add(QualityTaskDetailParam param) {
        QualityTaskDetail entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(QualityTaskDetailParam param) {
        this.removeById(getKey(param));
    }

    @Override
    public void addDetails(QualityTaskDetailParam param) {

        List<QualityTaskDetail> taskDetails = this.list(new QueryWrapper<QualityTaskDetail>() {{
            in("quality_task_detail_id", param.getDetailsIds());
        }});

        for (QualityTaskDetail taskDetail : taskDetails) {
            taskDetail.setUserIds(param.getUserIds());
            taskDetail.setAddress(param.getAddress());
            taskDetail.setPerson(param.getPerson());
            taskDetail.setTime(param.getTime());
            taskDetail.setPhone(param.getPhone());
            taskDetail.setNote(param.getNote());
        }

        this.updateBatchById(taskDetails);
        WxCpTemplate wxCpTemplate = new WxCpTemplate();
        List<Long> users = Arrays.asList(param.getUserIds().split(",")).stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
        OrCodeBind formId = bindService.query().eq("form_id", param.getQualityTaskId()).one();
        wxCpTemplate.setUserIds(users);
        String url = mobileService.getMobileConfig().getUrl();
        url = url +"OrCode?id="+formId.getOrCodeId();
        wxCpTemplate.setUrl(url);
        wxCpTemplate.setDescription("点击查看新质检任务");
        wxCpTemplate.setTitle("您被分派新的任务");

        wxCpSendTemplate.getWxCpTemplate();
        wxCpSendTemplate.sendTemplate();
    }

    @Override
    public void update(QualityTaskDetailParam param) {
        QualityTaskDetail oldEntity = getOldEntity(param);
        QualityTaskDetail newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public QualityTaskDetailResult findBySpec(QualityTaskDetailParam param) {
        return null;
    }

    @Override
    public List<QualityTaskDetailResult> findListBySpec(QualityTaskDetailParam param) {
        return null;
    }

    @Override
    public PageInfo<QualityTaskDetailResult> findPageBySpec(QualityTaskDetailParam param) {
        Page<QualityTaskDetailResult> pageContext = getPageContext();
        IPage<QualityTaskDetailResult> page = this.baseMapper.customPageList(pageContext, param);
        this.format(page.getRecords());
        return PageFactory.createPageInfo(page);
    }

    @Override
    public List<FormDataValueResult> backData(Long qrcodeId) {
        List<FormDataValueResult> dataValueResults = new ArrayList<>();

        OrCodeBind codeId = bindService.query().eq("qr_code_id", qrcodeId).one();
        if (ToolUtil.isNotEmpty(codeId)) {
            FormData formData = dataService.getOne(new QueryWrapper<FormData>() {{
                eq("form_id", codeId.getFormId());
            }});

            if (ToolUtil.isNotEmpty(formData)) {
                List<FormDataValue> dataValues = valueService.list(new QueryWrapper<FormDataValue>() {{
                    eq("data_id", formData.getDataId());
                }});


                for (FormDataValue dataValue : dataValues) {
                    FormDataValueResult valueResult = new FormDataValueResult();
                    ToolUtil.copyProperties(dataValue, valueResult);
                    dataValueResults.add(valueResult);
                }
                return dataValueResults;
            }
        }

        return new ArrayList<>();
    }

    @Override
    public void updateDataValue(Long inkind) {

         dataService.list(new QueryWrapper<FormData>(){{
         }});
    }

    @Override
    public void format(List<QualityTaskDetailResult> param) {
        List<Long> skuIds = new ArrayList<>();
        //品牌id
        List<Long> brandIds = new ArrayList<>();
        //质检项id
        List<Long> planIds = new ArrayList<>();

        for (QualityTaskDetailResult qualityTaskDetailResult : param) {
            skuIds.add(qualityTaskDetailResult.getSkuId());
            brandIds.add(qualityTaskDetailResult.getBrandId());
            planIds.add(qualityTaskDetailResult.getQualityPlanId());
        }
        //查询品牌
        List<Brand> brandList = brandIds.size() == 0 ? new ArrayList<>() : brandService.lambdaQuery().in(Brand::getBrandId, brandIds).and(i -> i.eq(Brand::getDisplay, 1)).list();
        //查询质检项目
        List<QualityPlan> qualityPlanList = planIds.size() == 0 ? new ArrayList<>() : qualityPlanService.lambdaQuery().in(QualityPlan::getQualityPlanId, planIds).and(i -> i.eq(QualityPlan::getDisplay, 1)).list();
        //查询sku
        List<SkuResult> skuResults = new ArrayList<>();
        List<Sku> skus = skuIds.size() == 0 ? new ArrayList<>() : skuService.lambdaQuery().in(Sku::getSkuId, skuIds).and(i -> i.eq(Sku::getDisplay, 1)).list();
        for (Sku sku : skus) {
            SkuResult skuResult = new SkuResult();
            ToolUtil.copyProperties(sku, skuResult);
            skuResults.add(skuResult);
        }
        skuService.format(skuResults);
        List<QualityPlanDetail> qualityPlanDetails = planIds.size() == 0 ? new ArrayList<>() : qualityPlanDetailService.lambdaQuery().in(QualityPlanDetail::getPlanId, planIds).and(i -> i.eq(QualityPlanDetail::getDisplay, 1)).list();

        //取出qualityPlanDetails 中的 checkId
        List<Long> qualityCheckIds = new ArrayList<>();
        for (QualityPlanDetail qualityPlanDetail : qualityPlanDetails) {
            qualityCheckIds.add(qualityPlanDetail.getQualityCheckId());
        }
        List<QualityCheck> qualityChecks = qualityCheckIds.size() == 0 ? new ArrayList<>() : qualityCheckService.lambdaQuery().in(QualityCheck::getQualityCheckId, qualityCheckIds).and(i -> i.eq(QualityCheck::getDisplay, 1)).list();


        for (QualityTaskDetailResult qualityTaskDetailResult : param) {

            //格式化sku数据
            for (SkuResult skuResult : skuResults) {
                if (qualityTaskDetailResult.getSkuId().equals(skuResult.getSkuId())) {
                    qualityTaskDetailResult.setSkuResult(skuResult);
                }
            }
            //格式化品牌
            for (Brand brand : brandList) {
                if (qualityTaskDetailResult.getBrandId().equals(brand.getBrandId())) {
                    BrandResult brandResult = new BrandResult();
                    ToolUtil.copyProperties(brand, brandResult);
                    qualityTaskDetailResult.setBrand(brandResult);
                }
            }
            for (QualityPlan qualityPlan : qualityPlanList) {
                if (ToolUtil.isNotEmpty(qualityTaskDetailResult.getQualityPlanId())) {
                    if (qualityTaskDetailResult.getQualityPlanId().equals(qualityPlan.getQualityPlanId())) {
                        QualityPlanResult qualityPlanResult = new QualityPlanResult();
                        ToolUtil.copyProperties(qualityPlan, qualityPlanResult);
                        List<QualityPlanDetailResult> qualityPlanDetailResults = new ArrayList<>();
                        for (QualityPlanDetail qualityPlanDetail : qualityPlanDetails) {
                            if (qualityPlanDetail.getPlanId().equals(qualityPlan.getQualityPlanId())) {
                                QualityPlanDetailResult qualityPlanDetailResult = new QualityPlanDetailResult();
                                ToolUtil.copyProperties(qualityPlanDetail, qualityPlanDetailResult);
                                for (QualityCheck qualityCheck : qualityChecks) {
                                    if (qualityPlanDetail.getQualityCheckId().equals(qualityCheck.getQualityCheckId())) {
                                        QualityCheckResult qualityCheckResult = new QualityCheckResult();
                                        ToolUtil.copyProperties(qualityCheck, qualityCheckResult);
                                        qualityPlanDetailResult.setQualityCheckResult(qualityCheckResult);
                                    }
                                }
                                if (ToolUtil.isNotEmpty(qualityPlanDetailResult.getUnitId())) {
                                    Unit unit = unitService.getById(qualityPlanDetailResult.getUnitId());
                                    qualityPlanDetailResult.setUnit(unit);
                                }
                                qualityPlanDetailResults.add(qualityPlanDetailResult);
                            }
                        }
                        qualityPlanResult.setQualityPlanDetailParams(qualityPlanDetailResults);
                        qualityTaskDetailResult.setQualityPlanResult(qualityPlanResult);
                    }
                }

            }

        }


    }

    private Serializable getKey(QualityTaskDetailParam param) {
        return param.getQualityTaskDetailId();
    }

    private Page<QualityTaskDetailResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private QualityTaskDetail getOldEntity(QualityTaskDetailParam param) {
        return this.getById(getKey(param));
    }

    private QualityTaskDetail getEntity(QualityTaskDetailParam param) {
        QualityTaskDetail entity = new QualityTaskDetail();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
