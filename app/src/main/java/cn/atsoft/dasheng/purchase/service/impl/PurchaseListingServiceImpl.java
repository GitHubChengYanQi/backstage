package cn.atsoft.dasheng.purchase.service.impl;


import cn.atsoft.dasheng.app.entity.StockDetails;
import cn.atsoft.dasheng.app.model.result.BrandResult;
import cn.atsoft.dasheng.app.service.BrandService;
import cn.atsoft.dasheng.app.service.StockDetailsService;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.model.result.SkuResult;
import cn.atsoft.dasheng.erp.service.SkuService;
import cn.atsoft.dasheng.purchase.entity.PurchaseAsk;
import cn.atsoft.dasheng.purchase.entity.PurchaseListing;
import cn.atsoft.dasheng.purchase.mapper.PurchaseListingMapper;
import cn.atsoft.dasheng.purchase.model.params.PurchaseListingParam;
import cn.atsoft.dasheng.purchase.model.result.PurchaseListingResult;
import cn.atsoft.dasheng.purchase.pojo.ListingPlan;
import cn.atsoft.dasheng.purchase.pojo.PlanListParam;
import cn.atsoft.dasheng.purchase.service.PurchaseAskService;
import cn.atsoft.dasheng.purchase.service.PurchaseListingService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
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
    @Autowired
    private UserService userService;
    @Autowired
    private BrandService brandService;
    @Autowired
    private StockDetailsService stockDetailsService;

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

    /**
     * 预购
     *
     * @param param
     * @return
     */
    @Override
    public PageInfo<PurchaseListingResult> readyBuy(PurchaseListingParam param) {
        Page<PurchaseListingResult> pageContext = getPageContext();
        IPage<PurchaseListingResult> page = this.baseMapper.readyBuy(pageContext, param);
        format(page.getRecords());


        List<PurchaseListing> listings = this.query().eq("status", 0).eq("display", 1).list();
        List<PurchaseListingResult> results = BeanUtil.copyToList(listings, PurchaseListingResult.class, new CopyOptions());
        format(results);


        for (PurchaseListingResult record : page.getRecords()) {
            getChild(record, results);
        }
        return PageFactory.createPageInfo(page);
    }


    private void getChild(PurchaseListingResult result, List<PurchaseListingResult> results) {

        List<PurchaseListingResult> resultList = new ArrayList<>();

        for (PurchaseListingResult purchaseListingResult : results) {
            if (purchaseListingResult.getSkuId().equals(result.getSkuId())) {
                resultList.add(purchaseListingResult);
            }
        }
        result.setChildren(resultList);
    }


    @Override
    public Set<ListingPlan> plans(PlanListParam param) {
        QueryWrapper<PurchaseAsk> askQueryWrapper = new QueryWrapper<>();
        if (ToolUtil.isNotEmpty(param.getCoding())) {
            askQueryWrapper.eq("coding", param.getCoding());
        }
        if (ToolUtil.isNotEmpty(param.getType())) {
            askQueryWrapper.eq("type", param.getType());
        }

        askQueryWrapper.eq("status", 2);
        List<PurchaseAsk> asks = askService.list(askQueryWrapper);
        List<Long> askIds = new ArrayList<>();
        for (PurchaseAsk ask : asks) {
            askIds.add(ask.getPurchaseAskId());
        }
        if (ToolUtil.isEmpty(askIds)) {
            return null;
        }
        //查询所有申请通过的物料
        QueryWrapper<PurchaseListing> listingQueryWrapper = new QueryWrapper<>();
        listingQueryWrapper.in("purchase_ask_id", askIds);
        listingQueryWrapper.eq("status", 0);

        if (ToolUtil.isNotEmpty(param.getEndTime()) && ToolUtil.isNotEmpty(param.getBeginTime())) {  //日期查询
            listingQueryWrapper.between("delivery_date", param.getBeginTime(), param.getEndTime());
        }
        if (ToolUtil.isNotEmpty(param.getStockNumber())) {          //小于库存数量
            List<StockDetails> details = stockDetailsService.query().lt("number", param.getStockNumber()).list();
            listingQueryWrapper.in("sku_id", new ArrayList<Long>() {{
                for (StockDetails detail : details) {
                    add(detail.getSkuId());
                }
            }});
        }
        if (ToolUtil.isNotEmpty(param.getSkuId())) {
            listingQueryWrapper.eq("sku_id", param.getSkuId());
        }
        List<PurchaseListing> purchaseListingList = this.list(listingQueryWrapper);

        Set<ListingPlan> listingPlanSet = new HashSet<>();
        for (PurchaseListing purchaseListing : purchaseListingList) {
            ListingPlan plan = new ListingPlan();
            plan.setSkuId(purchaseListing.getSkuId());
            plan.setBrandId(purchaseListing.getBrandId());
            listingPlanSet.add(plan);
        }

        List<PurchaseListingResult> results = new ArrayList<>();
        for (PurchaseListing purchaseListing : purchaseListingList) {
            PurchaseListingResult listingResult = new PurchaseListingResult();
            ToolUtil.copyProperties(purchaseListing, listingResult);
            results.add(listingResult);
        }
        format(results);

        for (ListingPlan plan : listingPlanSet) {

            List<PurchaseListingResult> resultList = new ArrayList<>();
            long number = 0L;
            for (PurchaseListingResult result : results) {
                if (ToolUtil.isNotEmpty(result.getBrandId()) && ToolUtil.isNotEmpty(result.getSkuId())
                        && plan.getSkuId().equals(result.getSkuId()) && plan.getBrandId().equals(result.getBrandId())) {
                    number = number + result.getApplyNumber();
                    resultList.add(result);
                    plan.setSkuResult(result.getSkuResult());
                    plan.setBrandResult(result.getBrandResult());
                }
            }

            plan.setChildren(resultList);
            plan.setApplyNumber(number);
        }
        return listingPlanSet;
    }

    @Override
    public void format(List<PurchaseListingResult> param) {
        List<Long> skuIds = new ArrayList<>();
        List<Long> userIds = new ArrayList<>();
        List<Long> brandIds = new ArrayList<>();
        for (PurchaseListingResult result : param) {
            skuIds.add(result.getSkuId());
            userIds.add(result.getCreateUser());
            brandIds.add(result.getBrandId());
        }
        List<SkuResult> skuResults = skuService.formatSkuResult(skuIds);
        List<User> userList = userIds.size() == 0 ? new ArrayList<>() : userService.listByIds(userIds);
        List<BrandResult> brandResults = brandService.getBrandResults(brandIds);


        for (PurchaseListingResult result : param) {
            for (SkuResult skuResult : skuResults) {
                if (ToolUtil.isNotEmpty(result.getSkuId()) && result.getSkuId().equals(skuResult.getSkuId())) {
                    result.setSkuResult(skuResult);
                    break;
                }
            }

            for (User user : userList) {
                if (user.getUserId().equals(result.getCreateUser())) {
                    result.setUser(user);
                    break;
                }
            }

            for (BrandResult brandResult : brandResults) {
                if (ToolUtil.isNotEmpty(result.getBrandId()) && brandResult.getBrandId().equals(result.getBrandId())) {
                    result.setBrandResult(brandResult);
                    break;
                }
            }
        }

    }
}
