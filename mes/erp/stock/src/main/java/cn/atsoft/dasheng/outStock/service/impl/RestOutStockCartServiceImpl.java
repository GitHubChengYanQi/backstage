package cn.atsoft.dasheng.outStock.service.impl;


//import cn.atsoft.dasheng.app.entity.StockDetails;
//import cn.atsoft.dasheng.app.entity.Storehouse;
//import cn.atsoft.dasheng.app.model.result.BrandResult;
//import cn.atsoft.dasheng.app.model.result.StockDetailsResult;
//import cn.atsoft.dasheng.app.model.result.StorehouseResult;
//import cn.atsoft.dasheng.app.model.result.StorehouseSimpleResult;
//import cn.atsoft.dasheng.app.pojo.StockSkuBrand;
//import cn.atsoft.dasheng.app.service.BrandService;
//import cn.atsoft.dasheng.app.service.StockDetailsService;
//import cn.atsoft.dasheng.app.service.StorehouseService;
import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.util.ToolUtil;
//import cn.atsoft.dasheng.erp.entity.Inkind;
//import cn.atsoft.dasheng.erp.service.InkindService;
//import cn.atsoft.dasheng.erp.service.ShopCartService;
//import cn.atsoft.dasheng.erp.service.SkuService;
//import cn.atsoft.dasheng.erp.service.StorehousePositionsService;
import cn.atsoft.dasheng.entity.RestTraceability;
import cn.atsoft.dasheng.form.entity.ActivitiProcessTask;
import cn.atsoft.dasheng.goods.sku.service.RestSkuService;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.outStock.entity.RestOutStockCart;
import cn.atsoft.dasheng.outStock.mapper.RestOutStockCartMapper;
import cn.atsoft.dasheng.outStock.model.params.RestOutStockCartParam;
import cn.atsoft.dasheng.outStock.service.RestOutStockCartService;
import cn.atsoft.dasheng.outStock.service.RestOutStockOrderDetailService;
import cn.atsoft.dasheng.outStock.service.RestOutStockOrderService;
//import cn.atsoft.dasheng.production.entity.ProductionPickLists;
//import cn.atsoft.dasheng.production.entity.RestOutStockCart;
//import cn.atsoft.dasheng.production.entity.RestOutStockOrderDetail;
//import cn.atsoft.dasheng.production.mapper.RestOutStockCartMapper;
//import cn.atsoft.dasheng.production.model.params.RestOutStockCartParam;
//import cn.atsoft.dasheng.production.model.params.RestOutStockOrderDetailParam;
//import cn.atsoft.dasheng.production.model.params.ProductionPickListsParam;
//import cn.atsoft.dasheng.production.model.request.CartGroupByUserListRequest;
//import cn.atsoft.dasheng.production.model.result.PickListsStorehouseResult;
//import cn.atsoft.dasheng.production.model.result.RestOutStockCartResult;
//import cn.atsoft.dasheng.production.model.result.RestOutStockOrderDetailResult;
//import cn.atsoft.dasheng.production.model.result.ProductionPickListsResult;
//import cn.atsoft.dasheng.production.pojo.LockedStockDetails;
//import cn.atsoft.dasheng.production.pojo.QuerryLockedParam;
//import cn.atsoft.dasheng.production.service.RestOutStockCartService;
//import cn.atsoft.dasheng.production.service.RestOutStockOrderDetailService;
//import cn.atsoft.dasheng.production.service.ProductionPickListsService;
import cn.atsoft.dasheng.service.RestTraceabilityService;
import cn.atsoft.dasheng.stockDetail.entity.RestStockDetails;
import cn.atsoft.dasheng.stockDetail.service.RestStockDetailsService;
import cn.atsoft.dasheng.storehouse.service.RestStorehouseService;
import cn.atsoft.dasheng.storehousePosition.service.RestStorehousePositionsService;
import cn.atsoft.dasheng.sys.modular.system.model.result.UserResult;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 领料单详情表 服务实现类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-03-25
 */
@Service
public class RestOutStockCartServiceImpl extends ServiceImpl<RestOutStockCartMapper, RestOutStockCart> implements RestOutStockCartService {


    @Autowired
    private RestSkuService skuService;

    @Autowired
    private RestOutStockOrderService pickListsService;

    @Autowired
    private UserService userService;
    @Autowired
    private RestStockDetailsService stockDetailsService;

    @Autowired
    private RestOutStockOrderDetailService pickListsDetailService;

//    @Autowired
//    private BrandService brandService;

    @Autowired
    private RestStorehouseService storehouseService;

//    @Autowired
//    private ShopCartService shopCartService;
    @Autowired
    private RestStorehousePositionsService storehousePositionsService;
    @Autowired
    private RestTraceabilityService inkindService;


    @Override
    public void add(RestOutStockCartParam param, List<RestStockDetails> stockDetails) {


        /**
         * 获取 库存中实物信息  拆分实物时匹配数据
         */
//        List<RestTraceability> inkinds = this.getStockDetailInkinds(stockDetails);
        List<RestTraceability> inkinds = new ArrayList<>();
        /**
         * 查找扫码物料是否存在于备料之中  如果存在 此物料不可再次扫码备料
         */
        List<Long> lockInkinds = new ArrayList<>();
        for (RestOutStockCartParam productionPickListsCartParam : param.getProductionPickListsCartParams()) {
            if (ToolUtil.isNotEmpty(productionPickListsCartParam.getInkindId())) {
                lockInkinds.add(productionPickListsCartParam.getInkindId());
            }
        }
        int lockInkindCount = lockInkinds.size() == 0 ? 0 : this.query().in("inkind_id", lockInkinds).count();
        if (lockInkindCount > 0) {
            throw new ServiceException(500, "扫码物料已被备料,不可再次备料,操作终止");
        }


        /**
         * 过滤实物
         */
        List<RestOutStockCart> entitys = new ArrayList<>();
        List<RestStockDetails> newStockDetails = new ArrayList<>();
        for (RestOutStockCartParam productionPickListsCartParam : param.getProductionPickListsCartParams()) {
            if (ToolUtil.isEmpty(productionPickListsCartParam.getBrandId())) {
                productionPickListsCartParam.setBrandId(0L);
            }
            if (stockDetails.size() == 0) {
                throw new ServiceException(500, "该库位库存不足");
            } else {
                int count = 0;
                for (RestStockDetails stockDetail : stockDetails) {
                    count += stockDetail.getNumber();
                }
                if (productionPickListsCartParam.getNumber() > count) {
                    throw new ServiceException(500, "该库位库存不足");
                }
            }
            int number = productionPickListsCartParam.getNumber();

            if (ToolUtil.isNotEmpty(productionPickListsCartParam.getInkindId())) {
                RestTraceability inkind = inkindService.getById(productionPickListsCartParam.getInkindId());
                RestOutStockCart entity = new RestOutStockCart();
                entity.setPickListsId(productionPickListsCartParam.getPickListsId());
                entity.setCustomerId(inkind.getCustomerId());
                entity.setPickListsDetailId(productionPickListsCartParam.getPickListsDetailId());
                entity.setStorehousePositionsId(productionPickListsCartParam.getStorehousePositionsId());
                entity.setStorehouseId(productionPickListsCartParam.getStorehouseId());
                entity.setSkuId(productionPickListsCartParam.getSkuId());
                entity.setBrandId(productionPickListsCartParam.getBrandId());
                entity.setCustomerId(productionPickListsCartParam.getCustomerId());
                entity.setNumber(productionPickListsCartParam.getNumber());
                entity.setType(productionPickListsCartParam.getType());
                entity.setInkindId(productionPickListsCartParam.getInkindId());
                entitys.add(entity);
                stockDetails.removeIf(i -> i.getInkindId().equals(productionPickListsCartParam.getInkindId()));
            } else {
                for (RestStockDetails stockDetail : stockDetails) {
                    if (number > 0) {
                        if ((ToolUtil.isNotEmpty(stockDetail.getBrandId()) && stockDetail.getBrandId().equals(productionPickListsCartParam.getBrandId()) && stockDetail.getSkuId().equals(productionPickListsCartParam.getSkuId()) && stockDetail.getStorehousePositionsId().equals(productionPickListsCartParam.getStorehousePositionsId())) || ((ToolUtil.isEmpty(stockDetail.getBrandId()) || stockDetail.getBrandId().equals(0L)) && stockDetail.getSkuId().equals(productionPickListsCartParam.getSkuId()) && stockDetail.getStorehousePositionsId().equals(productionPickListsCartParam.getStorehousePositionsId()))) {
                            int lastNumber = number;
                            number -= stockDetail.getNumber();
                            if (number >= 0) {  //如果库存实物被全部备料 则不拆分实物  反之 需要拆分实物
                                RestOutStockCart entity = new RestOutStockCart();
                                entity.setNumber(Math.toIntExact(stockDetail.getNumber()));
                                entity.setSkuId(stockDetail.getSkuId());
                                if (ToolUtil.isNotEmpty(stockDetail.getBrandId())) {
                                    entity.setBrandId(stockDetail.getBrandId());
                                }

                                entity.setPickListsId(productionPickListsCartParam.getPickListsId());
                                entity.setPickListsDetailId(productionPickListsCartParam.getPickListsDetailId());
                                entity.setStorehousePositionsId(productionPickListsCartParam.getStorehousePositionsId());
                                entity.setStorehouseId(productionPickListsCartParam.getStorehouseId());
                                entity.setType(productionPickListsCartParam.getType());
                                entity.setInkindId(stockDetail.getInkindId());
                                entity.setCustomerId(stockDetail.getCustomerId());
                                entitys.add(entity);
                            } else {
                                RestOutStockCart entity = new RestOutStockCart();
                                entity.setNumber(lastNumber);
                                entity.setSkuId(stockDetail.getSkuId());
                                if (ToolUtil.isNotEmpty(stockDetail.getBrandId())) {
                                    entity.setBrandId(stockDetail.getBrandId());
                                }
                                for (RestTraceability inkind : inkinds) {
                                    if (stockDetail.getInkindId().equals(inkind.getInkindId())) {
                                        RestTraceability newInkind = BeanUtil.copyProperties(inkind, RestTraceability.class);
                                        newInkind.setInkindId(null);
                                        newInkind.setSource("Inkind");
                                        newInkind.setSourceId(inkind.getInkindId());
                                        inkindService.save(newInkind);
                                        RestStockDetails newStockDetail = BeanUtil.copyProperties(stockDetail, RestStockDetails.class);
                                        newStockDetail.setStockItemId(null);
                                        newStockDetail.setNumber((long) lastNumber);
                                        newStockDetail.setInkindId(newInkind.getInkindId());
                                        newStockDetails.add(newStockDetail);
                                        entity.setStorehousePositionsId(productionPickListsCartParam.getStorehousePositionsId());
                                        entity.setStorehouseId(productionPickListsCartParam.getStorehouseId());
                                        entity.setPickListsId(productionPickListsCartParam.getPickListsId());
                                        entity.setPickListsDetailId(productionPickListsCartParam.getPickListsDetailId());
                                        entity.setType(productionPickListsCartParam.getType());
                                        entity.setInkindId(newInkind.getInkindId());
                                        entity.setNumber(Math.toIntExact(newStockDetail.getNumber()));
                                        entity.setCustomerId(newInkind.getCustomerId());
                                        entitys.add(entity);
                                        stockDetail.setNumber(stockDetail.getNumber()- lastNumber);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        stockDetailsService.saveBatch(newStockDetails);
        stockDetailsService.updateBatchById(stockDetails);
        if (entitys.size() > 0) {
            this.saveBatch(entitys);
//            TODO
//            String skuName = skuService.skuMessage(entitys.get(0).getSkuId());
//            shopCartService.addDynamic(entitys.get(0).getPickListsId(), entitys.get(0).getSkuId(),skuName + "进行了备料");
        } else {
            throw new ServiceException(500, "未匹配到所需物料,备料失败");
        }

    }
//    @Override
//    public List<RestOutStockOrderDetailResult> autoAdd(RestOutStockCartParam param){
//        Long pickListsId = param.getPickListsId();
//        //查出全部申请的物料以及数量信息
//        List<RestOutStockOrderDetail> listsDetails = pickListsDetailService.lambdaQuery().eq(RestOutStockOrderDetail::getPickListsId, pickListsId).eq(RestOutStockOrderDetail::getDisplay, 1).ne(RestOutStockOrderDetail::getStatus,99).list();
//        List<RestOutStockCart> inCarts =listsDetails.size() == 0 ? new ArrayList<>() : this.lambdaQuery().in(RestOutStockCart::getPickListsDetailId, listsDetails.stream().map(RestOutStockOrderDetail::getPickListsDetailId).distinct().collect(Collectors.toList())).eq(RestOutStockCart::getDisplay, 1).eq(RestOutStockCart::getStatus, 0).list();
//        for (RestOutStockOrderDetail listsDetail : listsDetails) {
//            for (RestOutStockCart inCart : inCarts) {
//                if (listsDetail.getPickListsDetailId().equals(inCart.getPickListsDetailId())){
//                    listsDetail.setNumber(listsDetail.getNumber()-inCart.getNumber());
//                }
//            }
//        }
//        List<Long> skuIds = listsDetails.stream().map(RestOutStockOrderDetail::getSkuId).collect(Collectors.toList());
//        List<Long> brandIds = listsDetails.stream().map(RestOutStockOrderDetail::getBrandId).distinct().collect(Collectors.toList());
//        for (RestOutStockOrderDetail listsDetail : listsDetails) {
//            listsDetail.setNumber(listsDetail.getNumber()-listsDetail.getReceivedNumber());
//        }
//        /**
//         * 首先抛去已备料实物  查询出 sku的库存
//         *
//         */
//        /**
//         * 获取购物车中实物
//         */
//        List<Long> cartInkindIds = this.getCartInkindIds(skuIds);
//        //查询库存
//        LambdaQueryChainWrapper<RestStockDetails> stockDetailWrapper = stockDetailsService.lambdaQuery();
//        stockDetailWrapper.in(RestStockDetails::getSkuId, skuIds);
//        stockDetailWrapper.eq(RestStockDetails::getDisplay,1);
//        stockDetailWrapper.eq(RestStockDetails::getStage,1);
//        if (brandIds.size()>0 && brandIds.stream().noneMatch(i->i.equals(0L))) {
//            stockDetailWrapper.in(RestStockDetails::getBrandId,brandIds);
//        }
//        stockDetailWrapper.orderByAsc(RestStockDetails::getCreateTime);
//        if (cartInkindIds.size() > 0) {
//            stockDetailWrapper.notIn(RestStockDetails::getInkindId,cartInkindIds);
//        }
//        List<RestStockDetails> stockDetails = stockDetailWrapper.list();
//        List<RestTraceability> inkinds =stockDetails.size() == 0 ? new ArrayList<>() : inkindService.listByIds(stockDetails.stream().map(RestStockDetails::getInkindId).distinct().collect(Collectors.toList()));
//        List<RestOutStockOrderDetailResult> results = new ArrayList<>();
//        //计算出库
//        List<RestOutStockCart> entitys = new ArrayList<>();
//        List<RestStockDetails> newStockDetails = new ArrayList<>();
//        for (RestOutStockOrderDetail listsDetail : listsDetails) {
//            if (ToolUtil.isEmpty(listsDetail.getBrandId())) {
//                listsDetail.setBrandId(0L);
//            }
////            if (stockDetails.size() == 0) {
////                throw new ServiceException(500, "该库位库存不足");
////            } else {
////                int count = 0;
////                for (StockDetails stockDetail : stockDetails) {
////                    count += stockDetail.getNumber();
////                }
////                if (listsDetail.getNumber() > count) {
////                    throw new ServiceException(500, "该库位库存不足");
////                }
////            }
//            int number = listsDetail.getNumber();
//
////            if (ToolUtil.isNotEmpty(listsDetail.getInkindId())) {
////                Inkind inkind = inkindService.getById(productionPickListsCartParam.getInkindId());
////                RestOutStockCart entity = new RestOutStockCart();
////                entity.setPickListsId(productionPickListsCartParam.getPickListsId());
////                entity.setCustomerId(inkind.getCustomerId());
////                entity.setPickListsDetailId(productionPickListsCartParam.getPickListsDetailId());
////                entity.setStorehousePositionsId(productionPickListsCartParam.getStorehousePositionsId());
////                entity.setStorehouseId(productionPickListsCartParam.getStorehouseId());
////                entity.setSkuId(productionPickListsCartParam.getSkuId());
////                entity.setBrandId(productionPickListsCartParam.getBrandId());
////                entity.setCustomerId(productionPickListsCartParam.getCustomerId());
////                entity.setNumber(productionPickListsCartParam.getNumber());
////                entity.setType(productionPickListsCartParam.getType());
////                entity.setInkindId(productionPickListsCartParam.getInkindId());
////                entitys.add(entity);
////                stockDetails.removeIf(i -> i.getInkindId().equals(productionPickListsCartParam.getInkindId()));
////            } else {
//                for (RestStockDetails stockDetail : stockDetails) {
//                    if (number > 0) {
////                        if ((ToolUtil.isNotEmpty(stockDetail.getBrandId()) && stockDetail.getBrandId().equals(listsDetail.getBrandId()) && stockDetail.getSkuId().equals(listsDetail.getSkuId()) && stockDetail.getStorehousePositionsId().equals(productionPickListsCartParam.getStorehousePositionsId())) || ((ToolUtil.isEmpty(stockDetail.getBrandId()) || stockDetail.getBrandId().equals(0L)) && stockDetail.getSkuId().equals(productionPickListsCartParam.getSkuId()) && stockDetail.getStorehousePositionsId().equals(productionPickListsCartParam.getStorehousePositionsId()))) {
//                        if ((ToolUtil.isNotEmpty(stockDetail.getBrandId()) && stockDetail.getBrandId().equals(listsDetail.getBrandId()) && stockDetail.getSkuId().equals(listsDetail.getSkuId())) || ((ToolUtil.isEmpty(stockDetail.getBrandId()) || stockDetail.getBrandId().equals(0L)) && stockDetail.getSkuId().equals(listsDetail.getSkuId()))) {
//                            int lastNumber = number;
//                            number -= stockDetail.getNumber();
//                            if (number >= 0) {  //如果库存实物被全部备料 则不拆分实物  反之 需要拆分实物
//                                RestOutStockCart entity = new RestOutStockCart();
//                                entity.setNumber(Math.toIntExact(stockDetail.getNumber()));
//                                entity.setSkuId(stockDetail.getSkuId());
//                                if (ToolUtil.isNotEmpty(stockDetail.getBrandId())) {
//                                    entity.setBrandId(stockDetail.getBrandId());
//                                }
//
//                                entity.setPickListsId(listsDetail.getPickListsId());
//                                entity.setPickListsDetailId(listsDetail.getPickListsDetailId());
//                                entity.setStorehousePositionsId(stockDetail.getStorehousePositionsId());
//                                entity.setStorehouseId(stockDetail.getStorehouseId());
//                                entity.setInkindId(stockDetail.getInkindId());
//                                entity.setCustomerId(stockDetail.getCustomerId());
//                                entitys.add(entity);
//                            } else {
//                                RestOutStockCart entity = new RestOutStockCart();
//                                entity.setNumber(lastNumber);
//                                entity.setSkuId(stockDetail.getSkuId());
//                                if (ToolUtil.isNotEmpty(stockDetail.getBrandId())) {
//                                    entity.setBrandId(stockDetail.getBrandId());
//                                }
//                                for (RestTraceability inkind : inkinds) {
//                                    if (stockDetail.getInkindId().equals(inkind.getInkindId())) {
//                                        RestTraceability newInkind = BeanUtil.copyProperties(inkind, RestTraceability.class);
//                                        newInkind.setInkindId(null);
//                                        newInkind.setSource("Inkind");
//                                        newInkind.setSourceId(inkind.getInkindId());
//                                        inkindService.save(newInkind);
//                                        RestStockDetails newStockDetail = BeanUtil.copyProperties(stockDetail, RestStockDetails.class);
//                                        newStockDetail.setStockItemId(null);
//                                        newStockDetail.setNumber((long) lastNumber);
//                                        newStockDetail.setInkindId(newInkind.getInkindId());
//                                        newStockDetails.add(newStockDetail);
//                                        entity.setStorehousePositionsId(stockDetail.getStorehousePositionsId());
//                                        entity.setStorehouseId(stockDetail.getStorehouseId());
//                                        entity.setPickListsId(listsDetail.getPickListsId());
//                                        entity.setPickListsDetailId(listsDetail.getPickListsDetailId());
//                                        entity.setInkindId(newInkind.getInkindId());
//                                        entity.setNumber(Math.toIntExact(newStockDetail.getNumber()));
//                                        entity.setCustomerId(newInkind.getCustomerId());
//                                        entitys.add(entity);
//                                        stockDetail.setNumber(stockDetail.getNumber()- lastNumber);
//                                        break;
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
////            }
//            if (number>0){
//                RestOutStockOrderDetailResult productionPickListsDetailResult = BeanUtil.copyProperties(listsDetail, RestOutStockOrderDetailResult.class);
//                productionPickListsDetailResult.setNumber(number);
//                results.add(productionPickListsDetailResult);
//            }
//        }
//        stockDetailsService.saveBatch(newStockDetails);
//        stockDetailsService.updateBatchById(stockDetails);
//        if (entitys.size() > 0) {
//            this.saveBatch(entitys);
////            String skuName = skuService.skuMessage(entitys.get(0).getSkuId());
////            shopCartService.addDynamic(entitys.get(0).getPickListsId(), entitys.get(0).getSkuId(),skuName + "进行了备料");
//        } else {
//            throw new ServiceException(500, "未匹配到所需物料,备料失败");
//        }
//        pickListsDetailService.format(results);
//        return results;
//
//
//
//
//    }
//
//    /**
//     * 库存预警
//     */
//    @Override
//    public boolean warning(RestOutStockCartParam param) {
//
////        List<StockSkuBrand> stockSkuBrands = stockDetailsService.stockSkuBrands();
////        if (stockSkuBrands.size() == 0) {
////            throw new ServiceException(500, "库存数不足");
////        }
////
////        //申请的物料跟库存数比较  并更新库存
////        for (RestOutStockCartParam productionPickListsCartParam : param.getRestOutStockCartParams()) {
////            updateStock(productionPickListsCartParam, stockSkuBrands);
////        }
////
////
////        Map<Integer, List<ActivitiProcessTask>> map = pickListsService.unExecuted(param.getTaskId());
////        List<ActivitiProcessTask> executed = map.get(99);  // 到执行节点的
////
////        List<Long> pickListsIds = new ArrayList<>();
////        for (ActivitiProcessTask processTask : executed) {
////            pickListsIds.add(processTask.getFormId());
////        }
////
////        List<RestOutStockOrderDetail> pickListsDetails = pickListsIds.size() == 0 ? new ArrayList<>() : pickListsDetailService.query().in("pick_lists_id", pickListsIds)
////                .eq("display", 1)
////                .eq("status", 0).list();
////
////        List<RestOutStockOrderDetailParam> pickListsDetailParams = BeanUtil.copyToList(pickListsDetails, RestOutStockOrderDetailParam.class, new CopyOptions());
////
////
////        for (RestOutStockOrderDetailParam pickListsDetailParam : pickListsDetailParams) {
////            for (RestOutStockCartParam listParam : param.getRestOutStockCartParams()) {
////                if (listParam.getSkuId().equals(pickListsDetailParam.getSkuId()) && listParam.getBrandId().equals(pickListsDetailParam.getBrandId())) {
////                    if (pickListsService.updateStock(pickListsDetailParam, stockSkuBrands)) {
////                        return true;
////                    }
////
////                }
////            }
////        }
//
////        for (RestOutStockOrderDetailParam listsParam : pickListsDetailParams) {
////
////        }
//        return false;
//    }
//
////    /**
////     * 更新库存
////     *
////     * @param detailParam
////     * @param stockSkuBrands
////     */
////    private void updateStock(RestOutStockCartParam detailParam, List<StockSkuBrand> stockSkuBrands) {
//
////        for (StockSkuBrand stockSkuBrand : stockSkuBrands) {
////            if ((ToolUtil.isEmpty(detailParam.getBrandId()) || detailParam.getBrandId() == 0) && detailParam.getSkuId().equals(stockSkuBrand.getSkuId())) {  //不指定品牌
////                long number = stockSkuBrand.getNumber() - detailParam.getNumber();
////
////                stockSkuBrand.setNumber(number);
////
////            } else if (ToolUtil.isNotEmpty(detailParam.getBrandId()) &&  detailParam.getBrandId() != 0 && detailParam.getBrandId().equals(stockSkuBrand.getBrandId()) && detailParam.getSkuId().equals(stockSkuBrand.getSkuId())) {  //指定品牌
////                long number = stockSkuBrand.getNumber() - detailParam.getNumber();
////
////                stockSkuBrand.setNumber(number);
////                break;
////            }
////        }
//
////    }
//
//    List<RestTraceability> getStockDetailInkinds(List<RestStockDetails> stockDetails) {
//        List<Long> inkindIds = new ArrayList<>();
//        for (RestStockDetails stockDetail : stockDetails) {
//            inkindIds.add(stockDetail.getInkindId());
//        }
//        return inkindIds.size() == 0 ? new ArrayList<>() : inkindService.listByIds(inkindIds);
//    }
//
//    @Override
//    public void addCheck(RestOutStockCartParam param) {
//        List<Long> detailIds = new ArrayList<>();
//        for (RestOutStockCartParam productionPickListsCartParam : param.getProductionPickListsCartParams()) {
//            detailIds.add(productionPickListsCartParam.getPickListsDetailId());
//        }
//        if (ToolUtil.isEmpty(detailIds) || detailIds.size() == 0) {
//            throw new ServiceException(500, "请选中备料信息");
//        }
//        List<Long> inkindIds = new ArrayList<>();
//        List<RestOutStockOrderDetail> details = pickListsDetailService.listByIds(detailIds);
//        for (RestOutStockCartParam productionPickListsCartParam : param.getProductionPickListsCartParams()) {
//            for (RestOutStockOrderDetail listsDetail : details) {
//                if (productionPickListsCartParam.getPickListsDetailId().equals(listsDetail.getPickListsDetailId())) {
//                    if (!listsDetail.getBrandId().equals(productionPickListsCartParam.getBrandId()) && !listsDetail.getBrandId().equals(0L)) {
//                        throw new ServiceException(500, "请选择对应品牌");
//                    }
//                    if (!listsDetail.getSkuId().equals(productionPickListsCartParam.getSkuId())) {
//                        throw new ServiceException(500, "请选择这对应物料");
//                    }
//                    if (listsDetail.getNumber() - listsDetail.getReceivedNumber() < productionPickListsCartParam.getNumber()) {
//                        throw new ServiceException(500, "备料数量超出申请数量");
//                    }
//
//                }
//            }
//            if (ToolUtil.isNotEmpty(productionPickListsCartParam.getInkindId())) {
//                inkindIds.add(productionPickListsCartParam.getInkindId());
//            }
//        }
//        List<RestStockDetails> stockDetails = inkindIds.size() == 0 ? new ArrayList<>() : stockDetailsService.query().in("inkind_id", inkindIds).eq("display", 1).list();
//        for (RestOutStockCartParam productionPickListsCartParam : param.getProductionPickListsCartParams()) {
//            for (RestStockDetails stockDetail : stockDetails) {
//                if (ToolUtil.isNotEmpty(productionPickListsCartParam.getInkindId()) && stockDetail.getInkindId().equals(productionPickListsCartParam.getInkindId()) && stockDetail.getNumber() > productionPickListsCartParam.getNumber()) {
//                    throw new ServiceException(500, "库存中实物数量小于您填入的数量");
//                }
//            }
//        }
//
//    }
//
//
//    /**
//     * 检查库存中实物是否可以被重复使用
//     *
//     * @param param
//     */
//    private List<RestStockDetails> foundCanBeUseStockDetail(RestOutStockCartParam param) {
//        //查询出所有的购物车  获取实物  下面会检查库存实物是否满足再次被使用
//        List<Long> skuIds = new ArrayList<>();
//        for (RestOutStockCartParam productionPickListsCartParam : param.getProductionPickListsCartParams()) {
//            skuIds.add(productionPickListsCartParam.getSkuId());
//        }
//        List<RestOutStockCart> cartList = this.query().eq("display", 1).list();
//        List<Long> inkindIds = new ArrayList<>();
//        for (RestOutStockCart pickListsCart : cartList) {
//            if (ToolUtil.isNotEmpty(pickListsCart.getInkindId())) {
//                inkindIds.add(pickListsCart.getInkindId());
//            }
//        }
//
//        List<RestStockDetails> beUseStockDetail = stockDetailsService.query().eq("stage", 1).in("sku_id", skuIds).eq("display", 1).list();
//        for (int i = 0; i < beUseStockDetail.size(); ) {
//            i++;
//            for (RestOutStockCart pickListsCart : cartList) {
//                if (beUseStockDetail.get(i).getInkindId().equals(pickListsCart.getInkindId())) {
//                    Long num = beUseStockDetail.get(i).getNumber() - pickListsCart.getNumber();
//                    if (num > 0) {
//                        beUseStockDetail.get(i).setNumber(num);
//
//                    } else {
//                        beUseStockDetail.remove(i);
//                        i -= 1;
//                    }
//                }
//            }
//        }
//        return beUseStockDetail;
//    }
//
//    @Override
//    public void delete(RestOutStockCartParam param) {
//        RestOutStockCart pickListsCart = this.getById(param.getPickListsCart());
//
//        if (ToolUtil.isNotEmpty(pickListsCart.getType()) && pickListsCart.getType().equals("frmLoss")) {
//            throw new ServiceException(500, "报损物料 不可退回");
//        }
//        this.removeById(getKey(param));
//    }
//
//    @Override
//    public void update(RestOutStockCartParam param) {
//        RestOutStockCart oldEntity = getOldEntity(param);
//        RestOutStockCart newEntity = getEntity(param);
//        ToolUtil.copyProperties(newEntity, oldEntity);
//        this.updateById(newEntity);
//    }
//
//    @Override
//    public RestOutStockCartResult findBySpec(RestOutStockCartParam param) {
//        return null;
//    }
//
//    @Override
//    public List<RestOutStockCartResult> findListBySpec(RestOutStockCartParam param) {
//        List<RestOutStockCartResult> productionPickListsCartResults = this.baseMapper.customList(param);
////        this.format(productionPickListsCartResults);
//
//        return productionPickListsCartResults;
//    }
//
//    @Override
//    public PageInfo findPageBySpec(RestOutStockCartParam param) {
//        Page<RestOutStockCartResult> pageContext = getPageContext();
//        IPage<RestOutStockCartResult> page = this.baseMapper.customPageList(pageContext, param);
//        return PageFactory.createPageInfo(page);
//    }
//
////    @Override
////    public List<LockedStockDetails> getLockSkuAndNumber(List<Long> skuIds) {
////        List<RestOutStockCart> lockedSkuNumber = skuIds.size() == 0 ? new ArrayList<>() : this.query().in("sku_id", skuIds).eq("display", 1).eq("status", 0).list();
////        List<RestOutStockCart> totalList = new ArrayList<>();
////        lockedSkuNumber.parallelStream().collect(Collectors.groupingBy(item -> item.getSkuId() + '_' + (ToolUtil.isEmpty(item.getBrandId()) ? 0L : item.getBrandId()), Collectors.toList())).forEach(
////                (id, transfer) -> {
////                    transfer.stream().reduce((a, b) -> new RestOutStockCart() {{
////                        setSkuId(a.getSkuId());
////                        setNumber(a.getNumber() + b.getNumber());
////                        setBrandId(ToolUtil.isEmpty(a.getBrandId()) ? 0L : a.getBrandId());
////                    }}).ifPresent(totalList::add);
////                }
////        );
////
////        return BeanUtil.copyToList(totalList, LockedStockDetails.class);
////
////
////    }
//
////    @Override
////    public void format(List<RestOutStockCartResult> param) {
////        List<Long> skuIds = new ArrayList<>();
////        List<Long> storehouseIds = new ArrayList<>();
////        List<Long> positionIds = new ArrayList<>();
////        List<Long> listsIds = new ArrayList<>();
////        List<Long> detailIds = new ArrayList<>();
////        List<Long> brandIds = new ArrayList<>();
////        for (RestOutStockCartResult productionPickListsCartResult : param) {
////            if (ToolUtil.isNotEmpty(productionPickListsCartResult.getBrandIds())) {
////                for (Long brandId : productionPickListsCartResult.getBrandIds()) {
////                    brandIds.add(brandId);
////                }
////            }
////            skuIds.add(productionPickListsCartResult.getSkuId());
////            storehouseIds.add(productionPickListsCartResult.getStorehouseId());
////            positionIds.add(productionPickListsCartResult.getStorehousePositionsId());
////            listsIds.add(productionPickListsCartResult.getPickListsId());
////            detailIds.add(productionPickListsCartResult.getPickListsDetailId());
////            brandIds.add(productionPickListsCartResult.getBrandId());
////            if (ToolUtil.isNotEmpty(productionPickListsCartResult.getBrandIds())) {
////                brandIds.addAll(productionPickListsCartResult.getBrandIds());
////            }
////        }
////        /**
////         * ids去重
////         */
////        skuIds = skuIds.stream().distinct().collect(Collectors.toList());
////        storehouseIds = storehouseIds.stream().distinct().collect(Collectors.toList());
////        detailIds = detailIds.stream().distinct().collect(Collectors.toList());
////        brandIds = brandIds.stream().distinct().collect(Collectors.toList());
////
////        /**
////         * 查询数据
////         */
////
////        List<StorehouseSimpleResult> storehouseResults = storehouseIds.size() == 0 ? new ArrayList<>() : BeanUtil.copyToList(storehouseService.listByIds(storehouseIds), StorehouseSimpleResult.class, new CopyOptions());
////        List<StorehousePositionsResult> storehousePositionsResults = positionIds.size() == 0 ? new ArrayList<>() : BeanUtil.copyToList(storehousePositionsService.listByIds(positionIds), StorehousePositionsResult.class);
////        List<ProductionPickListsResult> pickListsResults = listsIds.size() == 0 ? new ArrayList<>() : BeanUtil.copyToList(pickListsService.listByIds(listsIds), ProductionPickListsResult.class);
////        List<SkuSimpleResult> skuSimpleResults = skuService.simpleFormatSkuResult(skuIds);
////        List<RestOutStockOrderDetailResult> pickListsDetailResults = detailIds.size() == 0 ? new ArrayList<>() : BeanUtil.copyToList(pickListsDetailService.listByIds(detailIds), RestOutStockOrderDetailResult.class);
////        List<BrandResult> brandResults = brandService.getBrandResults(brandIds);
////        List<StockDetails> list = new ArrayList<>();
////        if (brandIds.size() > 0 && skuIds.size() > 0) {
////            list = stockDetailsService.query().select("sku_id,brand_id, sum(number) as num").in("brand_id", brandIds).in("sku_id", skuIds).eq("display", 1).groupBy("sku_id").groupBy("brand_id").list();
////            List<StockDetails> lockStockDetail = this.getLockStockDetail();
////            for (StockDetails stockDetails : list) {
////                for (StockDetails details : lockStockDetail) {
////                    if (stockDetails.getBrandId().equals(details.getBrandId()) && stockDetails.getSkuId().equals(details.getSkuId())) {
////                        stockDetails.setNum(stockDetails.getNum() - details.getNumber());
////                    }
////                }
////            }
////        }
////        //        pickListsDetailService.format(pickListsDetailResults);
////        for (RestOutStockCartResult productionPickListsCartResult : param) {
////            for (StockDetails stockDetails : list) {
////                if (productionPickListsCartResult.getSkuId().equals(stockDetails.getSkuId()) && ToolUtil.isNotEmpty(productionPickListsCartResult.getBrandId()) && productionPickListsCartResult.getBrandId().equals(stockDetails.getBrandId())) {
////                    productionPickListsCartResult.setStockNumber(stockDetails.getNum());
////                }
////            }
////            if (ToolUtil.isNotEmpty(productionPickListsCartResult.getBrandIds())) {
////                List<String> brandNames = new ArrayList<>();
////                for (Long brandId : productionPickListsCartResult.getBrandIds()) {
////                    for (BrandResult brandResult : brandResults) {
////                        if (brandResult.getBrandId().equals(brandId)) {
////                            brandNames.add(brandResult.getBrandName());
////                        }
////                        if (brandId.equals(0L)) {
////                            brandNames.add("无品牌");
////                        }
////                    }
////                }
////                brandNames = brandNames.stream().distinct().collect(Collectors.toList());
////                productionPickListsCartResult.setBrandNames(brandNames);
////            }
////            for (SkuSimpleResult skuResult : skuSimpleResults) {
////                if (productionPickListsCartResult.getSkuId().equals(skuResult.getSkuId())) {
////                    productionPickListsCartResult.setSkuResult(skuResult);
////                    break;
////                }
////            }
////            for (StorehouseSimpleResult storehouseResult : storehouseResults) {
////                if (storehouseResult.getStorehouseId().equals(productionPickListsCartResult.getStorehouseId())) {
////                    productionPickListsCartResult.setStorehouseResult(storehouseResult);
////                    break;
////                }
////            }
////            for (StorehousePositionsResult storehousePositionsResult : storehousePositionsResults) {
////                if (storehousePositionsResult.getStorehousePositionsId().equals(productionPickListsCartResult.getStorehousePositionsId())) {
////                    productionPickListsCartResult.setStorehousePositionsResult(storehousePositionsResult);
////                    break;
////                }
////            }
////            for (ProductionPickListsResult pickListsResult : pickListsResults) {
////                if (productionPickListsCartResult.getPickListsId().equals(pickListsResult.getPickListsId())) {
////                    productionPickListsCartResult.setPickListsResult(pickListsResult);
////                    break;
////                }
////            }
////            for (RestOutStockOrderDetailResult pickListsDetailResult : pickListsDetailResults) {
////                if (productionPickListsCartResult.getPickListsDetailId().equals(pickListsDetailResult.getPickListsDetailId())) {
////                    productionPickListsCartResult.setRestOutStockOrderDetailResult(pickListsDetailResult);
////                    break;
////                }
////            }
////            for (BrandResult brandResult : brandResults) {
////                if (ToolUtil.isNotEmpty(productionPickListsCartResult.getBrandId()) && productionPickListsCartResult.getBrandId().equals(brandResult.getBrandId())) {
////                    productionPickListsCartResult.setBrandResult(brandResult);
////                    break;
////                }
////            }
////            List<String> brandNames = new ArrayList<>();
////            if (ToolUtil.isNotEmpty(productionPickListsCartResult.getBrandIds())) {
////                for (BrandResult brandResult : brandResults) {
////                    if (productionPickListsCartResult.getBrandIds().stream().anyMatch(i -> i.equals(brandResult.getBrandId()))) {
////                        brandNames.add(brandResult.getBrandName());
////                    }
////                }
////            }
////            brandNames = brandNames.stream().distinct().collect(Collectors.toList());
////            productionPickListsCartResult.setBrandNames(brandNames);
////        }
////    }
////
////
//    private Serializable getKey(RestOutStockCartParam param) {
//        return param.getPickListsCart();
//    }
////
//    private Page<RestOutStockCartResult> getPageContext() {
//        return PageFactory.defaultPage();
//    }
//
//    private RestOutStockCart getOldEntity(RestOutStockCartParam param) {
//        return this.getById(getKey(param));
//    }
//
//    private RestOutStockCart getEntity(RestOutStockCartParam param) {
//        RestOutStockCart entity = new RestOutStockCart();
//        ToolUtil.copyProperties(param, entity);
//        return entity;
//    }
////
////    @Override
////    public List<CartGroupByUserListRequest> groupByUser(RestOutStockCartParam param) {
////
////        List<ProductionPickLists> productionPickLists = param.getPickListsIds().size() == 0 ? new ArrayList<>() : pickListsService.listByIds(param.getPickListsIds());
////        List<ProductionPickListsResult> pickListsResults = new ArrayList<>();
////        List<Long> userIds = new ArrayList<>();
////        List<Long> pickListsId = new ArrayList<>();
////        for (ProductionPickLists productionPickList : productionPickLists) {
////            ProductionPickListsResult productionPickListsResult = new ProductionPickListsResult();
////            ToolUtil.copyProperties(productionPickList, productionPickListsResult);
////            pickListsResults.add(productionPickListsResult);
////            userIds.add(productionPickList.getUserId());
////            pickListsId.add(productionPickList.getPickListsId());
////        }
////
////        pickListsService.format(pickListsResults);
////        List<RestOutStockCart> pickListsCart = pickListsId.size() == 0 ? new ArrayList<>() : this.query().in("pick_lists_id", pickListsId).list();
////        List<RestOutStockCartResult> results = new ArrayList<>();
////        for (RestOutStockCart productionPickListsCart : pickListsCart) {
////            RestOutStockCartResult result = new RestOutStockCartResult();
////            ToolUtil.copyProperties(productionPickListsCart, result);
////            results.add(result);
////        }
////
////        this.format(results);
////
////
////        List<UserResult> userResultsByIds = userService.getUserResultsByIds(userIds);
////        List<CartGroupByUserListRequest> requests = new ArrayList<>();
////        for (UserResult userResult : userResultsByIds) {
////            CartGroupByUserListRequest request = new CartGroupByUserListRequest();
////            request.setName(userResult.getName());
////            request.setUserId(userResult.getUserId());
////            List<RestOutStockCartResult> pickListsCartAll = new ArrayList<>();
////            for (ProductionPickListsResult pickListsResult : pickListsResults) {
////                if (userResult.getUserId().equals(pickListsResult.getUserId())) {
////                    List<RestOutStockCartResult> pickListsCartResults = new ArrayList<>();
////                    for (RestOutStockCartResult result : results) {
////                        if (result.getPickListsId().equals(pickListsResult.getPickListsId())) {
////                            pickListsCartResults.add(result);
////                            result.setProductionPickListsResult(pickListsResult);
////                        }
////                    }
////
////                    pickListsCartAll.addAll(pickListsCartResults);
////                }
////            }
////            request.setCartResults(pickListsCartAll);
////            requests.add(request);
////        }
////
////
////        return requests;
////    }
////
////    @Override
////    public List<RestOutStockCartResult> getSelfCarts(RestOutStockCartParam param) {
////        List<ProductionPickLists> productionPickLists = pickListsService.query().eq("user_id", LoginContextHolder.getContext().getUserId()).list();
////        List<Long> pickListsIds = new ArrayList<>();
////        for (ProductionPickLists productionPickList : productionPickLists) {
////            pickListsIds.add(productionPickList.getPickListsId());
////        }
////        List<RestOutStockCart> pickListsCarts = pickListsIds.size() == 0 ? new ArrayList<>() : this.query().in("pick_lists_id", pickListsIds).list();
////        List<RestOutStockCartResult> results = new ArrayList<>();
////        for (RestOutStockCart pickListsCart : pickListsCarts) {
////            RestOutStockCartResult result = new RestOutStockCartResult();
////            ToolUtil.copyProperties(pickListsCart, result);
////            results.add(result);
////        }
////        this.format(results);
////        return results;
////    }
////
////    @Override
////    public List<RestOutStockOrderDetailResult> getSelfCartsByLists(Long pickListsId) {
////
////        if (BeanUtil.isNotEmpty(pickListsId)) {
////            List<RestOutStockOrderDetail> listsDetails = pickListsDetailService.query().eq("pick_lists_id", pickListsId).ne("status", 99).eq("display", 1).list();
////            List<RestOutStockOrderDetailResult> pickListsDetailResults = BeanUtil.copyToList(listsDetails, RestOutStockOrderDetailResult.class);
////            pickListsDetailService.format(pickListsDetailResults);
////            return pickListsDetailResults;
////        }
////        return null;
////
////    }
////
////    @Override
////    public List<Map<String, Object>> getSelfCartsBySku(RestOutStockCartParam productionPickListsCartParam) {
////        List<Long> skuIds = new ArrayList<>();
////        List<Long> pickListsIds = new ArrayList<>();
////        List<RestOutStockCartResult> productionPickListsCartResults = this.baseMapper.selfCartList(productionPickListsCartParam, LoginContextHolder.getContext().getUserId());
////        for (RestOutStockCartResult pickListsCart : productionPickListsCartResults) {
////            skuIds.add(pickListsCart.getSkuId());
////            pickListsIds.add(pickListsCart.getPickListsId());
////        }
////
////        List<ProductionPickLists> productionPickLists = pickListsIds.size() == 0 ? new ArrayList<>() : pickListsService.listByIds(pickListsIds);
////        List<ProductionPickListsResult> pickListsResults = BeanUtil.copyToList(productionPickLists, ProductionPickListsResult.class, new CopyOptions());
////        List<Long> userIds = new ArrayList<>();
////        for (ProductionPickListsResult pickListsResult : pickListsResults) {
////            pickListsIds.add(pickListsResult.getPickListsId());
////            userIds.add(pickListsResult.getCreateUser());
////        }
////        List<UserResult> userResults = userService.getUserResultsByIds(userIds);
////        for (ProductionPickListsResult pickListsResult : pickListsResults) {
////            for (UserResult userResult : userResults) {
////                if (pickListsResult.getCreateUser().equals(userResult.getUserId())) {
////                    pickListsResult.setCreateUserResult(userResult);
////                }
////
////            }
////        }
////
////        List<RestOutStockCartResult> cartTotalResults = new ArrayList<>();
////
////        productionPickListsCartResults.parallelStream().collect(Collectors.groupingBy(item -> item.getSkuId() + "_" + item.getPickListsId() + "_" + item.getStorehouseId(), Collectors.toList())).forEach(
////                (id, transfer) -> {
////                    transfer.stream().reduce((a, b) -> new RestOutStockCartResult() {{
////                        setSkuId(a.getSkuId());
////                        setNumber(a.getNumber() + b.getNumber());
////                        setBrandId(a.getBrandId());
////                        setPickListsId(a.getPickListsId());
////                        setPickListsDetailId(a.getPickListsDetailId());
////                        setStorehouseId(a.getStorehouseId());
////                    }}).ifPresent(cartTotalResults::add);
////                }
////        );
////
////
////        List<RestOutStockOrderDetail> details = pickListsIds.size() == 0 ? new ArrayList<>() : pickListsDetailService.query().in("pick_lists_id", pickListsIds).eq("status", 0).eq("display", 1).list();
////        List<RestOutStockOrderDetailResult> detailResults = BeanUtil.copyToList(details, RestOutStockOrderDetailResult.class);
////
////        List<RestOutStockOrderDetailResult> detailTotalList = new ArrayList<>();
////        for (RestOutStockOrderDetailResult pickListsDetailResult : detailTotalList) {
////            for (ProductionPickListsResult pickListsResult : pickListsResults) {
////                if (pickListsResult.getPickListsId().equals(pickListsDetailResult.getPickListsId())) {
////                    pickListsDetailResult.setPickListsResult(pickListsResult);
////                }
////            }
////        }
////        detailResults.parallelStream().collect(Collectors.groupingBy(item -> item.getSkuId() + "_" + item.getPickListsId(), Collectors.toList())).forEach(
////                (id, transfer) -> {
////                    transfer.stream().reduce((a, b) -> new RestOutStockOrderDetailResult() {{
////                        setPickListsId(a.getPickListsId());
////                        setSkuId(a.getSkuId());
////                        setNumber(a.getNumber() + b.getNumber());
////                        setStorehouseId(a.getStorehouseId());
////                    }}).ifPresent(detailTotalList::add);
////                }
////        );
////
////        for (RestOutStockOrderDetailResult detailResult : detailTotalList) {
////            for (ProductionPickListsResult pickListsResult : pickListsResults) {
////                if (detailResult.getPickListsId().equals(pickListsResult.getPickListsId())) {
////                    detailResult.setPickListsResult(pickListsResult);
////                }
////            }
////        }
////
////        List<SkuSimpleResult> skuSimpleResults = skuIds.size() == 0 ? new ArrayList<>() : skuService.simpleFormatSkuResult(skuIds);
////
////        //返回对象
////        List<Map<String, Object>> results = new ArrayList<>();
////
////        Map<String, Object> result = new HashMap<>();
////        List<Long> storehouseSkuIds = new ArrayList<>();
////
////        for (RestOutStockCartResult storehouseCart : cartTotalResults) {
////            for (RestOutStockOrderDetailResult pickListsDetailResult : detailTotalList) {
////                if (storehouseCart.getPickListsId().equals(pickListsDetailResult.getPickListsId())) {
////                    storehouseCart.setRestOutStockOrderDetailResult(pickListsDetailResult);
////                }
////            }
////        }
////        List<Map<String, Object>> skuMapResults = new ArrayList<>();
////        for (SkuSimpleResult skuSimpleResult : skuSimpleResults) {
////            if (skuIds.stream().anyMatch(i -> i.equals(skuSimpleResult.getSkuId()))) {
////                Map<String, Object> map = BeanUtil.beanToMap(skuSimpleResult);
////                List<RestOutStockCartResult> cartResults = new ArrayList<>();
////                for (RestOutStockCartResult storehouseCart : cartTotalResults) {
////                    if (storehouseCart.getSkuId().equals(skuSimpleResult.getSkuId())) {
////                        cartResults.add(storehouseCart);
////                    }
////                }
////                map.put("cartResults", cartResults);
////                skuMapResults.add(map);
////            }
////        }
////        result.put("skuResults", skuMapResults);
////        results.add(result);
////
////        return results;
////    }
////
////    @Override
////    public void deleteBatchByIds(List<RestOutStockCartParam> cartParams) {
////        List<Long> pickListsIds = new ArrayList<>();
////
////
////        for (RestOutStockCartParam cartParam : cartParams) {
////            pickListsIds.add(cartParam.getPickListsId());
////        }
////        List<RestOutStockCart> list = pickListsIds.size() == 0 ? new ArrayList<>() : this.query().in("pick_lists_id", pickListsIds).eq("display", 1).isNull("type").list();
////        List<Long> inkindIds = new ArrayList<>();
////        for (RestOutStockCart pickListsCart : list) {
////            inkindIds.add(pickListsCart.getInkindId());
////        }
////
////        List<Inkind> inkinds = inkindIds.size() == 0 ? new ArrayList<>() :inkindService.listByIds(inkindIds);
////        List<Long> parentInkindIds = new ArrayList<>();
////        for (Inkind inkind : inkinds) {
////            if (inkind.getSource().equals("Inkind")) {
////                parentInkindIds.add(inkind.getSourceId());
////            }
////        }
////        List<StockDetails> stockDetails = inkindIds.size() == 0 ? new ArrayList<>() : stockDetailsService.query().in("inkind_id", inkindIds).eq("display", 1).list();
////        List<StockDetails> parentStockDetails = parentInkindIds.size() == 0 ? new ArrayList<>() : stockDetailsService.query().in("inkind_id", parentInkindIds).eq("display", 1).list();
////
////
////
////        List<RestOutStockCart> updateEntity = new ArrayList<>();
////        for (RestOutStockCartParam cartParam : cartParams) {
////            for (RestOutStockCart pickListsCart : list) {
////                if (cartParam.getSkuId().equals(pickListsCart.getSkuId()) && cartParam.getPickListsId().equals(pickListsCart.getPickListsId()) && cartParam.getBrandId().equals(pickListsCart.getBrandId())) {
////                    pickListsCart.setStatus(-1);
////                    pickListsCart.setDisplay(0);
////                    updateEntity.add(pickListsCart);
////                    for (StockDetails stockDetail : stockDetails) {
////                        if (stockDetail.getInkindId().equals(pickListsCart.getInkindId())){
////                            for (Inkind inkind : inkinds) {
////                                for (StockDetails parentStockDetail : parentStockDetails) {
////                                    if (stockDetail.getInkindId().equals(inkind.getInkindId()) && inkind.getSource().equals("Inkind") && inkind.getSourceId().equals(parentStockDetail.getInkindId()) && parentStockDetail.getDisplay().equals(1) && stockDetail.getDisplay().equals(1)) {
////                                        parentStockDetail.setNumber(stockDetail.getNumber() + parentStockDetail.getNumber());
////                                        if (parentStockDetail.getDisplay().equals(0) && parentStockDetail.getStage().equals(2)){
////                                            parentStockDetail.setDisplay(1);
////                                            parentStockDetail.setStage(1);
////                                            parentStockDetail.setNumber(stockDetail.getNumber());
////                                        }
////                                        stockDetail.setNumber(0L);
////                                        stockDetail.setDisplay(0);
////                                        stockDetail.setStage(2);
////                                    }
////                                }
////                            }
////
////                        }
////                    }
////                }
////            }
////        }
////        stockDetailsService.updateBatchById(stockDetails);
////        stockDetailsService.updateBatchById(parentStockDetails);
//////        shopCartService.addDynamic(pickListsIds.get(0), inkinds.get(0).getSkuId(),"领取了物料 "+skuService.skuMessage(inkinds.get(0).getSkuId()));
////        this.updateBatchById(updateEntity);
////    }
////
////    @Override
////    public List<RestOutStockCartResult> listByListsDetailIds(List<Long> listsDetailIds) {
////        if (ToolUtil.isEmpty(listsDetailIds) || listsDetailIds.size() == 0) {
////            return new ArrayList<>();
////        }
////        listsDetailIds = listsDetailIds.stream().distinct().collect(Collectors.toList());
////        List<RestOutStockCart> list = this.query().in("pick_lists_detail_id", listsDetailIds).eq("status", 0).eq("display", 1).list();
////        List<RestOutStockCartResult> productionPickListsCartResults = BeanUtil.copyToList(list, RestOutStockCartResult.class, new CopyOptions());
////        this.format(productionPickListsCartResults);
////        return productionPickListsCartResults;
////    }
////
////    @Override
////    public List<StockDetails> getLockStockDetail() {
////        List<Long> inkindIds = this.baseMapper.lockInkind();
////
////        List<StockDetails> stockDetails = inkindIds.size() == 0 ? new ArrayList<>() : stockDetailsService.query().in("inkind_id", inkindIds).eq("display", 1).list();
////        List<StockDetails> totalLockDetail = new ArrayList<>();
////        stockDetails.parallelStream().collect(Collectors.groupingBy(StockDetails::getSkuId, Collectors.toList())).forEach(
////                (id, transfer) -> {
////                    transfer.stream().reduce((a, b) -> new StockDetails() {{
////                        setSkuId(a.getSkuId());
////                        setNumber(a.getNumber() + b.getNumber());
////                    }}).ifPresent(totalLockDetail::add);
////                }
////        );
////        return totalLockDetail;
////
////    }
////    @Override
////    public List<Long> getLockedInkindIds(){
////        return this.baseMapper.lockInkind();
////    }
////    @Override
////    public Integer getLockNumber(QuerryLockedParam param) {
////        return this.baseMapper.lockNumber(param);
////    }
////
//    @Override
//    public List<Long> getCartInkindIds(List<Long> skuIds) {
//        List<Long> inkindIds = new ArrayList<>();
//
//        List<RestOutStockCart> listsCarts = skuIds.size() == 0 ? new ArrayList<>() : this.query().in("sku_id", skuIds).eq("display", 1).list();
//        for (RestOutStockCart listsCart : listsCarts) {
//            inkindIds.add(listsCart.getInkindId());
//        }
//        return inkindIds;
//    }
////
////    @Override
////    public List<PickListsStorehouseResult> listPickListsStorehouse(ProductionPickListsParam param) {
////        //查询符合条件主表id 用主表id查询所有购物车中物品
////        List<Long> ids = pickListsService.idsList(new ProductionPickListsParam() {{
////            setUserId(LoginContextHolder.getContext().getUserId());
////        }});
////
////        List<RestOutStockCart> pickListsCarts = ids.size() == 0 ? new ArrayList<>() : this.query().in("pick_lists_id", ids).eq("status", 0).list();
////        List<Long> storehouseIds = new ArrayList<>();
////        List<Long> skuIds = new ArrayList<>();
////        for (RestOutStockCart pickListsCart : pickListsCarts) {
////            storehouseIds.add(pickListsCart.getStorehouseId());
////            skuIds.add(pickListsCart.getSkuId());
////        }
////        storehouseIds = storehouseIds.stream().distinct().collect(Collectors.toList());
////        skuIds = skuIds.stream().distinct().collect(Collectors.toList());
////        List<SkuResult> skuResults = skuService.formatSkuResult(skuIds);
////        List<Storehouse> storehouses = storehouseIds.size() == 0 ? new ArrayList<>() : storehouseService.listByIds(storehouseIds);
////
////        List<StorehouseResult> storehouseResults = BeanUtil.copyToList(storehouses, StorehouseResult.class);
////        List<PickListsStorehouseResult> results = new ArrayList<>();
////        for (StorehouseResult storehouseResult : storehouseResults) {
////            PickListsStorehouseResult result = new PickListsStorehouseResult();
////            result.setStorehouseResult(storehouseResult);
////            List<RestOutStockCart> storehouseAndSku = new ArrayList<>();
////            pickListsCarts.parallelStream().collect(Collectors.groupingBy(item -> item.getSkuId() + '_' + item.getStorehouseId(), Collectors.toList())).forEach(
////                    (id, transfer) -> {
////                        transfer.stream().reduce((a, b) -> new RestOutStockCart() {{
////                            setSkuId(a.getSkuId());
////                            setNumber(a.getNumber() + b.getNumber());
////                            setStorehouseId(a.getStorehouseId());
////                        }}).ifPresent(storehouseAndSku::add);
////                    }
////            );
////            int number = 0;
////            int skuCount = 0;
////            List<SkuResult> skuTotal = new ArrayList<>();
////            for (RestOutStockCart pickListsCart : storehouseAndSku) {
////                if (pickListsCart.getStorehouseId().equals(storehouseResult.getStorehouseId())) {
////                    number += pickListsCart.getNumber();
////                    skuCount += 1;
////                    for (SkuResult skuResult : skuResults) {
////                        if (pickListsCart.getSkuId().equals(skuResult.getSkuId())) {
////                            skuTotal.add(skuResult);
////                        }
////                    }
////                }
////            }
////            result.setSkuCount(skuCount);
////            result.setNumberCount(number);
////            List<SpuClassificationResult> spuClassificationResults = new ArrayList<>();
////            for (SkuResult skuResult : skuTotal) {
////                spuClassificationResults.add(skuResult.getSpuResult().getSpuClassificationResult());
////            }
////
////            List<SpuClassificationResult> totalSpuClassificationResults = new ArrayList<>();
////            spuClassificationResults.parallelStream().collect(Collectors.groupingBy(item -> item.getSpuClassificationId(), Collectors.toList())).forEach(
////                    (id, transfer) -> {
////                        transfer.stream().reduce((a, b) -> a).ifPresent(totalSpuClassificationResults::add);
////                    }
////            );
////            List<PickListsStorehouseResult.ClassCount> classCounts = new ArrayList<>();
////            for (SpuClassificationResult totalSpuClassificationResult : totalSpuClassificationResults) {
////                PickListsStorehouseResult.ClassCount classCount = new PickListsStorehouseResult.ClassCount();
////                classCount.setClassName(totalSpuClassificationResult.getName());
////                int classNumberCount = 0;
////                int skuNumberCount = 0;
////                for (SkuResult skuResult : skuTotal) {
////                    for (RestOutStockCart pickListsCart : storehouseAndSku) {
////                        if (totalSpuClassificationResult.getSpuClassificationId().equals(skuResult.getSpuResult().getSpuClassificationId()) && pickListsCart.getSkuId().equals(skuResult.getSkuId())) {
////                            skuNumberCount += 1;
////                            classNumberCount += pickListsCart.getNumber();
////                        }
////                    }
////                }
////                classCount.setNumberCount(classNumberCount);
////                classCount.setClassCount(skuNumberCount);
////                classCounts.add(classCount);
////            }
////            result.setClassCounts(classCounts);
////            results.add(result);
////
////        }
////        return results;
////    }
////
////    @Override
////    public List<StockDetailsResult> getCartInkindByLists(RestOutStockCartParam param) {
////
////
////        List<RestOutStockCart> list = this.query().eq("pick_lists_Id", param.getPickListsId()).eq("brand_id", param.getBrandId()).eq("sku_id", param.getSkuId()).eq("display", 1).eq("status", 0).list();
////
////        List<StockDetailsResult> stockDetailsResults = BeanUtil.copyToList(list, StockDetailsResult.class);
////        stockDetailsService.format(stockDetailsResults);
////        return stockDetailsResults;
////    }
}
