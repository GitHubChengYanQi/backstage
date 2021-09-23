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
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.util.resources.cldr.mg.LocaleNames_mg;

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
    public void update(StockParam param) {
        Stock oldEntity = getOldEntity(param);
        Stock newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
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
    public PageInfo<StockResult> findPageBySpec(StockParam param) {
        Page<StockResult> pageContext = getPageContext();
        IPage<StockResult> page = this.baseMapper.customPageList(pageContext, param);
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
        List<Long> itemIds = new ArrayList<>();
        List<Long> brandIds = new ArrayList<>();
        List<Long> stockIds = new ArrayList<>();
        for (StockResult datum : data) {
            storeIds.add(datum.getStorehouseId());
            itemIds.add(datum.getItemId());
            brandIds.add(datum.getBrandId());
            stockIds.add(datum.getStockId());
        }

        QueryWrapper<Storehouse> storehouseQueryWrapper = new QueryWrapper<>();
        if (!storeIds.isEmpty()) {
            storehouseQueryWrapper.in("storehouse_id", storeIds);
        }
        List<Storehouse> storeList = storehouseService.list(storehouseQueryWrapper);

        QueryWrapper<Items> itemsQueryWrapper = new QueryWrapper<>();
        if (!itemIds.isEmpty()) {
            itemsQueryWrapper.in("item_id", itemIds);
        }
        List<Items> itemList = itemsService.list(itemsQueryWrapper);

        QueryWrapper<Brand> brandQueryWrapper = new QueryWrapper<>();
        if (!brandIds.isEmpty()) {
            brandQueryWrapper.in("brand_id", brandIds);
        }
        List<Brand> brandList = brandService.list(brandQueryWrapper);
        List<StockDetails> stockDetails = new ArrayList<>();
        if (ToolUtil.isNotEmpty(storeIds) && ToolUtil.isNotEmpty(itemIds) && ToolUtil.isNotEmpty(brandIds)) {
            stockDetails = stockDetailsService.lambdaQuery()
                    .in(StockDetails::getStockId, stockIds)
                    .and(i -> i.in(StockDetails::getBrandId, brandIds))
                    .and(i -> i.in(StockDetails::getItemId, itemIds))
                    .list();
        }


        for (StockResult datum : data) {
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
            if (!itemList.isEmpty()) {
                for (Items items : itemList) {
                    if (datum.getItemId().equals(items.getItemId())) {
                        ItemsResult itemsResult = new ItemsResult();
                        ToolUtil.copyProperties(items, itemsResult);
                        datum.setItemsResult(itemsResult);
                        break;
                    }
                }
            }
            if (!brandList.isEmpty()) {
                for (Brand brand : brandList) {
                    if (datum.getBrandId().equals(brand.getBrandId())) {
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
                        datum.setSalePrice(Math.toIntExact(stockDetail.getPrice()));
                    }
                }
            }
        }
    }
}
