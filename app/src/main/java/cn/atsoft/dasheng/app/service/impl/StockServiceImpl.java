package cn.atsoft.dasheng.app.service.impl;


import cn.atsoft.dasheng.app.entity.*;
import cn.atsoft.dasheng.app.model.result.BrandResult;
import cn.atsoft.dasheng.app.model.result.ItemsResult;
import cn.atsoft.dasheng.app.model.result.StorehouseResult;
import cn.atsoft.dasheng.app.service.*;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.mapper.StockMapper;
import cn.atsoft.dasheng.app.model.params.StockParam;
import cn.atsoft.dasheng.app.model.result.StockResult;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.entity.Sku;
import cn.atsoft.dasheng.erp.model.result.BackSku;
import cn.atsoft.dasheng.erp.model.result.SkuResult;
import cn.atsoft.dasheng.erp.model.result.SpuResult;
import cn.atsoft.dasheng.erp.service.SkuService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 仓库总表 服务实现类
 * </p>
 *
 * @author
 * @since 2021-07-15
 */
@Service
public class StockServiceImpl extends ServiceImpl<StockMapper, Stock> implements StockService {
    @Autowired
    private ItemsService itemsService;
    @Autowired
    private StorehouseService storehouseService;
    @Autowired
    private BrandService brandService;
    @Autowired
    private StockDetailsService stockDetailsService;
    @Autowired
    private SkuService skuService;

    @Override
    public Long add(StockParam param) {
        Stock entity = getEntity(param);
        this.save(entity);
        return entity.getStockId();
    }

    @Override
    public void delete(StockParam param) {
        this.removeById(getKey(param));
    }

    @Override
    public Long update(StockParam param) {
        Stock oldEntity = getOldEntity(param);
        Stock newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(oldEntity);
        return oldEntity.getStockId();
    }

    @Override
    public StockResult findBySpec(StockParam param) {
        return null;
    }

    @Override
    public List<StockResult> findListBySpec(StockParam param) {
        List<StockResult> stockResults = null;
        if (param.getItemId() != null) {
            stockResults = this.baseMapper.brand(param);
        } else if (param.getStorehouseId() != null) {
            stockResults = this.baseMapper.item(param);
        } else {
            stockResults = this.baseMapper.customList(param);
        }
        format(stockResults);
        return stockResults;
    }

    @Override
    public PageInfo<StockResult> findPageBySpec(StockParam param, DataScope dataScope) {
        Page<StockResult> pageContext = getPageContext();
        IPage<StockResult> page = this.baseMapper.customPageList(pageContext, param, dataScope);
        format(page.getRecords());
        return PageFactory.createPageInfo(page);
    }

    @Override
    public void batchDelete(List<Long> Ids) {
        Stock stock = new Stock();
        stock.setDisplay(0);
        QueryWrapper<Stock> stockQueryWrapper = new QueryWrapper<>();
        stockQueryWrapper.in("stock_id", Ids);
        this.update(stock, stockQueryWrapper);
    }

    private Serializable getKey(StockParam param) {
        return param.getStockId();
    }

    private Page<StockResult> getPageContext() {
        List<String> fields = new ArrayList<String>() {{
            add("bname");
            add("pname");
            add("iname");
        }};
        return PageFactory.defaultPage(fields);
    }

    private Stock getOldEntity(StockParam param) {
        return this.getById(getKey(param));
    }

    private Stock getEntity(StockParam param) {
        Stock entity = new Stock();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

    public void format(List<StockResult> data) {
        List<Long> storeIds = new ArrayList<>();
        List<Long> brandIds = new ArrayList<>();
        List<Long> stockIds = new ArrayList<>();
        List<Long> skuIds = new ArrayList<>();
        for (StockResult datum : data) {
            storeIds.add(datum.getStorehouseId());
            skuIds.add(datum.getSkuId());
            brandIds.add(datum.getBrandId());
            stockIds.add(datum.getStockId());
        }

        QueryWrapper<Storehouse> storehouseQueryWrapper = new QueryWrapper<>();
        if (!storeIds.isEmpty()) {
            storehouseQueryWrapper.in("storehouse_id", storeIds);
        }
        List<Storehouse> storeList = storehouseService.list(storehouseQueryWrapper);


        QueryWrapper<Brand> brandQueryWrapper = new QueryWrapper<>();
        if (!brandIds.isEmpty()) {
            brandQueryWrapper.in("brand_id", brandIds);
        }
        List<Sku> skus = skuIds.size() == 0 ? new ArrayList<>() : skuService.query().in("sku_id", skuIds).list();

        List<Brand> brandList = brandService.list(brandQueryWrapper);
        List<StockDetails> stockDetails = new ArrayList<>();
        if (ToolUtil.isNotEmpty(storeIds) && ToolUtil.isNotEmpty(skus) && ToolUtil.isNotEmpty(brandIds)) {
            stockDetails = stockDetailsService.lambdaQuery()
                    .in(StockDetails::getStockId, stockIds)
                    .and(i -> i.in(StockDetails::getBrandId, brandIds))
                    .and(i -> i.in(StockDetails::getItemId, skus))
                    .list();
        }


        for (StockResult datum : data) {
            SpuResult spuResult = skuService.backSpu(datum.getSkuId());
            List<BackSku> backSkus = skuService.backSku(datum.getSkuId());
            datum.setBackSkus(backSkus);
            datum.setSpuResult(spuResult);

//
//            if (ToolUtil.isNotEmpty(skus)) {
//                for (Sku sku : skus) {
//                    if (datum.getSkuId() != null && sku.getSkuId().equals(datum.getSkuId())) {
//                        SkuResult skuResult = new SkuResult();
//                        ToolUtil.copyProperties(sku, skuResult);
//                        datum.setSkuResult(skuResult);
//                    }
//                }
//            }

            if (!storeList.isEmpty()) {
                for (Storehouse storehouse : storeList) {
                    if (datum.getStorehouseId().equals(storehouse.getStorehouseId())) {
                        StorehouseResult storehouseResult = new StorehouseResult();
                        ToolUtil.copyProperties(storehouse, storehouseResult);
                        datum.setStorehouseResult(storehouseResult);
                        break;
                    }
                }
            }

            if (!brandList.isEmpty()) {
                for (Brand brand : brandList) {
                    if (datum.getBrandId() != null && datum.getBrandId().equals(brand.getBrandId())) {
                        BrandResult brandResult = new BrandResult();
                        ToolUtil.copyProperties(brand, brandResult);
                        datum.setBrandResult(brandResult);
                        break;
                    }
                }
            }
            if (ToolUtil.isNotEmpty(stockDetails)) {
                for (StockDetails stockDetail : stockDetails) {
                    if (stockDetail.getStockId()
                            .equals(datum.getStockId()) && stockDetail.getBrandId()
                            .equals(datum.getBrandId()) && datum.getItemId()
                            .equals(stockDetail.getItemId())) {

                        if (ToolUtil.isNotEmpty(stockDetail.getPrice())) {
                            datum.setSalePrice(Math.toIntExact(stockDetail.getPrice()));
                        }

                    }
                }
            }
        }
    }
}
