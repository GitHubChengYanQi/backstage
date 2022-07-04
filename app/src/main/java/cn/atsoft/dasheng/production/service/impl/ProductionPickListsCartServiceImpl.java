package cn.atsoft.dasheng.production.service.impl;


import cn.atsoft.dasheng.app.entity.StockDetails;
import cn.atsoft.dasheng.app.entity.Storehouse;
import cn.atsoft.dasheng.app.model.result.BrandResult;
import cn.atsoft.dasheng.app.model.result.StorehouseResult;
import cn.atsoft.dasheng.app.model.result.StorehouseSimpleResult;
import cn.atsoft.dasheng.app.service.BrandService;
import cn.atsoft.dasheng.app.service.StockDetailsService;
import cn.atsoft.dasheng.app.service.StorehouseService;
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


    @Override
    public void add(ProductionPickListsCartParam param) {
//        List<StockDetails> stockDetailList = foundCanBeUseStockDetail(param);
//        List<StockDetails> stockDetailsList =  inkindIds.size() == 0 ? stockDetailsService.query().eq("stage", 1).eq("display", 1).list() : stockDetailsService.query().in("sku_id", skuIds).notIn("inkind_id", inkindIds).eq("display", 1).list();
        //判断添加条件
        this.addCheck(param);

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


        List<StockDetails> totalList = new ArrayList<>();
        stockDetailList.parallelStream().collect(Collectors.groupingBy(item -> item.getSkuId() + '_' + (ToolUtil.isEmpty(item.getBrandId()) ? 0L : item.getBrandId() + '_' + item.getStorehousePositionsId()), Collectors.toList())).forEach(
                (id, transfer) -> {
                    transfer.stream().reduce((a, b) -> new StockDetails() {{
                        setSkuId(a.getSkuId());
                        setNumber(a.getNumber() + b.getNumber());
                        setBrandId(ToolUtil.isEmpty(a.getBrandId()) ? 0L : a.getBrandId());
                        setStorehousePositionsId(a.getStorehousePositionsId());
                    }}).ifPresent(totalList::add);
                }
        );


        for (ProductionPickListsCartParam pickListsCartParam : param.getProductionPickListsCartParams()) {
            if (totalList.size() == 0) {
                throw new ServiceException(500, "此物料数量已经被其他备料占用,无法满足目前单据数量出库");
            }
            for (StockDetails stockDetail : totalList) {
                if (pickListsCartParam.getSkuId().equals(stockDetail.getSkuId()) && pickListsCartParam.getStorehousePositionsId().equals(stockDetail.getStorehousePositionsId()) && pickListsCartParam.getBrandId().equals(stockDetail.getBrandId()) && pickListsCartParam.getSkuId().equals(stockDetail.getSkuId()) && pickListsCartParam.getNumber() > stockDetail.getNumber()) {
                    throw new ServiceException(500, "此物料数量已经被其他备料占用,无法满足目前单据数量出库");
                } else if (totalList.stream().noneMatch(i -> i.getSkuId().equals(pickListsCartParam.getSkuId())) || totalList.stream().noneMatch(i -> i.getBrandId().equals(pickListsCartParam.getBrandId())) || totalList.stream().noneMatch(i -> i.getStorehousePositionsId().equals(pickListsCartParam.getStorehousePositionsId()))) {
                    throw new ServiceException(500, "此物料数量已经被其他备料占用,无法满足目前单据数量出库");
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
                    if ((ToolUtil.isNotEmpty(stockDetails.getBrandId()) && stockDetails.getBrandId().equals(productionPickListsCartParam.getBrandId()) && stockDetails.getSkuId().equals(productionPickListsCartParam.getSkuId()) && stockDetails.getStorehousePositionsId().equals(productionPickListsCartParam.getStorehousePositionsId())) || ((ToolUtil.isEmpty(stockDetails.getBrandId()) || stockDetails.getBrandId().equals(0L)) && stockDetails.getSkuId().equals(productionPickListsCartParam.getSkuId()) && stockDetails.getStorehousePositionsId().equals(productionPickListsCartParam.getStorehousePositionsId()))) {
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


    private void addCheck(ProductionPickListsCartParam param){
        ProductionPickListsDetail listsDetail = pickListsDetailService.getById(param.getPickListsDetailId());
        if (!listsDetail.getBrandId().equals(param.getBrandId()) && !listsDetail.getBrandId().equals(0L)){
            throw new ServiceException(500,"请选择对应品牌");
        }
        if (!listsDetail.getSkuId().equals(param.getSkuId())){
            throw new ServiceException(500,"请选择这对应物料");
        }
        if (listsDetail.getNumber()-listsDetail.getReceivedNumber()<param.getNumber()){
            throw new ServiceException(500,"备料数量溢出,备料失败");
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
        List<Long> brandIds = new ArrayList<>();
        List<Long> stockIds = new ArrayList<>();
        for (ProductionPickListsCartResult productionPickListsCartResult : param) {
            skuIds.add(productionPickListsCartResult.getSkuId());
            stockIds.add(productionPickListsCartResult.getStorehouseId());
            brandIds.add(productionPickListsCartResult.getBrandId());
        }
        /**
         * ids去重
         */
        skuIds = skuIds.stream().distinct().collect(Collectors.toList());
        brandIds = brandIds.stream().distinct().collect(Collectors.toList());
        stockIds = stockIds.stream().distinct().collect(Collectors.toList());


        /**
         * 查询数据
         */
        List<BrandResult> brandResults = brandIds.size() == 0 ? new ArrayList<>() : brandService.getBrandResults(brandIds);
        List<Storehouse> storehouses = stockIds.size() == 0 ? new ArrayList<>() : storehouseService.listByIds(stockIds);
        List<StorehouseSimpleResult> storehouseResults = BeanUtil.copyToList(storehouses, StorehouseSimpleResult.class, new CopyOptions());

//        List<SkuResult> skuResults = skuService.formatSkuResult(skuIds);
        List<SkuSimpleResult> skuSimpleResults = skuService.simpleFormatSkuResult(skuIds);
        for (ProductionPickListsCartResult productionPickListsCartResult : param) {
            for (SkuSimpleResult skuResult : skuSimpleResults) {
                if (productionPickListsCartResult.getSkuId().equals(skuResult.getSkuId())) {
                    productionPickListsCartResult.setSkuResult(skuResult);
                    break;
                }
            }

            for (BrandResult brandResult : brandResults) {
                if (productionPickListsCartResult.getBrandId().equals(brandResult.getBrandId())) {
                    productionPickListsCartResult.setBrandResult(brandResult);
                }
            }
            for (StorehouseSimpleResult storehouseResult : storehouseResults) {
                if (storehouseResult.getStorehouseId().equals(productionPickListsCartResult.getStorehouseId())) {
                    productionPickListsCartResult.setStorehouseResult(storehouseResult);
                }
            }
        }
    }

    void mirageByStorehouseAndSkuFormat(List<ProductionPickListsCartResult> param) {
        List<Long> pickListsId = new ArrayList<>();
        for (ProductionPickListsCartResult pickListsCartResult : param) {
            pickListsId.add(pickListsCartResult.getPickListsId());
        }
        List<ProductionPickListsDetail> details = pickListsDetailService.query().in("pick_lists_id", pickListsId).eq("status", 0).eq("display", 1).list();
        List<ProductionPickListsDetailResult> detailResults = BeanUtil.copyToList(details, ProductionPickListsDetailResult.class);
        List<ProductionPickListsDetailResult> totalList = new ArrayList<>();

        detailResults.parallelStream().collect(Collectors.groupingBy(item -> item.getSkuId() + '_' + item.getStorehouseId() + "_" + item.getPickListsId(), Collectors.toList())).forEach(
                (id, transfer) -> {
                    transfer.stream().reduce((a, b) -> new ProductionPickListsDetailResult() {{
                        setPickListsId(a.getPickListsId());
                        setSkuId(a.getSkuId());
                        setNumber(a.getNumber() + b.getNumber());
                        setStorehouseId(a.getStorehouseId());
                    }}).ifPresent(totalList::add);
                }
        );


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
        List<ProductionPickLists> productionPickLists = pickListsService.query().eq("user_id", LoginContextHolder.getContext().getUserId()).eq("display", 1).list();
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
        List<ProductionPickListsDetail> pickListsDetails = pickListsIds.size() == 0 ? new ArrayList<>() : pickListsDetailService.query().in("pick_lists_id", pickListsIds).eq("display", 1).eq("status", 0).list();
        List<ProductionPickListsDetailResult> pickListsDetailResults = BeanUtil.copyToList(pickListsDetails, ProductionPickListsDetailResult.class, new CopyOptions());
        pickListsDetailService.format(pickListsDetailResults);


        return pickListsResults;


    }

    @Override
    public List<Map<String, Object>> getSelfCartsBySku(ProductionPickListsCartParam productionPickListsCartParam) {
        List<Long> skuIds = new ArrayList<>();
        List<ProductionPickLists> productionPickLists = pickListsService.query().eq("user_id", LoginContextHolder.getContext().getUserId()).list();
        List<ProductionPickListsResult> pickListsResults = BeanUtil.copyToList(productionPickLists, ProductionPickListsResult.class, new CopyOptions());
        List<Long> pickListsIds = new ArrayList<>();
        List<Long> userIds = new ArrayList<>();
        for (ProductionPickListsResult pickListsResult : pickListsResults) {
            pickListsIds.add(pickListsResult.getPickListsId());
            userIds.add(pickListsResult.getCreateUser());
        }
        List<UserResult> userResults = userService.getUserResultsByIds(userIds);
        for (ProductionPickListsResult pickListsResult : pickListsResults) {
            for (UserResult userResult : userResults) {
                if (pickListsResult.getCreateUser().equals(userResult.getUserId())){
                    pickListsResult.setCreateUserResult(userResult);
                }

            }
        }

        List<ProductionPickListsCart> pickListsCarts = this.query().in("pick_lists_id", pickListsIds).eq("display", 1).list();
        List<ProductionPickListsCartResult> productionPickListsCartResults = BeanUtil.copyToList(pickListsCarts, ProductionPickListsCartResult.class, new CopyOptions());
        List<Long> storehouseIds = new ArrayList<>();

        for (ProductionPickListsCart pickListsCart : pickListsCarts) {
            storehouseIds.add(pickListsCart.getStorehouseId());
            skuIds.add(pickListsCart.getSkuId());
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
        detailResults.parallelStream().collect(Collectors.groupingBy(item -> item.getSkuId() +  "_" + item.getPickListsId(), Collectors.toList())).forEach(
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
                if (detailResult.getPickListsId().equals(pickListsResult.getPickListsId())){
                    detailResult.setPickListsResult(pickListsResult);
                }
            }
        }

        List<SkuSimpleResult> skuSimpleResults = skuIds.size() == 0 ? new ArrayList<>() : skuService.simpleFormatSkuResult(skuIds);
        List<Storehouse> storehouses = storehouseIds.size() == 0 ? new ArrayList<>() : storehouseService.listByIds(storehouseIds.stream().distinct().collect(Collectors.toList()));
        List<StorehouseResult> storehouseResults = BeanUtil.copyToList(storehouses, StorehouseResult.class);
        //返回对象
        List<Map<String, Object>> results = new ArrayList<>();
        for (StorehouseResult storehouseResult : storehouseResults) {
            Map<String, Object> result = new HashMap<>();
            result.put("storehouseResult", storehouseResult);
            List<Long> storehouseSkuIds = new ArrayList<>();
            List<ProductionPickListsCartResult> storehouseCarts = new ArrayList<>();
            for (ProductionPickListsCartResult productionPickListsCartResult : cartTotalResults) {
                if (storehouseResult.getStorehouseId().equals(productionPickListsCartResult.getStorehouseId())) {
                    storehouseCarts.add(productionPickListsCartResult);
                    storehouseSkuIds.add(productionPickListsCartResult.getSkuId());
                }
            }
            for (ProductionPickListsCartResult storehouseCart : storehouseCarts) {
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
                    for (ProductionPickListsCartResult storehouseCart : storehouseCarts) {
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
}
