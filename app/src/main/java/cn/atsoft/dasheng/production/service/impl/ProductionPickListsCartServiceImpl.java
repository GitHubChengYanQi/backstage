package cn.atsoft.dasheng.production.service.impl;


import cn.atsoft.dasheng.app.entity.StockDetails;
import cn.atsoft.dasheng.app.entity.Storehouse;
import cn.atsoft.dasheng.app.model.result.BrandResult;
import cn.atsoft.dasheng.app.model.result.StorehouseResult;
import cn.atsoft.dasheng.app.model.result.StorehouseSimpleResult;
import cn.atsoft.dasheng.app.pojo.StockSkuBrand;
import cn.atsoft.dasheng.app.service.BrandService;
import cn.atsoft.dasheng.app.service.StockDetailsService;
import cn.atsoft.dasheng.app.service.StorehouseService;
import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.Inkind;
import cn.atsoft.dasheng.erp.entity.StorehousePositions;
import cn.atsoft.dasheng.erp.model.result.*;
import cn.atsoft.dasheng.erp.service.ShopCartService;
import cn.atsoft.dasheng.erp.service.SkuService;
import cn.atsoft.dasheng.erp.service.StorehousePositionsService;
import cn.atsoft.dasheng.form.entity.ActivitiProcessTask;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.production.entity.ProductionPickLists;
import cn.atsoft.dasheng.production.entity.ProductionPickListsCart;
import cn.atsoft.dasheng.production.entity.ProductionPickListsDetail;
import cn.atsoft.dasheng.production.mapper.ProductionPickListsCartMapper;
import cn.atsoft.dasheng.production.model.params.ProductionPickListsCartParam;
import cn.atsoft.dasheng.production.model.params.ProductionPickListsDetailParam;
import cn.atsoft.dasheng.production.model.params.ProductionPickListsParam;
import cn.atsoft.dasheng.production.model.request.CartGroupByUserListRequest;
import cn.atsoft.dasheng.production.model.result.PickListsStorehouseResult;
import cn.atsoft.dasheng.production.model.result.ProductionPickListsCartResult;
import cn.atsoft.dasheng.production.model.result.ProductionPickListsDetailResult;
import cn.atsoft.dasheng.production.model.result.ProductionPickListsResult;
import cn.atsoft.dasheng.production.service.ProductionPickListsCartService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.production.service.ProductionPickListsDetailService;
import cn.atsoft.dasheng.production.service.ProductionPickListsService;
import cn.atsoft.dasheng.sys.modular.system.model.result.UserResult;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
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
public class ProductionPickListsCartServiceImpl extends ServiceImpl<ProductionPickListsCartMapper, ProductionPickListsCart> implements ProductionPickListsCartService {


    @Autowired
    private SkuService skuService;

    @Autowired
    private ProductionPickListsService pickListsService;

    @Autowired
    private UserService userService;
    @Autowired
    private StockDetailsService stockDetailsService;

    @Autowired
    private ProductionPickListsDetailService pickListsDetailService;

    @Autowired
    private BrandService brandService;

    @Autowired
    private StorehouseService storehouseService;

    @Autowired
    private ShopCartService shopCartService;
    @Autowired
    private StorehousePositionsService storehousePositionsService;


    @Override
    public void add(ProductionPickListsCartParam param, List<StockDetails> stockDetails) {

        /**
         * 过滤实物
         */
        List<ProductionPickListsCart> entitys = new ArrayList<>();
        for (ProductionPickListsCartParam productionPickListsCartParam : param.getProductionPickListsCartParams()) {
            if (ToolUtil.isEmpty(productionPickListsCartParam.getBrandId())) {
                productionPickListsCartParam.setBrandId(0L);
            }
            if (stockDetails.size() == 0) {
                throw new ServiceException(500, "该库位库存不足");
            } else {
                int count = 0;
                for (StockDetails stockDetail : stockDetails) {
                    count += stockDetail.getNumber();
                }
                if (productionPickListsCartParam.getNumber() > count) {
                    throw new ServiceException(500, "该库位库存不足");
                }
            }
            int number = productionPickListsCartParam.getNumber();

            if (ToolUtil.isNotEmpty(productionPickListsCartParam.getInkindId())) {
                ProductionPickListsCart entity = new ProductionPickListsCart();
                entity.setPickListsId(productionPickListsCartParam.getPickListsId());
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
                for (StockDetails stockDetail : stockDetails) {
                    if (number > 0) {
                        if ((ToolUtil.isNotEmpty(stockDetail.getBrandId()) && stockDetail.getBrandId().equals(productionPickListsCartParam.getBrandId()) && stockDetail.getSkuId().equals(productionPickListsCartParam.getSkuId()) && stockDetail.getStorehousePositionsId().equals(productionPickListsCartParam.getStorehousePositionsId())) || ((ToolUtil.isEmpty(stockDetail.getBrandId()) || stockDetail.getBrandId().equals(0L)) && stockDetail.getSkuId().equals(productionPickListsCartParam.getSkuId()) && stockDetail.getStorehousePositionsId().equals(productionPickListsCartParam.getStorehousePositionsId()))) {
                            int lastNumber = number;
                            number -= stockDetail.getNumber();
                            if (number >= 0) {
                                ProductionPickListsCart entity = new ProductionPickListsCart();
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
                                entitys.add(entity);
                            } else {
                                ProductionPickListsCart entity = new ProductionPickListsCart();
                                entity.setNumber(lastNumber);
                                entity.setSkuId(stockDetail.getSkuId());
                                if (ToolUtil.isNotEmpty(stockDetail.getBrandId())) {
                                    entity.setBrandId(stockDetail.getBrandId());
                                }
                                entity.setStorehousePositionsId(productionPickListsCartParam.getStorehousePositionsId());
                                entity.setStorehouseId(productionPickListsCartParam.getStorehouseId());
                                entity.setPickListsId(productionPickListsCartParam.getPickListsId());
                                entity.setPickListsDetailId(productionPickListsCartParam.getPickListsDetailId());
                                entity.setType(productionPickListsCartParam.getType());
                                entity.setInkindId(stockDetail.getInkindId());
                                entitys.add(entity);
                            }

                        }
                    }
                }
            }
        }
        if (entitys.size() > 0) {
            this.saveBatch(entitys);
            String skuName = skuService.skuMessage(entitys.get(0).getSkuId());
            shopCartService.addDynamic(entitys.get(0).getPickListsId(), skuName + "进行了备料");
        } else {
            throw new ServiceException(500, "未匹配到所需物料,备料失败");
        }

    }


    /**
     * 库存预警
     */
    @Override
    public boolean warning(ProductionPickListsCartParam param) {

        List<StockSkuBrand> stockSkuBrands = stockDetailsService.stockSkuBrands();
        if (stockSkuBrands.size() == 0) {
            throw new ServiceException(500, "库存数不足");
        }

        //申请的物料跟库存数比较  并更新库存
        for (ProductionPickListsCartParam productionPickListsCartParam : param.getProductionPickListsCartParams()) {
            updateStock(productionPickListsCartParam, stockSkuBrands);
        }


        Map<Integer, List<ActivitiProcessTask>> map = pickListsService.unExecuted(param.getTaskId());
        List<ActivitiProcessTask> executed = map.get(99);  // 到执行节点的

        List<Long> pickListsIds = new ArrayList<>();
        for (ActivitiProcessTask processTask : executed) {
            pickListsIds.add(processTask.getFormId());
        }

        List<ProductionPickListsDetail> pickListsDetails = pickListsIds.size() == 0 ? new ArrayList<>() : pickListsDetailService.query().in("pick_lists_id", pickListsIds)
                .eq("display", 1)
                .eq("status", 0).list();

        List<ProductionPickListsDetailParam> pickListsDetailParams = BeanUtil.copyToList(pickListsDetails, ProductionPickListsDetailParam.class, new CopyOptions());


        for (ProductionPickListsDetailParam pickListsDetailParam : pickListsDetailParams) {
            for (ProductionPickListsCartParam listParam : param.getProductionPickListsCartParams()) {
                if (listParam.getSkuId().equals(pickListsDetailParam.getSkuId()) && listParam.getBrandId().equals(pickListsDetailParam.getBrandId())) {
                    if (pickListsService.updateStock(pickListsDetailParam, stockSkuBrands)) {
                        return true;
                    }

                }
            }
        }

//        for (ProductionPickListsDetailParam listsParam : pickListsDetailParams) {
//
//        }
        return false;
    }

    /**
     * 更新库存
     *
     * @param detailParam
     * @param stockSkuBrands
     */
    private void updateStock(ProductionPickListsCartParam detailParam, List<StockSkuBrand> stockSkuBrands) {

        for (StockSkuBrand stockSkuBrand : stockSkuBrands) {
            if ((ToolUtil.isEmpty(detailParam.getBrandId()) || detailParam.getBrandId() == 0) && detailParam.getSkuId().equals(stockSkuBrand.getSkuId())) {  //不指定品牌
                long number = stockSkuBrand.getNumber() - detailParam.getNumber();

                stockSkuBrand.setNumber(number);

            } else if (ToolUtil.isNotEmpty(detailParam.getBrandId()) && detailParam.getBrandId().equals(stockSkuBrand.getBrandId()) && detailParam.getSkuId().equals(stockSkuBrand.getSkuId())) {  //指定品牌
                long number = stockSkuBrand.getNumber() - detailParam.getNumber();

                stockSkuBrand.setNumber(number);
                break;
            }
        }

    }

    @Override
    public void addCheck(ProductionPickListsCartParam param) {
        List<Long> detailIds = new ArrayList<>();
        for (ProductionPickListsCartParam productionPickListsCartParam : param.getProductionPickListsCartParams()) {
            detailIds.add(productionPickListsCartParam.getPickListsDetailId());
        }
        if (ToolUtil.isEmpty(detailIds) || detailIds.size() == 0) {
            throw new ServiceException(500, "请选中备料信息");
        }
        List<Long> inkindIds = new ArrayList<>();
        List<ProductionPickListsDetail> details = pickListsDetailService.listByIds(detailIds);
        for (ProductionPickListsCartParam productionPickListsCartParam : param.getProductionPickListsCartParams()) {
            for (ProductionPickListsDetail listsDetail : details) {
                if (productionPickListsCartParam.getPickListsDetailId().equals(listsDetail.getPickListsDetailId())) {
                    if (!listsDetail.getBrandId().equals(productionPickListsCartParam.getBrandId()) && !listsDetail.getBrandId().equals(0L)) {
                        throw new ServiceException(500, "请选择对应品牌");
                    }
                    if (!listsDetail.getSkuId().equals(productionPickListsCartParam.getSkuId())) {
                        throw new ServiceException(500, "请选择这对应物料");
                    }
                    if (listsDetail.getNumber() - listsDetail.getReceivedNumber() < productionPickListsCartParam.getNumber()) {
                        throw new ServiceException(500, "备料数量超出申请数量");
                    }

                }
            }
            if (ToolUtil.isNotEmpty(productionPickListsCartParam.getInkindId())) {
                inkindIds.add(productionPickListsCartParam.getInkindId());
            }
        }
        List<StockDetails> stockDetails = inkindIds.size() == 0 ? new ArrayList<>() : stockDetailsService.query().in("inkind_id", inkindIds).eq("display", 1).list();
        for (ProductionPickListsCartParam productionPickListsCartParam : param.getProductionPickListsCartParams()) {
            for (StockDetails stockDetail : stockDetails) {
                if (ToolUtil.isNotEmpty(productionPickListsCartParam.getInkindId()) && stockDetail.getInkindId().equals(productionPickListsCartParam.getInkindId()) && stockDetail.getNumber() > productionPickListsCartParam.getNumber()) {
                    throw new ServiceException(500, "库存中实物数量小于您填入的数量");
                }
            }
        }

    }


    /**
     * 检查库存中实物是否可以被重复使用
     *
     * @param param
     */
    private List<StockDetails> foundCanBeUseStockDetail(ProductionPickListsCartParam param) {
        //查询出所有的购物车  获取实物  下面会检查库存实物是否满足再次被使用
        List<Long> skuIds = new ArrayList<>();
        for (ProductionPickListsCartParam productionPickListsCartParam : param.getProductionPickListsCartParams()) {
            skuIds.add(productionPickListsCartParam.getSkuId());
        }
        List<ProductionPickListsCart> cartList = this.query().eq("display", 1).list();
        List<Long> inkindIds = new ArrayList<>();
        for (ProductionPickListsCart pickListsCart : cartList) {
            if (ToolUtil.isNotEmpty(pickListsCart.getInkindId())) {
                inkindIds.add(pickListsCart.getInkindId());
            }
        }

        List<StockDetails> beUseStockDetail = stockDetailsService.query().eq("stage", 1).in("sku_id", skuIds).eq("display", 1).list();
        for (int i = 0; i < beUseStockDetail.size(); ) {
            i++;
            for (ProductionPickListsCart pickListsCart : cartList) {
                if (beUseStockDetail.get(i).getInkindId().equals(pickListsCart.getInkindId())) {
                    Long num = beUseStockDetail.get(i).getNumber() - pickListsCart.getNumber();
                    if (num > 0) {
                        beUseStockDetail.get(i).setNumber(num);

                    } else {
                        beUseStockDetail.remove(i);
                        i -= 1;
                    }
                }
            }
        }
        return beUseStockDetail;
    }

    @Override
    public void delete(ProductionPickListsCartParam param) {
        ProductionPickListsCart pickListsCart = this.getById(param.getPickListsCart());

        if (ToolUtil.isNotEmpty(pickListsCart.getType()) && pickListsCart.getType().equals("frmLoss")) {
            throw new ServiceException(500, "报损物料 不可退回");
        }
        this.removeById(getKey(param));
    }

    @Override
    public void update(ProductionPickListsCartParam param) {
        ProductionPickListsCart oldEntity = getOldEntity(param);
        ProductionPickListsCart newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public ProductionPickListsCartResult findBySpec(ProductionPickListsCartParam param) {
        return null;
    }

    @Override
    public List<ProductionPickListsCartResult> findListBySpec(ProductionPickListsCartParam param) {
        List<ProductionPickListsCartResult> productionPickListsCartResults = this.baseMapper.customList(param);
        this.format(productionPickListsCartResults);

        return productionPickListsCartResults;
    }

    @Override
    public PageInfo<ProductionPickListsCartResult> findPageBySpec(ProductionPickListsCartParam param) {
        Page<ProductionPickListsCartResult> pageContext = getPageContext();
        IPage<ProductionPickListsCartResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    @Override
    public void format(List<ProductionPickListsCartResult> param) {
        List<Long> skuIds = new ArrayList<>();
        List<Long> storehouseIds = new ArrayList<>();
        List<Long> positionIds = new ArrayList<>();
        List<Long> listsIds = new ArrayList<>();
        List<Long> detailIds = new ArrayList<>();
        List<Long> brandIds = new ArrayList<>();
        for (ProductionPickListsCartResult productionPickListsCartResult : param) {
            skuIds.add(productionPickListsCartResult.getSkuId());
            storehouseIds.add(productionPickListsCartResult.getStorehouseId());
            positionIds.add(productionPickListsCartResult.getStorehousePositionsId());
            listsIds.add(productionPickListsCartResult.getPickListsId());
            detailIds.add(productionPickListsCartResult.getPickListsDetailId());
            brandIds.add(productionPickListsCartResult.getBrandId());
        }
        /**
         * ids去重
         */
        skuIds = skuIds.stream().distinct().collect(Collectors.toList());
        storehouseIds = storehouseIds.stream().distinct().collect(Collectors.toList());
        detailIds = detailIds.stream().distinct().collect(Collectors.toList());

        /**
         * 查询数据
         */

        List<StorehouseSimpleResult> storehouseResults = storehouseIds.size() == 0 ? new ArrayList<>() : BeanUtil.copyToList(storehouseService.listByIds(storehouseIds), StorehouseSimpleResult.class, new CopyOptions());
        List<StorehousePositionsResult> storehousePositionsResults = positionIds.size() == 0 ? new ArrayList<>() : BeanUtil.copyToList(storehousePositionsService.listByIds(positionIds), StorehousePositionsResult.class);
        List<ProductionPickListsResult> pickListsResults = listsIds.size() == 0 ? new ArrayList<>() : BeanUtil.copyToList(pickListsService.listByIds(listsIds), ProductionPickListsResult.class);
        List<SkuSimpleResult> skuSimpleResults = skuService.simpleFormatSkuResult(skuIds);
        List<ProductionPickListsDetailResult> pickListsDetailResults = detailIds.size() == 0 ? new ArrayList<>() : BeanUtil.copyToList(pickListsDetailService.listByIds(detailIds), ProductionPickListsDetailResult.class);
        List<BrandResult> brandResults = brandService.getBrandResults(brandIds);
//        pickListsDetailService.format(pickListsDetailResults);
        for (ProductionPickListsCartResult productionPickListsCartResult : param) {
            for (SkuSimpleResult skuResult : skuSimpleResults) {
                if (productionPickListsCartResult.getSkuId().equals(skuResult.getSkuId())) {
                    productionPickListsCartResult.setSkuResult(skuResult);
                    break;
                }
            }
            for (StorehouseSimpleResult storehouseResult : storehouseResults) {
                if (storehouseResult.getStorehouseId().equals(productionPickListsCartResult.getStorehouseId())) {
                    productionPickListsCartResult.setStorehouseResult(storehouseResult);
                    break;
                }
            }
            for (StorehousePositionsResult storehousePositionsResult : storehousePositionsResults) {
                if (storehousePositionsResult.getStorehousePositionsId().equals(productionPickListsCartResult.getStorehousePositionsId())) {
                    productionPickListsCartResult.setStorehousePositionsResult(storehousePositionsResult);
                    break;
                }
            }
            for (ProductionPickListsResult pickListsResult : pickListsResults) {
                if (productionPickListsCartResult.getPickListsId().equals(pickListsResult.getPickListsId())) {
                    productionPickListsCartResult.setPickListsResult(pickListsResult);
                    break;
                }
            }
            for (ProductionPickListsDetailResult pickListsDetailResult : pickListsDetailResults) {
                if (productionPickListsCartResult.getPickListsDetailId().equals(pickListsDetailResult.getPickListsDetailId())) {
                    productionPickListsCartResult.setProductionPickListsDetailResult(pickListsDetailResult);
                    break;
                }
            }
            for (BrandResult brandResult : brandResults) {
                if (productionPickListsCartResult.getBrandId().equals(brandResult.getBrandId())) {
                    productionPickListsCartResult.setBrandResult(brandResult);
                    break;
                }
            }
        }
    }


    private Serializable getKey(ProductionPickListsCartParam param) {
        return param.getPickListsCart();
    }

    private Page<ProductionPickListsCartResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private ProductionPickListsCart getOldEntity(ProductionPickListsCartParam param) {
        return this.getById(getKey(param));
    }

    private ProductionPickListsCart getEntity(ProductionPickListsCartParam param) {
        ProductionPickListsCart entity = new ProductionPickListsCart();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

    @Override
    public List<CartGroupByUserListRequest> groupByUser(ProductionPickListsCartParam param) {

        List<ProductionPickLists> productionPickLists = param.getPickListsIds().size() == 0 ? new ArrayList<>() : pickListsService.listByIds(param.getPickListsIds());
        List<ProductionPickListsResult> pickListsResults = new ArrayList<>();
        List<Long> userIds = new ArrayList<>();
        List<Long> pickListsId = new ArrayList<>();
        for (ProductionPickLists productionPickList : productionPickLists) {
            ProductionPickListsResult productionPickListsResult = new ProductionPickListsResult();
            ToolUtil.copyProperties(productionPickList, productionPickListsResult);
            pickListsResults.add(productionPickListsResult);
            userIds.add(productionPickList.getUserId());
            pickListsId.add(productionPickList.getPickListsId());
        }

        pickListsService.format(pickListsResults);
        List<ProductionPickListsCart> pickListsCart = pickListsId.size() == 0 ? new ArrayList<>() : this.query().in("pick_lists_id", pickListsId).list();
        List<ProductionPickListsCartResult> results = new ArrayList<>();
        for (ProductionPickListsCart productionPickListsCart : pickListsCart) {
            ProductionPickListsCartResult result = new ProductionPickListsCartResult();
            ToolUtil.copyProperties(productionPickListsCart, result);
            results.add(result);
        }

        this.format(results);


        List<UserResult> userResultsByIds = userService.getUserResultsByIds(userIds);
        List<CartGroupByUserListRequest> requests = new ArrayList<>();
        for (UserResult userResult : userResultsByIds) {
            CartGroupByUserListRequest request = new CartGroupByUserListRequest();
            request.setName(userResult.getName());
            request.setUserId(userResult.getUserId());
            List<ProductionPickListsCartResult> pickListsCartAll = new ArrayList<>();
            for (ProductionPickListsResult pickListsResult : pickListsResults) {
                if (userResult.getUserId().equals(pickListsResult.getUserId())) {
                    List<ProductionPickListsCartResult> pickListsCartResults = new ArrayList<>();
                    for (ProductionPickListsCartResult result : results) {
                        if (result.getPickListsId().equals(pickListsResult.getPickListsId())) {
                            pickListsCartResults.add(result);
                            result.setProductionPickListsResult(pickListsResult);
                        }
                    }

                    pickListsCartAll.addAll(pickListsCartResults);
                }
            }
            request.setCartResults(pickListsCartAll);
            requests.add(request);
        }


        return requests;
    }

    @Override
    public List<ProductionPickListsCartResult> getSelfCarts(ProductionPickListsCartParam param) {
        List<ProductionPickLists> productionPickLists = pickListsService.query().eq("user_id", LoginContextHolder.getContext().getUserId()).list();
        List<Long> pickListsIds = new ArrayList<>();
        for (ProductionPickLists productionPickList : productionPickLists) {
            pickListsIds.add(productionPickList.getPickListsId());
        }
        List<ProductionPickListsCart> pickListsCarts = pickListsIds.size() == 0 ? new ArrayList<>() : this.query().in("pick_lists_id", pickListsIds).list();
        List<ProductionPickListsCartResult> results = new ArrayList<>();
        for (ProductionPickListsCart pickListsCart : pickListsCarts) {
            ProductionPickListsCartResult result = new ProductionPickListsCartResult();
            ToolUtil.copyProperties(pickListsCart, result);
            results.add(result);
        }
        this.format(results);
        return results;
    }

    @Override
    public List<ProductionPickListsDetailResult> getSelfCartsByLists(Long pickListsId) {

        if (BeanUtil.isNotEmpty(pickListsId)) {
            List<ProductionPickListsDetail> listsDetails = pickListsDetailService.query().eq("pick_lists_id", pickListsId).eq("status", 0).eq("display", 1).list();
            List<ProductionPickListsDetailResult> pickListsDetailResults = BeanUtil.copyToList(listsDetails, ProductionPickListsDetailResult.class);
            pickListsDetailService.format(pickListsDetailResults);
            return pickListsDetailResults;
        }
        return null;

    }

    @Override
    public List<Map<String, Object>> getSelfCartsBySku(ProductionPickListsCartParam productionPickListsCartParam) {
        List<Long> skuIds = new ArrayList<>();
        List<Long> pickListsIds = new ArrayList<>();
        List<ProductionPickListsCartResult> productionPickListsCartResults = this.baseMapper.selfCartList(productionPickListsCartParam, LoginContextHolder.getContext().getUserId());
        for (ProductionPickListsCartResult pickListsCart : productionPickListsCartResults) {
            skuIds.add(pickListsCart.getSkuId());
            pickListsIds.add(pickListsCart.getPickListsId());
        }

        List<ProductionPickLists> productionPickLists = pickListsIds.size() == 0 ? new ArrayList<>() : pickListsService.listByIds(pickListsIds);
        List<ProductionPickListsResult> pickListsResults = BeanUtil.copyToList(productionPickLists, ProductionPickListsResult.class, new CopyOptions());
        List<Long> userIds = new ArrayList<>();
        for (ProductionPickListsResult pickListsResult : pickListsResults) {
            pickListsIds.add(pickListsResult.getPickListsId());
            userIds.add(pickListsResult.getCreateUser());
        }
        List<UserResult> userResults = userService.getUserResultsByIds(userIds);
        for (ProductionPickListsResult pickListsResult : pickListsResults) {
            for (UserResult userResult : userResults) {
                if (pickListsResult.getCreateUser().equals(userResult.getUserId())) {
                    pickListsResult.setCreateUserResult(userResult);
                }

            }
        }

        List<ProductionPickListsCartResult> cartTotalResults = new ArrayList<>();

        productionPickListsCartResults.parallelStream().collect(Collectors.groupingBy(item -> item.getSkuId() + "_" + item.getPickListsId() + "_" + item.getStorehouseId(), Collectors.toList())).forEach(
                (id, transfer) -> {
                    transfer.stream().reduce((a, b) -> new ProductionPickListsCartResult() {{
                        setSkuId(a.getSkuId());
                        setNumber(a.getNumber() + b.getNumber());
                        setBrandId(a.getBrandId());
                        setPickListsId(a.getPickListsId());
                        setPickListsDetailId(a.getPickListsDetailId());
                        setStorehouseId(a.getStorehouseId());
                    }}).ifPresent(cartTotalResults::add);
                }
        );


        List<ProductionPickListsDetail> details = pickListsIds.size() == 0 ? new ArrayList<>() : pickListsDetailService.query().in("pick_lists_id", pickListsIds).eq("status", 0).eq("display", 1).list();
        List<ProductionPickListsDetailResult> detailResults = BeanUtil.copyToList(details, ProductionPickListsDetailResult.class);

        List<ProductionPickListsDetailResult> detailTotalList = new ArrayList<>();
        for (ProductionPickListsDetailResult pickListsDetailResult : detailTotalList) {
            for (ProductionPickListsResult pickListsResult : pickListsResults) {
                if (pickListsResult.getPickListsId().equals(pickListsDetailResult.getPickListsId())) {
                    pickListsDetailResult.setPickListsResult(pickListsResult);
                }
            }
        }
        detailResults.parallelStream().collect(Collectors.groupingBy(item -> item.getSkuId() + "_" + item.getPickListsId(), Collectors.toList())).forEach(
                (id, transfer) -> {
                    transfer.stream().reduce((a, b) -> new ProductionPickListsDetailResult() {{
                        setPickListsId(a.getPickListsId());
                        setSkuId(a.getSkuId());
                        setNumber(a.getNumber() + b.getNumber());
                        setStorehouseId(a.getStorehouseId());
                    }}).ifPresent(detailTotalList::add);
                }
        );

        for (ProductionPickListsDetailResult detailResult : detailTotalList) {
            for (ProductionPickListsResult pickListsResult : pickListsResults) {
                if (detailResult.getPickListsId().equals(pickListsResult.getPickListsId())) {
                    detailResult.setPickListsResult(pickListsResult);
                }
            }
        }

        List<SkuSimpleResult> skuSimpleResults = skuIds.size() == 0 ? new ArrayList<>() : skuService.simpleFormatSkuResult(skuIds);

        //返回对象
        List<Map<String, Object>> results = new ArrayList<>();

        Map<String, Object> result = new HashMap<>();
        List<Long> storehouseSkuIds = new ArrayList<>();

        for (ProductionPickListsCartResult storehouseCart : cartTotalResults) {
            for (ProductionPickListsDetailResult pickListsDetailResult : detailTotalList) {
                if (storehouseCart.getPickListsId().equals(pickListsDetailResult.getPickListsId())) {
                    storehouseCart.setProductionPickListsDetailResult(pickListsDetailResult);
                }
            }
        }
        List<Map<String, Object>> skuMapResults = new ArrayList<>();
        for (SkuSimpleResult skuSimpleResult : skuSimpleResults) {
            if (skuIds.stream().anyMatch(i -> i.equals(skuSimpleResult.getSkuId()))) {
                Map<String, Object> map = BeanUtil.beanToMap(skuSimpleResult);
                List<ProductionPickListsCartResult> cartResults = new ArrayList<>();
                for (ProductionPickListsCartResult storehouseCart : cartTotalResults) {
                    if (storehouseCart.getSkuId().equals(skuSimpleResult.getSkuId())) {
                        cartResults.add(storehouseCart);
                    }
                }
                map.put("cartResults", cartResults);
                skuMapResults.add(map);
            }
        }
        result.put("skuResults", skuMapResults);
        results.add(result);

        return results;
    }

    @Override
    public void deleteBatchByIds(List<ProductionPickListsCartParam> cartParams) {
        List<ProductionPickListsCart> list = this.query().eq("display", 1).list();
        List<ProductionPickListsCart> updateEntity = new ArrayList<>();
        for (ProductionPickListsCartParam cartParam : cartParams) {
            for (ProductionPickListsCart pickListsCart : list) {
                if (cartParam.getSkuId().equals(pickListsCart.getSkuId()) && cartParam.getPickListsId().equals(pickListsCart.getPickListsId()) && cartParam.getBrandId().equals(pickListsCart.getBrandId())) {
                    pickListsCart.setStatus(-1);
                    pickListsCart.setDisplay(0);
                    if (ToolUtil.isNotEmpty(pickListsCart.getType()) && pickListsCart.getType().equals("frmLoss")) {
                        throw new ServiceException(500, "报损物料不可退回");
                    }
                    updateEntity.add(pickListsCart);
                }
            }
        }

        this.updateBatchById(updateEntity);
    }

    @Override
    public List<ProductionPickListsCartResult> listByListsDetailIds(List<Long> listsDetailIds) {
        if (ToolUtil.isEmpty(listsDetailIds) || listsDetailIds.size() == 0) {
            return new ArrayList<>();
        }
        listsDetailIds = listsDetailIds.stream().distinct().collect(Collectors.toList());
        List<ProductionPickListsCart> list = this.query().in("pick_lists_detail_id", listsDetailIds).eq("status", 0).eq("display", 1).list();
        List<ProductionPickListsCartResult> productionPickListsCartResults = BeanUtil.copyToList(list, ProductionPickListsCartResult.class, new CopyOptions());
        this.format(productionPickListsCartResults);
        return productionPickListsCartResults;
    }

    @Override
    public List<StockDetails> getLockStockDetail() {
        List<Long> inkindIds = new ArrayList<>();
        List<ProductionPickListsCart> carts = this.query().eq("display", 1).list();
        for (ProductionPickListsCart cart : carts) {
            inkindIds.add(cart.getInkindId());
        }
        List<StockDetails> stockDetails = inkindIds.size() == 0 ? new ArrayList<>() : stockDetailsService.query().in("inkind_id", inkindIds).eq("display", 1).list();
        return stockDetails;

    }

    @Override
    public List<Long> getCartInkindIds(ProductionPickListsCartParam productionPickListsCartParam) {
        List<Long> skuIds = new ArrayList<>();
        List<Long> inkindIds = new ArrayList<>();
        for (ProductionPickListsCartParam pickListsCartParam : productionPickListsCartParam.getProductionPickListsCartParams()) {
            skuIds.add(pickListsCartParam.getSkuId());
        }
        List<ProductionPickListsCart> listsCarts = skuIds.size() == 0 ? new ArrayList<>() : this.query().in("sku_id", skuIds).eq("display", 1).list();
        for (ProductionPickListsCart listsCart : listsCarts) {
            inkindIds.add(listsCart.getInkindId());
        }
        return inkindIds;
    }

    @Override
    public List<PickListsStorehouseResult> listPickListsStorehouse(ProductionPickListsParam param) {
        //查询符合条件主表id 用主表id查询所有购物车中物品
        List<Long> ids = pickListsService.idsList(new ProductionPickListsParam() {{
            setUserId(LoginContextHolder.getContext().getUserId());
        }});

        List<ProductionPickListsCart> pickListsCarts = ids.size() == 0 ? new ArrayList<>() : this.query().in("pick_lists_id", ids).eq("status", 0).list();
        List<Long> storehouseIds = new ArrayList<>();
        List<Long> skuIds = new ArrayList<>();
        for (ProductionPickListsCart pickListsCart : pickListsCarts) {
            storehouseIds.add(pickListsCart.getStorehouseId());
            skuIds.add(pickListsCart.getSkuId());
        }
        storehouseIds = storehouseIds.stream().distinct().collect(Collectors.toList());
        skuIds = skuIds.stream().distinct().collect(Collectors.toList());
        List<SkuResult> skuResults = skuService.formatSkuResult(skuIds);
        List<Storehouse> storehouses = storehouseIds.size() == 0 ? new ArrayList<>() : storehouseService.listByIds(storehouseIds);

        List<StorehouseResult> storehouseResults = BeanUtil.copyToList(storehouses, StorehouseResult.class);
        List<PickListsStorehouseResult> results = new ArrayList<>();
        for (StorehouseResult storehouseResult : storehouseResults) {
            PickListsStorehouseResult result = new PickListsStorehouseResult();
            result.setStorehouseResult(storehouseResult);
            List<ProductionPickListsCart> storehouseAndSku = new ArrayList<>();
            pickListsCarts.parallelStream().collect(Collectors.groupingBy(item -> item.getSkuId() + '_' + item.getStorehouseId(), Collectors.toList())).forEach(
                    (id, transfer) -> {
                        transfer.stream().reduce((a, b) -> new ProductionPickListsCart() {{
                            setSkuId(a.getSkuId());
                            setNumber(a.getNumber() + b.getNumber());
                            setStorehouseId(a.getStorehouseId());
                        }}).ifPresent(storehouseAndSku::add);
                    }
            );
            int number = 0;
            int skuCount = 0;
            List<SkuResult> skuTotal = new ArrayList<>();
            for (ProductionPickListsCart pickListsCart : storehouseAndSku) {
                if (pickListsCart.getStorehouseId().equals(storehouseResult.getStorehouseId())) {
                    number += pickListsCart.getNumber();
                    skuCount += 1;
                    for (SkuResult skuResult : skuResults) {
                        if (pickListsCart.getSkuId().equals(skuResult.getSkuId())) {
                            skuTotal.add(skuResult);
                        }
                    }
                }
            }
            result.setSkuCount(skuCount);
            result.setNumberCount(number);
            List<SpuClassificationResult> spuClassificationResults = new ArrayList<>();
            for (SkuResult skuResult : skuTotal) {
                spuClassificationResults.add(skuResult.getSpuResult().getSpuClassificationResult());
            }

            List<SpuClassificationResult> totalSpuClassificationResults = new ArrayList<>();
            spuClassificationResults.parallelStream().collect(Collectors.groupingBy(item -> item.getSpuClassificationId(), Collectors.toList())).forEach(
                    (id, transfer) -> {
                        transfer.stream().reduce((a, b) -> a).ifPresent(totalSpuClassificationResults::add);
                    }
            );
            List<PickListsStorehouseResult.ClassCount> classCounts = new ArrayList<>();
            for (SpuClassificationResult totalSpuClassificationResult : totalSpuClassificationResults) {
                PickListsStorehouseResult.ClassCount classCount = new PickListsStorehouseResult.ClassCount();
                classCount.setClassName(totalSpuClassificationResult.getName());
                int classNumberCount = 0;
                int skuNumberCount = 0;
                for (SkuResult skuResult : skuTotal) {
                    for (ProductionPickListsCart pickListsCart : storehouseAndSku) {
                        if (totalSpuClassificationResult.getSpuClassificationId().equals(skuResult.getSpuResult().getSpuClassificationId()) && pickListsCart.getSkuId().equals(skuResult.getSkuId())) {
                            skuNumberCount += 1;
                            classNumberCount += pickListsCart.getNumber();
                        }
                    }
                }
                classCount.setNumberCount(classNumberCount);
                classCount.setClassCount(skuNumberCount);
                classCounts.add(classCount);
            }
            result.setClassCounts(classCounts);
            results.add(result);

        }
        return results;
    }
}
