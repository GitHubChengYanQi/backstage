package cn.atsoft.dasheng.outStock.service.impl;


//import cn.atsoft.dasheng.app.entity.StockDetails;
//import cn.atsoft.dasheng.app.model.request.StockView;
//import cn.atsoft.dasheng.app.model.result.BrandResult;
//import cn.atsoft.dasheng.app.model.result.StorehouseSimpleResult;
//import cn.atsoft.dasheng.app.service.BrandService;
//import cn.atsoft.dasheng.app.service.CustomerService;
//import cn.atsoft.dasheng.app.service.StockDetailsService;
//import cn.atsoft.dasheng.app.service.StorehouseService;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.util.ToolUtil;
//import cn.atsoft.dasheng.erp.model.params.DataStatisticsViewParam;
//import cn.atsoft.dasheng.erp.model.result.SkuListResult;
//import cn.atsoft.dasheng.erp.model.result.StorehousePositionsResult;
//import cn.atsoft.dasheng.erp.service.SkuListService;
//import cn.atsoft.dasheng.erp.service.SkuService;
//import cn.atsoft.dasheng.erp.service.StorehousePositionsService;
//import cn.atsoft.dasheng.production.entity.RestOutStockCart;
//import cn.atsoft.dasheng.production.entity.RestOutStockOrderDetail;
//import cn.atsoft.dasheng.production.mapper.RestOutStockOrderDetailMapper;
//import cn.atsoft.dasheng.production.model.params.RestOutStockOrderDetailParam;
//import cn.atsoft.dasheng.production.model.result.RestOutStockCartResult;
//import cn.atsoft.dasheng.production.model.result.RestOutStockCartSimpleResult;
//import cn.atsoft.dasheng.production.model.result.RestOutStockOrderDetailResult;
//import cn.atsoft.dasheng.production.pojo.LockedStockDetails;
//import cn.atsoft.dasheng.production.service.RestOutStockCartService;
//import cn.atsoft.dasheng.production.service.RestOutStockOrderDetailService;
import cn.atsoft.dasheng.goods.sku.service.RestSkuService;
import cn.atsoft.dasheng.outStock.entity.RestOutStockCart;
import cn.atsoft.dasheng.outStock.entity.RestOutStockOrderDetail;
import cn.atsoft.dasheng.outStock.mapper.RestOutStockOrderDetailMapper;
import cn.atsoft.dasheng.outStock.model.params.RestOutStockOrderDetailParam;
import cn.atsoft.dasheng.outStock.model.result.RestOutStockOrderDetailResult;
import cn.atsoft.dasheng.outStock.service.RestOutStockCartService;
import cn.atsoft.dasheng.outStock.service.RestOutStockOrderDetailService;
import cn.atsoft.dasheng.stockDetail.entity.RestStockDetails;
import cn.atsoft.dasheng.stockDetail.service.RestStockDetailsService;
import cn.atsoft.dasheng.storehouse.service.RestStorehouseService;
import cn.atsoft.dasheng.storehousePosition.service.RestStorehousePositionsService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
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
public class RestOutStockOrderDetailServiceImpl extends ServiceImpl<RestOutStockOrderDetailMapper, RestOutStockOrderDetail> implements RestOutStockOrderDetailService {
    @Autowired
    private RestStockDetailsService stockDetailsService;
//    @Autowired
//    private CustomerService customerService;
//    @Autowired
//    private BrandService brandService;
    @Autowired
    private RestOutStockCartService pickListsCartService;

    @Autowired
    private RestSkuService skuService;

    @Autowired
    private RestStorehousePositionsService storehousePositionsService;
//    @Autowired
//    private SkuListService skuListService;
    @Autowired
    private RestStorehouseService storehouseService;

    @Override
    public void add(RestOutStockOrderDetailParam param) {
        RestOutStockOrderDetail entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(RestOutStockOrderDetailParam param) {
        this.removeById(getKey(param));
    }

    @Override
    public void update(RestOutStockOrderDetailParam param) {
        RestOutStockOrderDetail oldEntity = getOldEntity(param);
        RestOutStockOrderDetail newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public RestOutStockOrderDetailResult findBySpec(RestOutStockOrderDetailParam param) {
        return null;
    }

    @Override
    public List<RestOutStockOrderDetailResult> findListBySpec(RestOutStockOrderDetailParam param) {
        List<RestOutStockOrderDetailResult> productionPickListsDetailResults = this.baseMapper.customList(param);
//        this.format(productionPickListsDetailResults);
        return productionPickListsDetailResults;

    }

    @Override
    public List<RestOutStockOrderDetailResult> listStatus0ByPickLists(Long pickListsId) {
        List<RestOutStockOrderDetail> pickListsDetails = this.query().eq("pick_lists_id", pickListsId).eq("status", 0).eq("display", 1).list();
        List<RestOutStockOrderDetailResult> results = BeanUtil.copyToList(pickListsDetails, RestOutStockOrderDetailResult.class);
//        this.format(results);
        return results;

    }

    @Override
    public List<RestOutStockOrderDetailResult> listStatus0ByPickLists(List<Long> pickListsIds) {
        List<RestOutStockOrderDetail> pickListsDetails = pickListsIds.size() == 0 ? new ArrayList<>() : this.query().in("pick_lists_id", pickListsIds).eq("status", 0).eq("display", 1).list();
        List<RestOutStockOrderDetailResult> results = BeanUtil.copyToList(pickListsDetails, RestOutStockOrderDetailResult.class);
//        this.format(results);
        return results;

    }

    @Override
    public List<RestOutStockOrderDetail> listByPickLists(List<Long> pickListsIds) {
        List<RestOutStockOrderDetail> results = pickListsIds.size() == 0 ? new ArrayList<>() : this.query().in("pick_lists_id", pickListsIds).eq("display", 1).list();
//        List<RestOutStockOrderDetailResult> results = BeanUtil.copyToList(pickListsDetails, RestOutStockOrderDetailResult.class);
//        this.format(results);
        return results;

    }

    @Override
    public List<Long> getSkuIdsByPickLists(Long id) {
        return this.baseMapper.getSkuIdsByPickLists(id);
    }

    @Override
    public List<RestOutStockOrderDetailResult> resultsByPickListsIds(List<Long> listsIds) {
        if (ToolUtil.isEmpty(listsIds) || listsIds.size() == 0) {
            return new ArrayList<>();
        }
        List<RestOutStockOrderDetailResult> pickListsDetailResults = BeanUtil.copyToList(this.query().in("pick_lists_id", listsIds).eq("display", 1).list(), RestOutStockOrderDetailResult.class);
//        this.format(pickListsDetailResults);

        return pickListsDetailResults;
    }

//    @Override
//    public List<RestOutStockOrderDetailResult> pickListsTaskDetail(List<Long> pickListsIds) {
//        List<Long> lockedInkindIds = this.getLockedInkindIds();
//        List<RestOutStockOrderDetail> pickListsDetails = pickListsIds.size() == 0 ? new ArrayList<>() : this.query().in("pick_lists_id", pickListsIds).eq("display", 1).list();
//        List<RestOutStockOrderDetailResult> results = BeanUtil.copyToList(pickListsDetails, RestOutStockOrderDetailResult.class);
//        List<Long> detailIds = results.stream().map(RestOutStockOrderDetailResult::getPickListsDetailId).collect(Collectors.toList());
//        List<RestOutStockCart> carts = detailIds.size() == 0 ? new ArrayList<>() : pickListsCartService.query().in("pick_lists_detail_id", detailIds).ne("status", -1).list();
//        List<RestOutStockCartResult> cartResults = BeanUtil.copyToList(carts, RestOutStockCartResult.class);
//        List<Long> skuIds = pickListsDetails.stream().map(RestOutStockOrderDetail::getSkuId).distinct().collect(Collectors.toList());
//        List<LockedStockDetails> lockSkuAndNumber = pickListsCartService.getLockSkuAndNumber(skuIds);
//        List<StockDetails> stockSkus = skuIds.size() == 0 ? new ArrayList<>() : lockedInkindIds.size() == 0 ? stockDetailsService.query().in("sku_id", skuIds).eq("display", 1).list() : stockDetailsService.query().in("sku_id", skuIds).notIn("inkind_id", lockedInkindIds).eq("display", 1).list();
//
//        List<StockDetails> totalList = new ArrayList<>();
//        stockSkus.parallelStream().collect(Collectors.groupingBy(item -> item.getSkuId() + '_' + (ToolUtil.isEmpty(item.getBrandId()) ? 0L : item.getBrandId()), Collectors.toList())).forEach(
//                (id, transfer) -> {
//                    transfer.stream().reduce((a, b) -> new StockDetails() {{
//                        setSkuId(a.getSkuId());
//                        setNumber(a.getNumber() + b.getNumber());
//                        setBrandId(ToolUtil.isEmpty(a.getBrandId()) ? 0L : a.getBrandId());
//                        setStorehousePositionsId(a.getStorehousePositionsId());
//                    }}).ifPresent(totalList::add);
//                }
//        );
//        List<StockDetails> anyBrand = new ArrayList<>();
//        stockSkus.parallelStream().collect(Collectors.groupingBy(item -> item.getSkuId() + "", Collectors.toList())).forEach(
//                (id, transfer) -> {
//                    transfer.stream().reduce((a, b) -> new StockDetails() {{
//                        setSkuId(a.getSkuId());
//                        setNumber(a.getNumber() + b.getNumber());
//                        setStorehousePositionsId(a.getStorehousePositionsId());
//                    }}).ifPresent(anyBrand::add);
//                }
//        );
//        for (RestOutStockOrderDetailResult result : results) {
//            result.setNeedOperateNum(result.getNumber());
//
//            for (RestOutStockCart cart : carts) {
//                if (result.getPickListsDetailId().equals(cart.getPickListsDetailId())) {
//                    result.setNeedOperateNum(result.getNeedOperateNum() - cart.getNumber());
//                }
//            }
//            if (!result.getBrandId().equals(0L)) {
//                for (StockDetails stockSkuTotal : totalList) {
//                    if (result.getSkuId().equals(stockSkuTotal.getSkuId()) && result.getBrandId().equals(stockSkuTotal.getBrandId())) {
//                        result.setStockNumber(Math.toIntExact(stockSkuTotal.getNumber()));
//                    }
//                    if (result.getNumber() <= stockSkuTotal.getNumber()) {
//                        result.setIsMeet(true);
//                    }
//                }
//                for (LockedStockDetails lockedStockDetails : lockSkuAndNumber) {
//                    if (result.getSkuId().equals(lockedStockDetails.getSkuId()) && result.getBrandId().equals(lockedStockDetails.getBrandId())) {
//                        result.setLockStockDetailNumber(lockedStockDetails.getNumber());
//                    }
//                }
//            } else {
//                for (StockDetails stockDetails : anyBrand) {
//                    if (result.getSkuId().equals(stockDetails.getSkuId())) {
//                        result.setStockNumber(Math.toIntExact(stockDetails.getNumber()));
//                        if (result.getNumber() <= stockDetails.getNumber()) {
//                            result.setIsMeet(true);
//                        }
//
//                    }
//                }
//                int lockedNumber = 0;
//                for (LockedStockDetails lockedStockDetails : lockSkuAndNumber) {
//                    if (result.getSkuId().equals(lockedStockDetails.getSkuId())) {
//                        lockedNumber += lockedStockDetails.getNumber();
//                    }
//                }
//                result.setLockStockDetailNumber(lockedNumber);
//            }
//            List<RestOutStockCartResult> cartResultList = new ArrayList<>();
//            for (RestOutStockCartResult cartResult : cartResults) {
//                if (result.getPickListsDetailId().equals(cartResult.getPickListsDetailId())) {
//                    cartResultList.add(cartResult);
//                }
//            }
//            if (cartResultList.stream().anyMatch(i -> i.getStatus().equals(0))) {
//                result.setCanPick(true);
//            } else {
//                result.setCanPick(false);
//
//            }
////            result.setCartResults(cartResultList);
//        }
//        return results;
//    }

//    @Override
//    public void format(List<RestOutStockOrderDetailResult> results) {
//        List<Long> skuIds = new ArrayList<>();
//        List<Long> brandIds = new ArrayList<>();
//        List<Long> detailIds = new ArrayList<>();
//        for (RestOutStockOrderDetailResult result : results) {
//            skuIds.add(result.getSkuId());
//            if (ToolUtil.isNotEmpty(result.getBrandId())) {
//                brandIds.add(result.getBrandId());
//            }
//            detailIds.add(result.getPickListsDetailId());
//        }
//        List<Long> lockedInkindIds = this.getLockedInkindIds();
////        List<SkuSimpleResult> skuSimpleResults = skuService.simpleFormatSkuResult(skuIds.stream().distinct().collect(Collectors.toList()));
//        List<SkuListResult> skuLists = skuIds.size()== 0 ? new ArrayList<>() : skuListService.resultByIds(skuIds);
//        /**
//         * 获取相关被锁定实物数量
//         */
//        List<LockedStockDetails> lockSkuAndNumber = pickListsCartService.getLockSkuAndNumber(skuIds);
//
//        /**
//         * 获取购物车信息
//         */
//        List<RestOutStockCart> carts = detailIds.size() == 0 ? new ArrayList<>() : pickListsCartService.query().in("pick_lists_detail_id", detailIds).ne("status", -1).list();
//
//
//        //TODO notin
//        List<StockDetails> stockSkus = skuIds.size() == 0 ? new ArrayList<>() : lockedInkindIds.size() == 0 ? stockDetailsService.query().in("sku_id", skuIds).eq("display", 1).list() : stockDetailsService.query().in("sku_id", skuIds).notIn("inkind_id", lockedInkindIds).eq("display", 1).list();
////        List<RestOutStockCartResult> cartResults = detailIds.size() == 0 ? new ArrayList<>() : pickListsCartService.listByListsDetailIds(detailIds);
//        List<RestOutStockCartResult> cartResults = BeanUtil.copyToList(carts, RestOutStockCartResult.class);
//        List<StorehouseSimpleResult> storehouseResults = cartResults.size() == 0 ? new ArrayList<>() : BeanUtil.copyToList(storehouseService.listByIds(cartResults.stream().map(RestOutStockCartResult::getStorehouseId).distinct().collect(Collectors.toList())), StorehouseSimpleResult.class, new CopyOptions());
//        for (RestOutStockCartResult cartResult : cartResults) {
//            for (StorehouseSimpleResult storehouseResult : storehouseResults) {
//                if (cartResult.getStorehouseId().equals(storehouseResult.getStorehouseId())){
//                    cartResult.setStorehouseResult(storehouseResult);
//                    break;
//                }
//
//            }
//        }
//
//        for (StockDetails skus : stockSkus) {
//            if (ToolUtil.isEmpty(skus.getBrandId())) {
//                skus.setBrandId(0L);
//            }
//        }
//        List<Long> positionIds = new ArrayList<>();
//        List<StockDetails> totalList = new ArrayList<>();
//        stockSkus.parallelStream().collect(Collectors.groupingBy(item -> item.getSkuId() + '_' + (ToolUtil.isEmpty(item.getBrandId()) ? 0L : item.getBrandId()), Collectors.toList())).forEach(
//                (id, transfer) -> {
//                    transfer.stream().reduce((a, b) -> new StockDetails() {{
//                        setSkuId(a.getSkuId());
//                        setNumber(a.getNumber() + b.getNumber());
//                        setBrandId(ToolUtil.isEmpty(a.getBrandId()) ? 0L : a.getBrandId());
//                        setStorehousePositionsId(a.getStorehousePositionsId());
//                    }}).ifPresent(totalList::add);
//                }
//        );
//        for (StockDetails stockDetails : stockSkus) {
//            positionIds.add(stockDetails.getStorehousePositionsId());
//        }
//        positionIds = positionIds.stream().distinct().collect(Collectors.toList());
////        List<StorehousePositionsResult> positionsResultList = storehousePositionsService.resultsByIds(positionIds);
//        List<StorehousePositionsResult> positionsResultList =positionIds.size() == 0? new ArrayList<>() : BeanUtil.copyToList(storehousePositionsService.listByIds(positionIds),StorehousePositionsResult.class);
//        List<StockDetails> anyBrand = new ArrayList<>();
//        stockSkus.parallelStream().collect(Collectors.groupingBy(item -> item.getSkuId() + "", Collectors.toList())).forEach(
//                (id, transfer) -> {
//                    transfer.stream().reduce((a, b) -> new StockDetails() {{
//                        setSkuId(a.getSkuId());
//                        setNumber(a.getNumber() + b.getNumber());
//                        setStorehousePositionsId(a.getStorehousePositionsId());
//                    }}).ifPresent(anyBrand::add);
//                }
//        );
//        List<StockDetails> positionTotal = new ArrayList<>();
//        stockSkus.parallelStream().collect(Collectors.groupingBy(item -> item.getSkuId() + "_" + item.getBrandId() + "_" + item.getStorehousePositionsId(), Collectors.toList())).forEach(
//                (id, transfer) -> {
//                    transfer.stream().reduce((a, b) -> new StockDetails() {{
//                        setSkuId(a.getSkuId());
//                        setNumber(a.getNumber() + b.getNumber());
//                        setStorehousePositionsId(a.getStorehousePositionsId());
//                    }}).ifPresent(positionTotal::add);
//                }
//        );
//
//        List<BrandResult> brandResults = brandIds.size() == 0 ? new ArrayList<>() : brandService.getBrandResults(brandIds);
//        for (RestOutStockOrderDetailResult result : results) {
//            //还没有备料的数量 detail中总数减去 用购物车中数量
//            result.setNeedOperateNum(result.getNumber());
//            List<String> positionNames = new ArrayList<>();
//            List<Long> positionIdList = new ArrayList<>();
//            result.setIsMeet(false);
//
//            for (RestOutStockCart cart : carts) {
//                if (result.getPickListsDetailId().equals(cart.getPickListsDetailId())) {
//                    result.setNeedOperateNum(result.getNeedOperateNum() - cart.getNumber());
//                }
//            }
//
//
//            if (!result.getBrandId().equals(0L)) {
//                for (StockDetails stockSkuTotal : totalList) {
//                    if (result.getSkuId().equals(stockSkuTotal.getSkuId()) && result.getBrandId().equals(stockSkuTotal.getBrandId())) {
//                        result.setStockNumber(Math.toIntExact(stockSkuTotal.getNumber()));
//                        for (StorehousePositionsResult positionsResult : positionsResultList) {
//                            if (stockSkuTotal.getStorehousePositionsId().equals(positionsResult.getStorehousePositionsId())) {
//                                positionNames.add(positionsResult.getName());
//                            }
//                        }
//                        if (result.getNumber() <= stockSkuTotal.getNumber()) {
//                            result.setIsMeet(true);
//                        }
//                    }
//                }
//                for (StockDetails stockDetails : positionTotal) {
//                    for (StorehousePositionsResult positionsResult : positionsResultList) {
//                        if (result.getSkuId().equals(stockDetails.getSkuId()) && result.getBrandId().equals(stockDetails.getBrandId()) && stockDetails.getStorehousePositionsId().equals(positionsResult.getStorehousePositionsId())) {
//                            positionNames.add(positionsResult.getName());
//                        }
//                    }
//                }
//                for (LockedStockDetails lockedStockDetails : lockSkuAndNumber) {
//                    if (result.getSkuId().equals(lockedStockDetails.getSkuId()) && result.getBrandId().equals(lockedStockDetails.getBrandId())) {
//                        result.setLockStockDetailNumber(lockedStockDetails.getNumber());
//                    }
//                }
//            } else {
//                for (StockDetails stockDetails : anyBrand) {
//                    if (result.getSkuId().equals(stockDetails.getSkuId())) {
//                        result.setStockNumber(Math.toIntExact(stockDetails.getNumber()));
//                        if (result.getNumber() <= stockDetails.getNumber()) {
//                            result.setIsMeet(true);
//                        }
//
//                    }
//                }
//                for (StockDetails stockDetails : positionTotal) {
//                    for (StorehousePositionsResult positionsResult : positionsResultList) {
//                        if (result.getSkuId().equals(stockDetails.getSkuId()) && stockDetails.getStorehousePositionsId().equals(positionsResult.getStorehousePositionsId())) {
//                            positionNames.add(positionsResult.getName());
//                            positionIdList.add(positionsResult.getStorehousePositionsId());
//                        }
//                    }
//                }
//                int lockedNumber = 0;
//                for (LockedStockDetails lockedStockDetails : lockSkuAndNumber) {
//                    if (result.getSkuId().equals(lockedStockDetails.getSkuId())) {
//                        lockedNumber += lockedStockDetails.getNumber();
//                    }
//                }
//                result.setLockStockDetailNumber(lockedNumber);
//            }
//            //返回可备料仓库名称
//            positionNames = positionNames.stream().distinct().collect(Collectors.toList());
//            result.setPositionNames(positionNames);
//            positionIdList = positionIdList.stream().distinct().collect(Collectors.toList());
//            result.setPositionIds(positionIdList);
//
//            for (SkuListResult skuSimpleResult : skuLists) {
//                if (result.getSkuId().equals(skuSimpleResult.getSkuId())) {
//                    result.setSkuResult(skuSimpleResult);
//                    break;
//                }
//            }
//            for (BrandResult brandResult : brandResults) {
//                if (ToolUtil.isNotEmpty(result.getBrandId()) && result.getBrandId().equals(brandResult.getBrandId())) {
//                    Map<String, String> brandMap = new HashMap<>();
//                    brandMap.put("brandId", brandResult.getBrandId().toString());
//                    brandMap.put("brandName", brandResult.getBrandName());
//                    result.setBrandResult(brandMap);
//                }
//            }
//            List<RestOutStockCartResult> cartResultList = new ArrayList<>();
//            for (RestOutStockCartResult cartResult : cartResults) {
//                if (result.getPickListsDetailId().equals(cartResult.getPickListsDetailId())) {
//                    cartResultList.add(cartResult);
//                }
//            }
//            if (cartResultList.stream().anyMatch(i -> i.getStatus().equals(0))) {
//                result.setCanPick(true);
//            } else {
//                result.setCanPick(false);
//
//            }
//            result.setCartResults(BeanUtil.copyToList(cartResultList, RestOutStockCartSimpleResult.class));
//        }
//
//    }

    public List<Long> getLockedInkindIds() {
        List<RestOutStockCart> list = pickListsCartService.query().eq("status", 0).eq("display", 1).list();
        List<Long> inkindIds = new ArrayList<>();
        for (RestOutStockCart pickListsCart : list) {
            inkindIds.add(pickListsCart.getInkindId());
        }
        return inkindIds;
    }

    @Override
    public List<RestOutStockOrderDetailResult> getByTask(RestOutStockOrderDetailParam param) {
        return this.baseMapper.customList(param);
    }

    @Override
    public PageInfo<RestOutStockOrderDetailResult> findPageBySpec(RestOutStockOrderDetailParam param) {
        Page<RestOutStockOrderDetailResult> pageContext = getPageContext();
        IPage<RestOutStockOrderDetailResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }
    @Override
    public PageInfo<RestOutStockOrderDetailResult> pickListsDetailList(RestOutStockOrderDetailParam param) {
        Page<RestOutStockOrderDetailResult> pageContext = getPageContext();
        IPage<RestOutStockOrderDetailResult> page = this.baseMapper.pickListsDetailList(pageContext, param);
//        this.format(page.getRecords());
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(RestOutStockOrderDetailParam param) {
        return param.getPickListsDetailId();
    }

    private Page<RestOutStockOrderDetailResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private RestOutStockOrderDetail getOldEntity(RestOutStockOrderDetailParam param) {
        return this.getById(getKey(param));
    }

    private RestOutStockOrderDetail getEntity(RestOutStockOrderDetailParam param) {
        RestOutStockOrderDetail entity = new RestOutStockOrderDetail();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }
//
//    @Override
//    public List<StockView> getUserSkuAndNumbers(DataStatisticsViewParam param) {
//        return this.baseMapper.userSkuAndNumbers(param);
//    }

    @Override
    public List<Long> getPisitionIds(Long listsId) {
        List<Long> skuIds = getSkuIdsByPickLists(listsId);
        List<Long> lockedInkindIds = this.getLockedInkindIds();
        //TODO notin
        List<RestStockDetails> stockSkus = skuIds.size() == 0 ? new ArrayList<>() : lockedInkindIds.size() == 0 ? stockDetailsService.query().in("sku_id", skuIds).eq("display", 1).list() : stockDetailsService.query().in("sku_id", skuIds).notIn("inkind_id", lockedInkindIds).eq("display", 1).list();

        List<Long> positionIds = new ArrayList<>();

        for (RestStockDetails stockDetails : stockSkus) {
            positionIds.add(stockDetails.getStorehousePositionsId());
        }
        return positionIds.stream().distinct().collect(Collectors.toList());
    }

}
