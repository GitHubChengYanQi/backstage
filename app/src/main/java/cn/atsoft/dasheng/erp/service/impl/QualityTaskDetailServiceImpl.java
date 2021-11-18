package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.app.entity.Brand;
import cn.atsoft.dasheng.app.model.result.BrandResult;
import cn.atsoft.dasheng.app.service.BrandService;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.*;
import cn.atsoft.dasheng.erp.mapper.QualityTaskDetailMapper;
import cn.atsoft.dasheng.erp.model.params.QualityTaskDetailParam;
import cn.atsoft.dasheng.erp.model.result.*;
import cn.atsoft.dasheng.erp.service.*;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.ws.Action;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
            ToolUtil.copyProperties(sku,skuResult);
            skuResults.add(skuResult);
        }
        skuService.format(skuResults);
        List<QualityPlanDetail> qualityPlanDetails =planIds.size() == 0 ? new ArrayList<>() : qualityPlanDetailService.lambdaQuery().in(QualityPlanDetail::getPlanId, planIds).and(i -> i.eq(QualityPlanDetail::getDisplay, 1)).list();

        //取出qualityPlanDetails 中的 checkId
        List<Long> qualityCheckIds = new ArrayList<>();
        for (QualityPlanDetail qualityPlanDetail : qualityPlanDetails) {
            qualityCheckIds.add(qualityPlanDetail.getQualityCheckId());
        }
        List<QualityCheck> qualityChecks =qualityCheckIds.size() == 0 ? new ArrayList<>() : qualityCheckService.lambdaQuery().in(QualityCheck::getQualityCheckId, qualityCheckIds).and(i -> i.eq(QualityCheck::getDisplay, 1)).list();


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
                if (qualityTaskDetailResult.getQualityPlanId().equals(qualityPlan.getQualityPlanId())) {
                    QualityPlanResult qualityPlanResult = new QualityPlanResult();
                    ToolUtil.copyProperties(qualityPlan,qualityPlanResult);
                    List<QualityPlanDetailResult> qualityPlanDetailResults = new ArrayList<>();
                    for (QualityPlanDetail qualityPlanDetail : qualityPlanDetails) {
                        if (qualityPlanDetail.getPlanId().equals(qualityPlan.getQualityPlanId())) {
                            QualityPlanDetailResult qualityPlanDetailResult = new QualityPlanDetailResult();
                            ToolUtil.copyProperties(qualityPlanDetail,qualityPlanDetailResult);
                            for (QualityCheck qualityCheck : qualityChecks) {
                                if (qualityPlanDetail.getQualityCheckId().equals(qualityCheck.getQualityCheckId())) {
                                    QualityCheckResult qualityCheckResult = new QualityCheckResult();
                                    ToolUtil.copyProperties(qualityCheck,qualityCheckResult);
                                    qualityPlanDetailResult.setQualityCheckResult(qualityCheckResult);
                                }
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
