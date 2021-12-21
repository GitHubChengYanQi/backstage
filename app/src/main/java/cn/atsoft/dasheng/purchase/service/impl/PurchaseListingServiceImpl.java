package cn.atsoft.dasheng.purchase.service.impl;


import cn.atsoft.dasheng.app.model.result.SkuRequest;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.Sku;
import cn.atsoft.dasheng.erp.model.result.SkuResult;
import cn.atsoft.dasheng.erp.service.SkuService;
import cn.atsoft.dasheng.purchase.entity.PurchaseAsk;
import cn.atsoft.dasheng.purchase.entity.PurchaseListing;
import cn.atsoft.dasheng.purchase.mapper.PurchaseListingMapper;
import cn.atsoft.dasheng.purchase.model.params.PurchaseListingParam;
import cn.atsoft.dasheng.purchase.model.result.PurchaseListingResult;
import cn.atsoft.dasheng.purchase.pojo.ListingPlan;
import cn.atsoft.dasheng.purchase.service.PurchaseAskService;
import cn.atsoft.dasheng.purchase.service.PurchaseListingService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.*;

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
    @Autowired
    private PurchaseAskService askService;


    @Override
    public void add(PurchaseListingParam param) {
        PurchaseListing entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(PurchaseListingParam param) {
        this.removeById(getKey(param));
    }

    @Override
    public void update(PurchaseListingParam param) {
        PurchaseListing oldEntity = getOldEntity(param);
        PurchaseListing newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public PurchaseListingResult findBySpec(PurchaseListingParam param) {
        return null;
    }

    @Override
    public List<PurchaseListingResult> findListBySpec(PurchaseListingParam param) {
        return null;
    }

    @Override
    public PageInfo<PurchaseListingResult> findPageBySpec(PurchaseListingParam param) {
        Page<PurchaseListingResult> pageContext = getPageContext();
        IPage<PurchaseListingResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(PurchaseListingParam param) {
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
    public List<PurchaseListingResult> getByAskId(Long askId) {
        List<PurchaseListing> purchaseListingList = this.list(new QueryWrapper<PurchaseListing>() {{
            eq("purchase_ask_id", askId);
        }});
        List<PurchaseListingResult> purchaseListingResults = new ArrayList<>();
        for (PurchaseListing purchaseListing : purchaseListingList) {
            PurchaseListingResult result = new PurchaseListingResult();
            ToolUtil.copyProperties(purchaseListing, result);
            purchaseListingResults.add(result);
        }
        this.format(purchaseListingResults);
        return purchaseListingResults;
    }

    @Override
    public List<ListingPlan> plans() {
        List<ListingPlan> plans = new ArrayList<>();
        List<PurchaseAsk> asks = askService.query().eq("status", 2).list();
        List<Long> askIds = new ArrayList<>();
        for (PurchaseAsk ask : asks) {
            askIds.add(ask.getPurchaseAskId());
        }
        //查询所有申请通过的物料
        List<PurchaseListing> purchaseListingList = askIds.size() == 0 ? new ArrayList<>() : this.query().in("purchase_ask_id", askIds).eq("status", 0).list();
        HashSet<Long> skuSet = new HashSet<>();

        List<PurchaseListingResult> results = new ArrayList<>();
        for (PurchaseListing purchaseListing : purchaseListingList) {
            PurchaseListingResult listingResult = new PurchaseListingResult();
            ToolUtil.copyProperties(purchaseListing, listingResult);
            skuSet.add(listingResult.getSkuId());
            results.add(listingResult);
        }
        //过滤相同sku 重新组合
        for (Long aLong : skuSet) {
            List<PurchaseListingResult> newListing = new ArrayList<>();
            ListingPlan plan = new ListingPlan();
            plan.setSkuId(aLong);
            for (PurchaseListingResult result : results) {
                if (result.getSkuId().equals(aLong)) {
                    newListing.add(result);
                }
            }
            plan.setListingResults(newListing);
            plans.add(plan);
        }
        //计算总数
        for (ListingPlan plan : plans) {
            Long number = 0L;
            for (PurchaseListingResult listingResult : plan.getListingResults()) {
                number = number + listingResult.getApplyNumber();
            }
            plan.setNumber(number);
        }

        planFormat(plans);
        return plans;
    }

    public void format(List<PurchaseListingResult> param) {
        List<Long> skuIds = new ArrayList<>();
        for (PurchaseListingResult result : param) {
            skuIds.add(result.getSkuId());
        }
        List<Sku> skuList = skuIds.size() == 0 ? new ArrayList<>() : skuService.listByIds(skuIds);
        List<SkuResult> skuResults = new ArrayList<>();
        for (Sku sku : skuList) {
            SkuResult skuResult = new SkuResult();
            ToolUtil.copyProperties(sku, skuResult);
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

    private void planFormat(List<ListingPlan> data) {
        for (ListingPlan datum : data) {
            SkuResult sku = skuService.getSku(datum.getSkuId());
            datum.setSkuResult(sku);
            format(datum.getListingResults());
        }
    }

}
