package cn.atsoft.dasheng.production.service.impl;


import cn.atsoft.dasheng.app.entity.StockDetails;
import cn.atsoft.dasheng.app.service.StockDetailsService;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.model.result.SkuResult;
import cn.atsoft.dasheng.erp.service.SkuService;
import cn.atsoft.dasheng.production.entity.ProductionPlanDetail;
import cn.atsoft.dasheng.production.mapper.ProductionPlanDetailMapper;
import cn.atsoft.dasheng.production.model.params.ProductionPlanDetailParam;
import cn.atsoft.dasheng.production.model.result.ProductionPlanDetailResult;
import cn.atsoft.dasheng.production.service.ProductionPlanDetailService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 生产计划子表 服务实现类
 * </p>
 *
 * @author
 * @since 2022-02-25
 */
@Service
public class ProductionPlanDetailServiceImpl extends ServiceImpl<ProductionPlanDetailMapper, ProductionPlanDetail> implements ProductionPlanDetailService {
    @Autowired
    private SkuService skuService;
    @Autowired
    private StockDetailsService stockDetailsService;


    @Override
    public void add(ProductionPlanDetailParam param) {
        ProductionPlanDetail entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(ProductionPlanDetailParam param) {
        this.removeById(getKey(param));
    }

    @Override
    public void update(ProductionPlanDetailParam param) {
        ProductionPlanDetail oldEntity = getOldEntity(param);
        ProductionPlanDetail newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public ProductionPlanDetailResult findBySpec(ProductionPlanDetailParam param) {
        return null;
    }

    @Override
    public List<ProductionPlanDetailResult> findListBySpec(ProductionPlanDetailParam param) {
        return null;
    }

    @Override
    public PageInfo<ProductionPlanDetailResult> findPageBySpec(ProductionPlanDetailParam param) {
        Page<ProductionPlanDetailResult> pageContext = getPageContext();
        IPage<ProductionPlanDetailResult> page = this.baseMapper.customPageList(pageContext, param);
        format(page.getRecords());
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(ProductionPlanDetailParam param) {
        return param.getProductionPlanDetailId();
    }

    private Page<ProductionPlanDetailResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private ProductionPlanDetail getOldEntity(ProductionPlanDetailParam param) {
        return this.getById(getKey(param));
    }

    private ProductionPlanDetail getEntity(ProductionPlanDetailParam param) {
        ProductionPlanDetail entity = new ProductionPlanDetail();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

    private void format(List<ProductionPlanDetailResult> data) {

        List<Long> skuIds = new ArrayList<>();
        for (ProductionPlanDetailResult datum : data) {
            skuIds.add(datum.getSkuId());
        }
        List<SkuResult> skuResults = skuService.formatSkuResult(skuIds);
        List<StockDetails> details = stockDetailsService.query().in("sku_id", skuIds).list();

        for (ProductionPlanDetailResult datum : data) {
            for (SkuResult skuResult : skuResults) {
                if (datum.getSkuId().equals(skuResult.getSkuId())) {
                    datum.setSkuResult(skuResult);
                    break;
                }
            }
            long stockNumber = 0;
            for (StockDetails detail : details) {
                if (detail.getSkuId().equals(datum.getSkuId())) {
                    stockNumber = stockNumber + detail.getNumber();
                }
            }
            datum.setStockNumber(stockNumber);
        }

    }

}
