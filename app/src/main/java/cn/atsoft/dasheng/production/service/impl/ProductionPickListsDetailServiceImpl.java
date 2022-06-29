package cn.atsoft.dasheng.production.service.impl;


import cn.atsoft.dasheng.app.entity.StockDetails;
import cn.atsoft.dasheng.app.model.result.BrandResult;
import cn.atsoft.dasheng.app.model.result.CustomerResult;
import cn.atsoft.dasheng.app.service.BrandService;
import cn.atsoft.dasheng.app.service.CustomerService;
import cn.atsoft.dasheng.app.service.StockDetailsService;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.model.result.SkuSimpleResult;
import cn.atsoft.dasheng.erp.service.SkuService;
import cn.atsoft.dasheng.production.entity.ProductionPickListsCart;
import cn.atsoft.dasheng.production.entity.ProductionPickListsDetail;
import cn.atsoft.dasheng.production.mapper.ProductionPickListsDetailMapper;
import cn.atsoft.dasheng.production.model.params.ProductionPickListsDetailParam;
import cn.atsoft.dasheng.production.model.request.StockSkuTotal;
import cn.atsoft.dasheng.production.model.result.ProductionPickListsCartResult;
import cn.atsoft.dasheng.production.model.result.ProductionPickListsDetailResult;
import cn.atsoft.dasheng.production.service.ProductionPickListsCartService;
import cn.atsoft.dasheng.production.service.ProductionPickListsDetailService;
import cn.atsoft.dasheng.core.util.ToolUtil;
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
        List<SkuSimpleResult> skuSimpleResults = skuService.simpleFormatSkuResult(skuIds);
        List<StockDetails> stockSkus = skuIds.size() == 0 ? new ArrayList<>() : stockDetailsService.query().in("sku_id", skuIds).eq("display", 1).list();
        List<ProductionPickListsCartResult> cartResults = pickListsCartService.listByListsDetailIds(detailIds);
        for (StockDetails skus : stockSkus) {
            if (ToolUtil.isEmpty(skus.getBrandId())) {
                skus.setBrandId(0L);
            }
        }
        List<StockDetails> totalList = new ArrayList<>();
        stockSkus.parallelStream().collect(Collectors.groupingBy(item -> item.getSkuId() + '_' + (ToolUtil.isEmpty(item.getBrandId()) ? 0L :item.getBrandId())+ '_' + item.getStorehousePositionsId(),Collectors.toList())).forEach(
                (id, transfer) -> {
                    transfer.stream().reduce((a, b) -> new StockDetails() {{
                        setSkuId(a.getSkuId());
                        setNumber(a.getNumber() + b.getNumber());
                        setBrandId(ToolUtil.isEmpty(a.getBrandId())? 0L:a.getBrandId());
                        setStorehousePositionsId(a.getStorehousePositionsId());
                    }}).ifPresent(totalList::add);
                }
        );
        List<StockDetails> noBrand = new ArrayList<>();
        stockSkus.parallelStream().collect(Collectors.groupingBy(item -> item.getSkuId() + '_' + item.getStorehousePositionsId(),Collectors.toList())).forEach(
                (id, transfer) -> {
                    transfer.stream().reduce((a, b) -> new StockDetails() {{
                        setSkuId(a.getSkuId());
                        setNumber(a.getNumber() + b.getNumber());
                        setStorehousePositionsId(a.getStorehousePositionsId());
                    }}).ifPresent(noBrand::add);
                }
        );

        List<BrandResult> brandResults = brandIds.size() == 0 ? new ArrayList<>() : brandService.getBrandResults(brandIds);
        for (ProductionPickListsDetailResult result : results) {
            result.setIsMeet(false);
            if (!result.getBrandId().equals(0L)){
                for (StockDetails stockSkuTotal : totalList) {
                    if (result.getSkuId().equals(stockSkuTotal.getSkuId()) && result.getBrandId().equals(stockSkuTotal.getBrandId())) {
                        result.setStockNumber(Math.toIntExact(stockSkuTotal.getNumber()));
                        if (result.getNumber() <= stockSkuTotal.getNumber()) {
                            result.setIsMeet(true);
                        }
                    }
                }
            }else {
                for (StockDetails stockDetails : noBrand) {
                    if (result.getSkuId().equals(stockDetails.getSkuId())) {
                        result.setStockNumber(Math.toIntExact(stockDetails.getNumber()));
                        if (result.getNumber() <= stockDetails.getNumber()) {
                            result.setIsMeet(true);
                        }
                    }
                }
            }
            for (SkuSimpleResult skuSimpleResult : skuSimpleResults) {
                if (result.getSkuId().equals(skuSimpleResult.getSkuId())){
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
                if (result.getPickListsDetailId().equals(cartResult.getPickListsDetailId())){
                    cartResultList.add(cartResult);
                }
            }
            result.setCartResults(cartResultList);
        }

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
