package cn.atsoft.dasheng.app.service.impl;


import cn.atsoft.dasheng.app.entity.*;
import cn.atsoft.dasheng.app.model.params.StockParam;
import cn.atsoft.dasheng.app.model.result.BrandResult;
import cn.atsoft.dasheng.app.model.result.ItemsResult;
import cn.atsoft.dasheng.app.model.result.StorehouseResult;
import cn.atsoft.dasheng.app.service.*;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.mapper.InstockMapper;
import cn.atsoft.dasheng.app.model.params.InstockParam;
import cn.atsoft.dasheng.app.model.result.InstockResult;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.exception.ServiceException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.rmi.ServerError;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 入库表 服务实现类
 * </p>
 *
 * @author song
 * @since 2021-07-17
 */
@Service
public class InstockServiceImpl extends ServiceImpl<InstockMapper, Instock> implements InstockService {
    @Autowired
    private BrandService brandService;
    @Autowired
    private ItemsService itemsService;
    @Autowired
    private StorehouseService storehouseService;
    @Autowired
    private StockService stockService;


    @Override
    public Long add(InstockParam param) {
        Items items = itemsService.getById(param.getItemId());
        Long itemId = items.getItemId();
        QueryWrapper<Stock> stockQueryWrapper = new QueryWrapper<>();
        stockQueryWrapper.in("item_id", itemId);
        List<Stock> stockList = stockService.list(stockQueryWrapper);
        for (Stock stock : stockList) {
            if (stock.getItemId().equals(itemId)) {
                Long inventory = stock.getInventory();
                Long stockId = stock.getStockId();
                Long brandId = stock.getBrandId();
                Long storehouseId = stock.getStorehouseId();
                StockParam stockParam = new StockParam();
                stockParam.setStockId(stockId);
                stockParam.setStorehouseId(storehouseId);
                stockParam.setStockId(brandId);
                stockParam.setInventory(inventory);
                stockService.update(stockParam);
                Instock entity = getEntity(param);
                this.save(entity);
                return entity.getInstockId();
            }
        }
        throw new ServiceException(500, "添加失败");
    }

    @Override
    public void delete(InstockParam param) {
        this.removeById(getKey(param));
    }

    @Override
    public void update(InstockParam param) {
        Instock oldEntity = getOldEntity(param);
        Instock newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public InstockResult findBySpec(InstockParam param) {
        return null;
    }

    @Override
    public List<InstockResult> findListBySpec(InstockParam param) {
        return null;
    }

    @Override
    public PageInfo<InstockResult> findPageBySpec(InstockParam param) {
        Page<InstockResult> pageContext = getPageContext();
        IPage<InstockResult> page = this.baseMapper.customPageList(pageContext, param);
        format(page.getRecords());
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(InstockParam param) {
        return param.getInstockId();
    }

    private Page<InstockResult> getPageContext() {
        List<String> fields = new ArrayList<String>() {{
            add("brandName");
            add("name");
            add("number");
            add("placeName");
            add("registerTime");
            add("price");
        }};
        return PageFactory.defaultPage(fields);
    }

    private Instock getOldEntity(InstockParam param) {
        return this.getById(getKey(param));
    }

    private Instock getEntity(InstockParam param) {
        Instock entity = new Instock();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

    public void format(List<InstockResult> data) {
        List<Long> brandIds = new ArrayList<>();
        List<Long> itemIds = new ArrayList<>();
        List<Long> storeIds = new ArrayList<>();
        for (InstockResult datum : data) {
            brandIds.add(datum.getBrandId());
            itemIds.add(datum.getItemId());
            storeIds.add(datum.getStorehouseId());
        }
        QueryWrapper<Brand> brandQueryWrapper = new QueryWrapper<>();
        brandQueryWrapper.in("brand_id", brandIds);
        List<Brand> brandList = brandService.list(brandQueryWrapper);

        QueryWrapper<Items> itemsQueryWrapper = new QueryWrapper<>();
        itemsQueryWrapper.in("item_id",itemIds);
        List<Items> itemsList = itemsService.list(itemsQueryWrapper);

        QueryWrapper<Storehouse> storehouseQueryWrapper = new QueryWrapper<>();
        storehouseQueryWrapper.in("storehouse_id",storeIds);
        List<Storehouse> storeList = storehouseService.list(storehouseQueryWrapper);
        for (InstockResult datum : data) {
            for (Brand brand : brandList) {
                if (brand.getBrandId().equals(datum.getBrandId())) {
                    BrandResult brandResult = new BrandResult();
                    ToolUtil.copyProperties(brand, brandResult);
                    datum.setBrandResult(brandResult);
                    break;
                }
            }
            for (Items items : itemsList) {
                if (items.getItemId().equals(datum.getItemId())) {
                    ItemsResult itemsResult = new ItemsResult();
                    ToolUtil.copyProperties(items,itemsResult);
                    datum.setItemsResult(itemsResult);
                    break;
                }
            }
            for (Storehouse storehouse : storeList) {
                if (storehouse.getStorehouseId().equals(datum.getStorehouseId())) {
                    StorehouseResult storehouseResult =new StorehouseResult();
                    ToolUtil.copyProperties(storehouse,storehouseResult);
                    datum.setStorehouseResult(storehouseResult);
                    break;
                }
            }
        }


    }
}
