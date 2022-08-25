package cn.atsoft.dasheng.production.service.impl;


import cn.atsoft.dasheng.app.entity.StockDetails;
import cn.atsoft.dasheng.app.model.result.BrandResult;
import cn.atsoft.dasheng.app.service.BrandService;
import cn.atsoft.dasheng.app.service.CustomerService;
import cn.atsoft.dasheng.app.service.StockDetailsService;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.model.result.SkuSimpleResult;
import cn.atsoft.dasheng.erp.model.result.StorehousePositionsResult;
import cn.atsoft.dasheng.erp.service.SkuService;
import cn.atsoft.dasheng.erp.service.StorehousePositionsService;
import cn.atsoft.dasheng.orCode.model.result.StoreHousePositionsRequest;
import cn.atsoft.dasheng.production.entity.ProductionPickListsCart;
import cn.atsoft.dasheng.production.entity.ProductionPickListsDetail;
import cn.atsoft.dasheng.production.mapper.ProductionPickListsDetailMapper;
import cn.atsoft.dasheng.production.model.params.ProductionPickListsDetailParam;
import cn.atsoft.dasheng.production.model.result.ProductionPickListsCartResult;
import cn.atsoft.dasheng.production.model.result.ProductionPickListsDetailResult;
import cn.atsoft.dasheng.production.pojo.LockedStockDetails;
import cn.atsoft.dasheng.production.service.ProductionPickListsCartService;
import cn.atsoft.dasheng.production.service.ProductionPickListsDetailService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 领料单详情表 服务实现类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-03-25
 */
@Service
public class ProductionPickListsDetailServiceImpl extends ServiceImpl<ProductionPickListsDetailMapper, ProductionPickListsDetail> implements ProductionPickListsDetailService {
    @Autowired
    private StockDetailsService stockDetailsService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private BrandService brandService;
    @Autowired
    private ProductionPickListsCartService pickListsCartService;

    @Autowired
    private SkuService skuService;

    @Autowired
    private StorehousePositionsService storehousePositionsService;

    @Override
    public void add(ProductionPickListsDetailParam param) {
        ProductionPickListsDetail entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(ProductionPickListsDetailParam param) {
        this.removeById(getKey(param));
    }

    @Override
    public void update(ProductionPickListsDetailParam param) {
        ProductionPickListsDetail oldEntity = getOldEntity(param);
        ProductionPickListsDetail newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public ProductionPickListsDetailResult findBySpec(ProductionPickListsDetailParam param) {
        return null;
    }

    @Override
    public List<ProductionPickListsDetailResult> findListBySpec(ProductionPickListsDetailParam param) {
        List<ProductionPickListsDetailResult> productionPickListsDetailResults = this.baseMapper.customList(param);
        this.format(productionPickListsDetailResults);
        return productionPickListsDetailResults;

    }

    @Override
    public List<ProductionPickListsDetailResult> listStatus0ByPickLists(Long pickListsId) {
        List<ProductionPickListsDetail> pickListsDetails = this.query().eq("pick_lists_id", pickListsId).eq("status", 0).eq("display", 1).list();
        List<ProductionPickListsDetailResult> results = BeanUtil.copyToList(pickListsDetails, ProductionPickListsDetailResult.class);
        this.format(results);
        return results;

    }
    @Override
    public List<ProductionPickListsDetailResult> listStatus0ByPickLists(List<Long> pickListsIds) {
        List<ProductionPickListsDetail> pickListsDetails =pickListsIds.size() == 0 ? new ArrayList<>() : this.query().in("pick_lists_id", pickListsIds).eq("status", 0).eq("display", 1).list();
        List<ProductionPickListsDetailResult> results = BeanUtil.copyToList(pickListsDetails, ProductionPickListsDetailResult.class);
        this.format(results);
        return results;

    }
    @Override
    public List<ProductionPickListsDetailResult> listByPickLists(List<Long> pickListsIds) {
        List<ProductionPickListsDetail> pickListsDetails =pickListsIds.size() == 0 ? new ArrayList<>() : this.query().in("pick_lists_id", pickListsIds).eq("display", 1).list();
        List<ProductionPickListsDetailResult> results = BeanUtil.copyToList(pickListsDetails, ProductionPickListsDetailResult.class);
        this.format(results);
        return results;

    }

    @Override
    public List<ProductionPickListsDetailResult> resultsByPickListsIds(List<Long> listsIds) {
        if (ToolUtil.isEmpty(listsIds) || listsIds.size() == 0) {
            return new ArrayList<>();
        }
        List<ProductionPickListsDetailResult> pickListsDetailResults = BeanUtil.copyToList(this.query().in("pick_lists_id", listsIds).eq("display", 1).list(), ProductionPickListsDetailResult.class);
        this.format(pickListsDetailResults);

        return pickListsDetailResults;
    }

    @Override
    public void format(List<ProductionPickListsDetailResult> results) {
        List<Long> skuIds = new ArrayList<>();
        List<Long> brandIds = new ArrayList<>();
        List<Long> detailIds = new ArrayList<>();
        for (ProductionPickListsDetailResult result : results) {
            skuIds.add(result.getSkuId());
            if (ToolUtil.isNotEmpty(result.getBrandId())) {
                brandIds.add(result.getBrandId());
            }
            detailIds.add(result.getPickListsDetailId());
        }
        List<Long> lockedInkindIds = this.getLockedInkindIds();
        List<SkuSimpleResult> skuSimpleResults = skuService.simpleFormatSkuResult(skuIds);
        /**
         * 获取相关被锁定实物数量
         */
        List<LockedStockDetails> lockSkuAndNumber = pickListsCartService.getLockSkuAndNumber(skuIds);

        //TODO notin
        List<StockDetails> stockSkus = skuIds.size() == 0 ? new ArrayList<>() : lockedInkindIds.size() == 0 ? stockDetailsService.query().in("sku_id", skuIds).eq("display", 1).list() : stockDetailsService.query().in("sku_id", skuIds).notIn("inkind_id",lockedInkindIds).eq("display", 1).list();
        List<ProductionPickListsCartResult> cartResults = pickListsCartService.listByListsDetailIds(detailIds);
        for (StockDetails skus : stockSkus) {
            if (ToolUtil.isEmpty(skus.getBrandId())) {
                skus.setBrandId(0L);
            }
        }
        List<Long> positionIds= new ArrayList<>();
        List<StockDetails> totalList = new ArrayList<>();
        stockSkus.parallelStream().collect(Collectors.groupingBy(item -> item.getSkuId() + '_' + (ToolUtil.isEmpty(item.getBrandId()) ? 0L : item.getBrandId()), Collectors.toList())).forEach(
                (id, transfer) -> {
                    transfer.stream().reduce((a, b) -> new StockDetails() {{
                        setSkuId(a.getSkuId());
                        setNumber(a.getNumber() + b.getNumber());
                        setBrandId(ToolUtil.isEmpty(a.getBrandId()) ? 0L : a.getBrandId());
                        setStorehousePositionsId(a.getStorehousePositionsId());
                    }}).ifPresent(totalList::add);
                }
        );
        for (StockDetails stockDetails : stockSkus) {
            positionIds.add(stockDetails.getStorehousePositionsId());
        }
        positionIds = positionIds.stream().distinct().collect(Collectors.toList());
        List<StorehousePositionsResult> positionsResultList = storehousePositionsService.resultsByIds(positionIds);
        List<StockDetails> anyBrand = new ArrayList<>();
        stockSkus.parallelStream().collect(Collectors.groupingBy(item -> item.getSkuId() + "", Collectors.toList())).forEach(
                (id, transfer) -> {
                    transfer.stream().reduce((a, b) -> new StockDetails() {{
                        setSkuId(a.getSkuId());
                        setNumber(a.getNumber() + b.getNumber());
                        setStorehousePositionsId(a.getStorehousePositionsId());
                    }}).ifPresent(anyBrand::add);
                }
        );
        List<StockDetails> positionTotal = new ArrayList<>();
        stockSkus.parallelStream().collect(Collectors.groupingBy(item -> item.getSkuId() + "_"+item.getBrandId()+"_"+item.getStorehousePositionsId(), Collectors.toList())).forEach(
                (id, transfer) -> {
                    transfer.stream().reduce((a, b) -> new StockDetails() {{
                        setSkuId(a.getSkuId());
                        setNumber(a.getNumber() + b.getNumber());
                        setStorehousePositionsId(a.getStorehousePositionsId());
                    }}).ifPresent(positionTotal::add);
                }
        );

        List<BrandResult> brandResults = brandIds.size() == 0 ? new ArrayList<>() : brandService.getBrandResults(brandIds);
        for (ProductionPickListsDetailResult result : results) {
            List<String> positionNames = new ArrayList<>();
            List<Long> positionIdList = new ArrayList<>();
            result.setIsMeet(false);
            if (!result.getBrandId().equals(0L)) {
                for (StockDetails stockSkuTotal : totalList) {
                    if (result.getSkuId().equals(stockSkuTotal.getSkuId()) && result.getBrandId().equals(stockSkuTotal.getBrandId())) {
                        result.setStockNumber(Math.toIntExact(stockSkuTotal.getNumber()));
                        for (StorehousePositionsResult positionsResult : positionsResultList) {
                            if (stockSkuTotal.getStorehousePositionsId().equals(positionsResult.getStorehousePositionsId())){
                                positionNames.add(positionsResult.getName());
                            }
                        }
                        if (result.getNumber() <= stockSkuTotal.getNumber()) {
                            result.setIsMeet(true);
                        }
                    }
                }
                for (StockDetails stockDetails : positionTotal) {
                    for (StorehousePositionsResult positionsResult : positionsResultList) {
                        if (result.getSkuId().equals(stockDetails.getSkuId()) && result.getBrandId().equals(stockDetails.getBrandId()) && stockDetails.getStorehousePositionsId().equals(positionsResult.getStorehousePositionsId())){
                            positionNames.add(positionsResult.getName());
                        }
                    }
                }
                for (LockedStockDetails lockedStockDetails : lockSkuAndNumber) {
                    if (result.getSkuId().equals(lockedStockDetails.getSkuId()) && result.getBrandId().equals(lockedStockDetails.getBrandId())){
                        result.setLockStockDetailNumber(lockedStockDetails.getNumber());
                    }
                }
            } else {
                for (StockDetails stockDetails : anyBrand) {
                    if (result.getSkuId().equals(stockDetails.getSkuId())) {
                        result.setStockNumber(Math.toIntExact(stockDetails.getNumber()));
                        if (result.getNumber() <= stockDetails.getNumber()) {
                            result.setIsMeet(true);
                        }

                    }
                }
                for (StockDetails stockDetails : positionTotal) {
                    for (StorehousePositionsResult positionsResult : positionsResultList) {
                        if (result.getSkuId().equals(stockDetails.getSkuId()) && stockDetails.getStorehousePositionsId().equals(positionsResult.getStorehousePositionsId())){
                            positionNames.add(positionsResult.getName());
                            positionIdList.add(positionsResult.getStorehousePositionsId());
                        }
                    }
                }
                int lockedNumber = 0 ;
                for (LockedStockDetails lockedStockDetails : lockSkuAndNumber) {
                    if (result.getSkuId().equals(lockedStockDetails.getSkuId())){
                        lockedNumber+=lockedStockDetails.getNumber();
                    }
                }
                result.setLockStockDetailNumber(lockedNumber);
            }
            //返回可备料仓库名称
            positionNames = positionNames.stream().distinct().collect(Collectors.toList());
            result.setPositionNames(positionNames);
            positionIdList = positionIdList.stream().distinct().collect(Collectors.toList());
            result.setPositionIds(positionIdList);

            for (SkuSimpleResult skuSimpleResult : skuSimpleResults) {
                if (result.getSkuId().equals(skuSimpleResult.getSkuId())) {
                    result.setSkuResult(skuSimpleResult);
                    break;
                }
            }
            for (BrandResult brandResult : brandResults) {
                if (ToolUtil.isNotEmpty(result.getBrandId()) && result.getBrandId().equals(brandResult.getBrandId())) {
                    Map<String, String> brandMap = new HashMap<>();
                    brandMap.put("brandId", brandResult.getBrandId().toString());
                    brandMap.put("brandName", brandResult.getBrandName());
                    result.setBrandResult(brandMap);
                }
            }
            List<ProductionPickListsCartResult> cartResultList = new ArrayList<>();
            for (ProductionPickListsCartResult cartResult : cartResults) {
                if (result.getPickListsDetailId().equals(cartResult.getPickListsDetailId())) {
                    cartResultList.add(cartResult);
                }
            }
            if (cartResultList.stream().anyMatch(i->i.getStatus().equals(0))) {
                result.setCanPick(true);
            }else {
                result.setCanPick(false);

            }
            result.setCartResults(cartResultList);
        }

    }

    public List<Long> getLockedInkindIds() {
        List<ProductionPickListsCart> list = pickListsCartService.query().eq("status", 0).eq("display", 1).list();
        List<Long> inkindIds = new ArrayList<>();
        for (ProductionPickListsCart pickListsCart : list) {
            inkindIds.add(pickListsCart.getInkindId());
        }
        return inkindIds;
    }

    @Override
    public List<ProductionPickListsDetailResult> getByTask(ProductionPickListsDetailParam param) {
        return this.baseMapper.customList(param);
    }

    @Override
    public PageInfo<ProductionPickListsDetailResult> findPageBySpec(ProductionPickListsDetailParam param) {
        Page<ProductionPickListsDetailResult> pageContext = getPageContext();
        IPage<ProductionPickListsDetailResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(ProductionPickListsDetailParam param) {
        return param.getPickListsDetailId();
    }

    private Page<ProductionPickListsDetailResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private ProductionPickListsDetail getOldEntity(ProductionPickListsDetailParam param) {
        return this.getById(getKey(param));
    }

    private ProductionPickListsDetail getEntity(ProductionPickListsDetailParam param) {
        ProductionPickListsDetail entity = new ProductionPickListsDetail();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
