package cn.atsoft.dasheng.production.service.impl;


import cn.atsoft.dasheng.app.entity.StockDetails;
import cn.atsoft.dasheng.app.model.result.BrandResult;
import cn.atsoft.dasheng.app.service.BrandService;
import cn.atsoft.dasheng.app.service.StockDetailsService;
import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.model.result.SkuSimpleResult;
import cn.atsoft.dasheng.erp.service.SkuService;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.production.entity.ProductionPickLists;
import cn.atsoft.dasheng.production.entity.ProductionPickListsCart;
import cn.atsoft.dasheng.production.entity.ProductionPickListsDetail;
import cn.atsoft.dasheng.production.mapper.ProductionPickListsCartMapper;
import cn.atsoft.dasheng.production.model.params.ProductionPickListsCartParam;
import cn.atsoft.dasheng.production.model.request.CartGroupByUserListRequest;
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
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.omg.CORBA.LongHolder;
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

    @Override
    public void add(ProductionPickListsCartParam param) {
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


        /**
         * 查询库存所有  排除已在购物车实物
         */
        List<StockDetails> stockDetailList = inkindIds.size() == 0 ? stockDetailsService.query().eq("stage", 1).in("sku_id", skuIds).eq("display", 1).list() : stockDetailsService.query().eq("stage", 1).in("sku_id", skuIds).notIn("inkind_id", inkindIds).eq("display", 1).list();
        for (StockDetails stockDetails : stockDetailList) {
            if (ToolUtil.isEmpty(stockDetails.getBrandId())) {
                stockDetails.setBrandId(0L);
            }
        }


//        List<StockDetails> stockDetailsList =  inkindIds.size() == 0 ? stockDetailsService.query().eq("stage", 1).eq("display", 1).list() : stockDetailsService.query().in("sku_id", skuIds).notIn("inkind_id", inkindIds).eq("display", 1).list();



        List<StockDetails> totalList = new ArrayList<>();
        stockDetailList.parallelStream().collect(Collectors.groupingBy(item -> item.getSkuId() + '_' + (ToolUtil.isEmpty(item.getBrandId()) ? 0L :item.getBrandId()+ '_' + item.getStorehousePositionsId()),Collectors.toList())).forEach(
                (id, transfer) -> {
                    transfer.stream().reduce((a, b) -> new StockDetails() {{
                        setSkuId(a.getSkuId());
                        setNumber(a.getNumber() + b.getNumber());
                        setBrandId(ToolUtil.isEmpty(a.getBrandId())? 0L:a.getBrandId());
                        setStorehousePositionsId(a.getStorehousePositionsId());
                    }}).ifPresent(totalList::add);
                }
        );



        for (ProductionPickListsCartParam pickListsCartParam : param.getProductionPickListsCartParams()) {
            if(totalList.size() == 0){
                throw new ServiceException(500, "此物料数量已经被其他备料占用无法满足目前单据数量出库");
            }
            for (StockDetails stockDetail : totalList) {
                if (pickListsCartParam.getSkuId().equals(stockDetail.getSkuId()) &&pickListsCartParam.getStorehousePositionsId().equals(stockDetail.getStorehousePositionsId()) && pickListsCartParam.getBrandId().equals(stockDetail.getBrandId()) && pickListsCartParam.getSkuId().equals(stockDetail.getSkuId()) && pickListsCartParam.getNumber() > stockDetail.getNumber()) {
                    throw new ServiceException(500, "此物料数量已经被其他备料占用无法满足目前单据数量出库");
                }else if (totalList.stream().noneMatch(i->i.getSkuId().equals(pickListsCartParam.getSkuId())) || totalList.stream().noneMatch(i->i.getBrandId().equals(pickListsCartParam.getBrandId())) ||totalList.stream().noneMatch(i->i.getStorehousePositionsId().equals(pickListsCartParam.getStorehousePositionsId())) ){
                    throw new ServiceException(500, "此物料数量已经被其他备料占用无法满足目前单据数量出库");
                }

            }
        }






        List<StockDetails> needStockDetail = new ArrayList<>();
        for (ProductionPickListsCartParam productionPickListsCartParam : param.getProductionPickListsCartParams()) {
            for (StockDetails details : stockDetailList) {
                if (productionPickListsCartParam.getStorehousePositionsId().equals(details.getStorehousePositionsId()) && productionPickListsCartParam.getSkuId().equals(details.getSkuId())) {
                    needStockDetail.add(details);
                }
            }
        }
        /**
         * 过滤实物
         */
        List<ProductionPickListsCart> entitys = new ArrayList<>();
        for (ProductionPickListsCartParam productionPickListsCartParam : param.getProductionPickListsCartParams()) {
            int number = productionPickListsCartParam.getNumber();

                for (StockDetails stockDetails : needStockDetail) {
                    if (number > 0) {
                    if ((ToolUtil.isNotEmpty(stockDetails.getBrandId()) && stockDetails.getBrandId().equals(productionPickListsCartParam.getBrandId()) && stockDetails.getSkuId().equals(productionPickListsCartParam.getSkuId()) && stockDetails.getStorehousePositionsId().equals(productionPickListsCartParam.getStorehousePositionsId())) || (ToolUtil.isEmpty(stockDetails.getBrandId()) && productionPickListsCartParam.getBrandId() == 0 && stockDetails.getSkuId().equals(productionPickListsCartParam.getSkuId()) && stockDetails.getStorehousePositionsId().equals(productionPickListsCartParam.getStorehousePositionsId()))) {
                        int lastNumber = number;
                        number -= stockDetails.getNumber();
                        if (number > 0) {
                            ProductionPickListsCart entity = new ProductionPickListsCart();
                            entity.setNumber(Math.toIntExact(stockDetails.getNumber()));
                            entity.setSkuId(stockDetails.getSkuId());
                            if (ToolUtil.isNotEmpty(stockDetails.getBrandId())) {
                                entity.setBrandId(stockDetails.getBrandId());
                            }
                            entity.setPickListsId(productionPickListsCartParam.getPickListsId());
                            entity.setPickListsDetailId(productionPickListsCartParam.getPickListsDetailId());
                            entity.setStorehousePositionsId(productionPickListsCartParam.getStorehousePositionsId());
                            entity.setStorehouseId(productionPickListsCartParam.getStorehouseId());
                            entity.setInkindId(stockDetails.getInkindId());
                            entitys.add(entity);
                        } else {
                            ProductionPickListsCart entity = new ProductionPickListsCart();
                            entity.setNumber(lastNumber);
                            entity.setSkuId(stockDetails.getSkuId());
                            if (ToolUtil.isNotEmpty(stockDetails.getBrandId())) {
                                entity.setBrandId(stockDetails.getBrandId());
                            }
                            entity.setStorehousePositionsId(productionPickListsCartParam.getStorehousePositionsId());
                            entity.setStorehouseId(productionPickListsCartParam.getStorehouseId());
                            entity.setPickListsId(productionPickListsCartParam.getPickListsId());
                            entity.setPickListsDetailId(productionPickListsCartParam.getPickListsDetailId());
                            entity.setInkindId(stockDetails.getInkindId());
                            entitys.add(entity);
                        }

                    }
                }
            }

        }

        this.saveBatch(entitys);


    }

    /**
     * 检查库存中实物是否可以被重复使用
     * @param param
     */
    private void checkInkintNumber(ProductionPickListsCartParam param){
        //查询出所有的购物车  获取实物  下面会检查库存实物是否满足再次被使用

        List<ProductionPickListsCart> cartList = this.query().eq("display", 1).list();
        List<Long> inkindIds = new ArrayList<>();
        for (ProductionPickListsCart pickListsCart : cartList) {
            if (ToolUtil.isNotEmpty(pickListsCart.getInkindId())) {
                inkindIds.add(pickListsCart.getInkindId());
            }
        }

        List<StockDetails> beUseStockDetail = stockDetailsService.query().eq("stage", 1).in("inkind_id", inkindIds).eq("display", 1).list();
        for (StockDetails stockDetails : beUseStockDetail) {
//            cartList
        }







    }

    @Override
    public void delete(ProductionPickListsCartParam param) {
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
        List<Long> detailIds = new ArrayList<>();
        List<Long> listsIds = new ArrayList<>();
        List<Long> brandIds = new ArrayList<>();
        for (ProductionPickListsCartResult productionPickListsCartResult : param) {
            skuIds.add(productionPickListsCartResult.getSkuId());
            detailIds.add(productionPickListsCartResult.getPickListsDetailId());
            listsIds.add(productionPickListsCartResult.getPickListsId());
            brandIds.add(productionPickListsCartResult.getBrandId());
        }
        List<ProductionPickListsDetail> details = detailIds.size() == 0 ? new ArrayList<>() : pickListsDetailService.listByIds(detailIds);
        List<ProductionPickListsDetailResult> productionPickListsDetailResults = BeanUtil.copyToList(details, ProductionPickListsDetailResult.class, new CopyOptions());

        List<BrandResult> brandResults = brandService.getBrandResults(brandIds);

        List<ProductionPickLists> productionPickLists = listsIds.size() == 0 ? new ArrayList<>() : pickListsService.listByIds(listsIds);
        List<ProductionPickListsResult> pickListsResults = BeanUtil.copyToList(productionPickLists, ProductionPickListsResult.class);
        pickListsService.format(pickListsResults);
//        List<SkuResult> skuResults = skuService.formatSkuResult(skuIds);
        List<SkuSimpleResult> skuSimpleResults = skuService.simpleFormatSkuResult(skuIds);
        for (ProductionPickListsCartResult productionPickListsCartResult : param) {
            for (SkuSimpleResult skuResult : skuSimpleResults) {
                if (productionPickListsCartResult.getSkuId().equals(skuResult.getSkuId())) {
                    productionPickListsCartResult.setSkuResult(skuResult);
                    break;
                }
            }
            for (ProductionPickListsDetailResult productionPickListsDetailResult : productionPickListsDetailResults) {
                if (productionPickListsDetailResult.getPickListsDetailId().equals(productionPickListsCartResult.getPickListsDetailId())) {
                    productionPickListsCartResult.setProductionPickListsDetailResult(productionPickListsDetailResult);
                }
            }
            for (ProductionPickListsResult pickListsResult : pickListsResults) {
                if (productionPickListsCartResult.getPickListsId().equals(pickListsResult.getPickListsId())) {
                    productionPickListsCartResult.setPickListsResult(pickListsResult);
                }
            }
            for (BrandResult brandResult : brandResults) {
                if (productionPickListsCartResult.getBrandId().equals(brandResult.getBrandId())) {
                    productionPickListsCartResult.setBrandResult(brandResult);
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
    public List<ProductionPickListsResult> getSelfCartsByLists(ProductionPickListsCartParam param) {
        List<ProductionPickLists> productionPickLists = pickListsService.query().eq("user_id", LoginContextHolder.getContext().getUserId()).list();
        List<ProductionPickListsResult> pickListsResults = BeanUtil.copyToList(productionPickLists, ProductionPickListsResult.class, new CopyOptions());
        pickListsService.format(pickListsResults);
        for (ProductionPickListsResult pickListsResult : pickListsResults) {
            pickListsResult.setProductionTaskResult(null);
            pickListsResult.setProductionTaskResults(null);
        }

        List<Long> pickListsIds = new ArrayList<>();
        for (ProductionPickListsResult pickListsResult : pickListsResults) {
            pickListsIds.add(pickListsResult.getPickListsId());
        }
        List<ProductionPickListsCart> pickListsCarts = pickListsIds.size() == 0 ? new ArrayList<>() : this.query().in("pick_lists_id", pickListsIds).eq("display", 1).list();
        /**
         * 合并
         */
        List<ProductionPickListsCart> totalList = new ArrayList<>();
        pickListsCarts.parallelStream().collect(Collectors.groupingBy(item -> item.getSkuId() + '_' + item.getBrandId() + '_' + item.getPickListsDetailId() + item.getPickListsId(), Collectors.toList())).forEach(
                (id, transfer) -> {
                    transfer.stream().reduce((a, b) -> new ProductionPickListsCart() {{
                        setSkuId(a.getSkuId());
                        setNumber(a.getNumber() + b.getNumber());
                        setBrandId(a.getBrandId());
                        setPickListsId(a.getPickListsId());
                        setPickListsDetailId(a.getPickListsDetailId());
                        setPickListsId(a.getPickListsId());
                    }}).ifPresent(totalList::add);
                }
        );


        List<ProductionPickListsCartResult> cartResultList = BeanUtil.copyToList(totalList, ProductionPickListsCartResult.class, new CopyOptions());
        this.format(cartResultList);
        for (ProductionPickListsResult pickListsResult : pickListsResults) {
            List<ProductionPickListsCartResult> cartResults = new ArrayList<>();
            for (ProductionPickListsCartResult pickListsCart : cartResultList) {
                if (pickListsResult.getPickListsId().equals(pickListsCart.getPickListsId())) {
                    cartResults.add(pickListsCart);
                }
            }
            pickListsResult.setCartResults(cartResults);
        }


        return pickListsResults;


    }

    @Override
    public List<Map<String, Object>> getSelfCartsBySku(ProductionPickListsCartParam productionPickListsCartParam) {

        List<ProductionPickLists> productionPickLists = pickListsService.query().eq("user_id", LoginContextHolder.getContext().getUserId()).list();
        List<ProductionPickListsResult> pickListsResults = BeanUtil.copyToList(productionPickLists, ProductionPickListsResult.class, new CopyOptions());
        List<Long> pickListsIds = new ArrayList<>();
        for (ProductionPickListsResult pickListsResult : pickListsResults) {
            pickListsIds.add(pickListsResult.getPickListsId());
        }
        List<ProductionPickListsCart> pickListsCarts = this.query().in("pick_lists_id", pickListsIds).eq("display", 1).list();
        List<ProductionPickListsCartResult> productionPickListsCartResults = BeanUtil.copyToList(pickListsCarts, ProductionPickListsCartResult.class, new CopyOptions());
        List<ProductionPickListsCartResult> totalList = new ArrayList<>();
        productionPickListsCartResults.parallelStream().collect(Collectors.groupingBy(item -> item.getSkuId() + '_' + item.getBrandId() +"_" + item.getPickListsId() + "_"+item.getPickListsDetailId(), Collectors.toList())).forEach(
                (id, transfer) -> {
                    transfer.stream().reduce((a, b) -> new ProductionPickListsCartResult() {{
                        setSkuId(a.getSkuId());
                        setNumber(a.getNumber() + b.getNumber());
                        setBrandId(a.getBrandId());
                        setPickListsId(a.getPickListsId());
                        setPickListsDetailId(a.getPickListsDetailId());
                    }}).ifPresent(totalList::add);
                }
        );
        List<ProductionPickListsCartResult> count = new ArrayList<>();
        productionPickListsCartResults.parallelStream().collect(Collectors.groupingBy(item -> item.getSkuId() + '_' + item.getBrandId(), Collectors.toList())).forEach(
                (id, transfer) -> {
                    transfer.stream().reduce((a, b) -> new ProductionPickListsCartResult() {{
                        setSkuId(a.getSkuId());
                        setBrandId(a.getBrandId());
                    }}).ifPresent(count::add);
                }
        );
        List<Long> skuIds = new ArrayList<>();
        List<Long> brandIds = new ArrayList<>();
        for (ProductionPickListsCartResult pickListsCartResult : totalList) {
            skuIds.add(pickListsCartResult.getSkuId());
            brandIds.add(pickListsCartResult.getBrandId());
        }
        this.format(totalList);

        List<SkuSimpleResult> skuSimpleResults = skuIds.size() == 0 ? new ArrayList<>() : skuService.simpleFormatSkuResult(skuIds);
        List<BrandResult> brandResults =brandIds.size() == 0 ? new ArrayList<>() : brandService.getBrandResults(brandIds);

        //返回对象
        List<Map<String, Object>> results = new ArrayList<>();

        for (ProductionPickListsCartResult num : count) {
            Map<String,Object> result = new HashMap<>();
            for (SkuSimpleResult skuSimpleResult : skuSimpleResults) {
                if (num.getSkuId().equals(skuSimpleResult.getSkuId())){
                    result.put("skuResult",skuSimpleResult);
                }
            }

            for (BrandResult brandResult : brandResults) {
                if (brandResult.getBrandId().equals(num.getBrandId())){
                    result.put("brandResult",brandResult);
                }
            }
            List<ProductionPickListsCartResult> resultList = new ArrayList<>();
            for (ProductionPickListsCartResult pickListsCartResult : totalList) {
                if (pickListsCartResult.getSkuId().equals(num.getSkuId()) && pickListsCartResult.getBrandId().equals(num.getBrandId())){
                    resultList.add(pickListsCartResult);
                }
            }
            result.put("pickListsCartResults",resultList);
            results.add(result);
        }



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
                    updateEntity.add(pickListsCart);
                }
            }
        }

        this.updateBatchById(updateEntity);
    }
}
