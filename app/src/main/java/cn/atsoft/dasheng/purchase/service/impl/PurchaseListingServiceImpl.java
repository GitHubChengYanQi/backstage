package cn.atsoft.dasheng.purchase.service.impl;


import cn.atsoft.dasheng.app.model.result.BrandResult;
import cn.atsoft.dasheng.app.model.result.SkuRequest;
import cn.atsoft.dasheng.app.service.BrandService;
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
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.*;
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
    public Set<ListingPlan> plans() {
        List<PurchaseAsk> asks = askService.query().eq("status", 2).list();
        List<Long> askIds = new ArrayList<>();
        for (PurchaseAsk ask : asks) {
            askIds.add(ask.getPurchaseAskId());
        }
        //查询所有申请通过的物料
        List<PurchaseListing> purchaseListingList = askIds.size() == 0 ? new ArrayList<>() : this.query().in("purchase_ask_id", askIds).eq("status", 0).list();


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
            Long number = 0L;
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
                if (result.getSkuId().equals(skuResult.getSkuId())) {
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
                if (brandResult.getBrandId().equals(result.getBrandId())) {
                    result.setBrandResult(brandResult);
                    break;
                }
            }
        }

    }
}
