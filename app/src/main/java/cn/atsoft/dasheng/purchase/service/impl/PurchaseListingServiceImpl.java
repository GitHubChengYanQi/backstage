package cn.atsoft.dasheng.purchase.service.impl;


import cn.atsoft.dasheng.app.model.result.SkuRequest;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.Sku;
import cn.atsoft.dasheng.erp.model.result.SkuResult;
import cn.atsoft.dasheng.erp.service.SkuService;
import cn.atsoft.dasheng.purchase.entity.PurchaseListing;
import cn.atsoft.dasheng.purchase.mapper.PurchaseListingMapper;
import cn.atsoft.dasheng.purchase.model.params.PurchaseListingParam;
import cn.atsoft.dasheng.purchase.model.result.PurchaseListingResult;
import  cn.atsoft.dasheng.purchase.service.PurchaseListingService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
 * 采购清单 服务实现类
 * </p>
 *
 * @author song
 * @since 2021-12-15
 */
@Service
public class PurchaseListingServiceImpl extends ServiceImpl<PurchaseListingMapper, PurchaseListing> implements PurchaseListingService {
    @Autowired
    private SkuService skuService;
    @Override
    public void add(PurchaseListingParam param){
        PurchaseListing entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(PurchaseListingParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(PurchaseListingParam param){
        PurchaseListing oldEntity = getOldEntity(param);
        PurchaseListing newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public PurchaseListingResult findBySpec(PurchaseListingParam param){
        return null;
    }

    @Override
    public List<PurchaseListingResult> findListBySpec(PurchaseListingParam param){
        return null;
    }

    @Override
    public PageInfo<PurchaseListingResult> findPageBySpec(PurchaseListingParam param){
        Page<PurchaseListingResult> pageContext = getPageContext();
        IPage<PurchaseListingResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(PurchaseListingParam param){
        return param.getPurchaseListingId();
    }

    private Page<PurchaseListingResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private PurchaseListing getOldEntity(PurchaseListingParam param) {
        return this.getById(getKey(param));
    }

    private PurchaseListing getEntity(PurchaseListingParam param) {
        PurchaseListing entity = new PurchaseListing();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }
    @Override
    public List<PurchaseListingResult>  getByAskId(Long askId){
        List<PurchaseListing> purchaseListingList = this.list(new QueryWrapper<PurchaseListing>() {{
            eq("purchase_ask_id", askId);
        }});
        List<PurchaseListingResult> purchaseListingResults = new ArrayList<>();
        for (PurchaseListing purchaseListing : purchaseListingList) {
            PurchaseListingResult result = new PurchaseListingResult();
            ToolUtil.copyProperties(purchaseListing,result);
            purchaseListingResults.add(result);
        }
        this.format(purchaseListingResults);
        return purchaseListingResults;
    }

    public void format(List<PurchaseListingResult> param){
        List<Long> skuIds = new ArrayList<>();
        for (PurchaseListingResult result : param) {
            skuIds.add(result.getSkuId());
        }
        List<Sku> skuList = skuService.listByIds(skuIds);
        List<SkuResult> skuResults = new ArrayList<>();
        for (Sku sku : skuList) {
            SkuResult skuResult = new SkuResult();
            ToolUtil.copyProperties(sku,skuResult);
            skuResults.add(skuResult);
        }
        skuService.format(skuResults);
        for (PurchaseListingResult result : param) {
            for (SkuResult skuResult : skuResults) {
                if (result.getSkuId().equals(skuResult.getSkuId())) {
                    result.setSkuResult(skuResult);
                }
            }
        }

    }

}
