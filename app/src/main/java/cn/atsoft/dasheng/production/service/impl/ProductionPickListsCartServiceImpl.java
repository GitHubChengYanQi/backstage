package cn.atsoft.dasheng.production.service.impl;


import cn.atsoft.dasheng.app.entity.StockDetails;
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
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
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

    @Override
    public void add(ProductionPickListsCartParam param) {
        List<Long> skuIds = new ArrayList<>();
        List<ProductionPickListsCart> entitys = new ArrayList<>();
        for (ProductionPickListsCartParam productionPickListsCartParam : param.getProductionPickListsCartParams()) {
            ProductionPickListsCart entity = getEntity(productionPickListsCartParam);
            entitys.add(entity);
            skuIds.add(productionPickListsCartParam.getSkuId());
        }
        List<ProductionPickListsCart> pickListsCarts = this.query().in("sku_id", skuIds).eq("display", 1).list();
        pickListsCarts = pickListsCarts.stream().collect(Collectors.toMap(ProductionPickListsCart::getSkuId, a -> a, (o1, o2) -> {
            o1.setNumber(o1.getNumber() + o2.getNumber());
            return o1;
        })).values().stream().collect(Collectors.toList());
        List<StockDetails> stockDetails = stockDetailsService.query().in("sku_id", skuIds).eq("display", 1).list();
        stockDetails = stockDetails.stream().collect(Collectors.toMap(StockDetails::getSkuId, a -> a, (o1, o2) -> {
            o1.setNumber(o1.getNumber() + o2.getNumber());
            return o1;
        })).values().stream().collect(Collectors.toList());

        for (ProductionPickListsCart pickListsCart : pickListsCarts) {
            for (StockDetails stockDetail : stockDetails) {
                if (pickListsCart.getSkuId().equals(stockDetail.getSkuId()) && pickListsCart.getNumber() > stockDetail.getNumber()) {
                    throw new ServiceException(500, "此物料数量已经被其他备料站用无法满足目前单据数量出库");
                }
            }
        }
        this.saveBatch(entitys);
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
        List<Long> listsIds= new ArrayList<>();
        for (ProductionPickListsCartResult productionPickListsCartResult : param) {
            skuIds.add(productionPickListsCartResult.getSkuId());
            detailIds.add(productionPickListsCartResult.getPickListsDetailId());
            listsIds.add(productionPickListsCartResult.getPickListsId());
        }
        List<ProductionPickListsDetail> details = detailIds.size() == 0 ? new ArrayList<>() : pickListsDetailService.listByIds(detailIds);
        List<ProductionPickListsDetailResult> productionPickListsDetailResults = BeanUtil.copyToList(details, ProductionPickListsDetailResult.class, new CopyOptions());


        List<ProductionPickLists> productionPickLists = listsIds.size() == 0 ? new ArrayList<>() : pickListsService.listByIds(listsIds);
        List<ProductionPickListsResult> pickListsResults = BeanUtil.copyToList(productionPickLists, ProductionPickListsResult.class);

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
                if (productionPickListsCartResult.getPickListsId().equals(pickListsResult.getPickListsId())){
                    productionPickListsCartResult.setPickListsResult(pickListsResult);
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
        List<ProductionPickListsCartResult> cartResultList = BeanUtil.copyToList(pickListsCarts, ProductionPickListsCartResult.class, new CopyOptions());
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

        List<Long> skuIds = new ArrayList<>();
        List<Long> brandIds = new ArrayList<>();
        for (ProductionPickListsCartResult pickListsCartResult : productionPickListsCartResults) {
            skuIds.add(pickListsCartResult.getSkuId());
            if (ToolUtil.isNotEmpty(pickListsCartResult.getBrandId())) {
                brandIds.add(pickListsCartResult.getBrandId());
            }
        }
        this.format(productionPickListsCartResults);

        List<SkuSimpleResult> skuSimpleResults = skuIds.size() == 0 ? new ArrayList<>() : skuService.simpleFormatSkuResult(skuIds);


        //返回对象
        List<Map<String, Object>> results = new ArrayList<>();

        for (SkuSimpleResult skuSimpleResult : skuSimpleResults) {
            Map<String, Object> result = BeanUtil.beanToMap(skuSimpleResult);
            List<ProductionPickListsCartResult> pickListsCartResults = new ArrayList<>();
            for (ProductionPickListsCartResult pickListsCartResult : productionPickListsCartResults) {
                if (pickListsCartResult.getSkuId().equals(skuSimpleResult.getSkuId())) {
                    pickListsCartResult.setSkuResult(null);
                    pickListsCartResults.add(pickListsCartResult);
                }
            }
            result.put("pickListsCartsResults", pickListsCartResults);
            results.add(result);
        }

        return results;
    }

    @Override
    public void deleteBatchByIds(List<Long> ids) {
        List<ProductionPickListsCart> entitys = new ArrayList<>();
        for (Long id : ids) {
            ProductionPickListsCart entity = new ProductionPickListsCart();
            entity.setPickListsCart(id);
            entity.setStatus(-1);
            entity.setDisplay(0);
            entitys.add(entity);
        }
        this.updateBatchById(entitys);
    }
}
