package cn.atsoft.dasheng.app.service.impl;


import cn.atsoft.dasheng.app.entity.*;
import cn.atsoft.dasheng.app.model.params.StockParam;
import cn.atsoft.dasheng.app.model.result.*;
import cn.atsoft.dasheng.app.service.*;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.mapper.OutstockMapper;
import cn.atsoft.dasheng.app.model.params.OutstockParam;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.exception.ServiceException;
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
 * 出库表 服务实现类
 * </p>
 *
 * @author song
 * @since 2021-07-17
 */
@Service
public class OutstockServiceImpl extends ServiceImpl<OutstockMapper, Outstock> implements OutstockService {
    @Autowired
    private StockService stockService;
    @Autowired
    private BrandService brandService;
    @Autowired
    private ItemsService itemsService;
    @Autowired
    private StorehouseService storehouseService;


    @Override
    public void add(OutstockParam param) {
        Stock stock = stockService.getById(param.getOutstockId());
        if (ToolUtil.isEmpty(stock)) {
            throw new ServiceException(500, "没有此物品");
        }

        Long inventory = stock.getInventory();
        Long stockId = stock.getStockId();
        Long storehouseId = stock.getStorehouseId();
        Long brandId = stock.getBrandId();
        StockParam stockParam = new StockParam();
        if (inventory >= param.getNumber()) {
            if (param.getBrandId() != null && stock.getBrandId().equals(param.getBrandId())) {
                stockParam.setStockId(stockId);
                stockParam.setInventory(inventory - param.getNumber());
                stockParam.setStorehouseId(storehouseId);
                stockParam.setBrandId(brandId);
                stockService.update(stockParam);
                Outstock entity = getEntity(param);
                this.save(entity);
            } else {
                throw new ServiceException(500, "请选择正确品牌");
            }

        } else {
            throw new ServiceException(500, "数量不足");
        }


    }

    @Override
    public void delete(OutstockParam param) {
        param.setDisplay(0);
        this.update(param);
//        this.removeById(getKey(param));
    }

    @Override
    public void update(OutstockParam param) {
        Outstock oldEntity = getOldEntity(param);
        Outstock newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public OutstockResult findBySpec(OutstockParam param) {
        return null;
    }

    @Override
    public List<OutstockResult> findListBySpec(OutstockParam param) {
        return null;
    }

    @Override
    public PageInfo<OutstockResult> findPageBySpec(OutstockParam param) {
        Page<OutstockResult> pageContext = getPageContext();
        IPage<OutstockResult> page = this.baseMapper.customPageList(pageContext, param);
        format(page.getRecords());
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(OutstockParam param) {
        return param.getOutstockId();
    }

    private Page<OutstockResult> getPageContext() {
        List<String> fields = new ArrayList<String>() {{
            add("brandName");
            add("name");
            add("number");
            add("deliveryTime");
            add("price");
        }};
        return PageFactory.defaultPage(fields);
    }

    private Outstock getOldEntity(OutstockParam param) {
        return this.getById(getKey(param));
    }

    private Outstock getEntity(OutstockParam param) {
        Outstock entity = new Outstock();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

    public void format(List<OutstockResult> data) {
        List<Long> brandIds = new ArrayList<>();
        List<Long> itemIds = new ArrayList<>();
        List<Long> storehouseIds = new ArrayList<>();
        for (OutstockResult datum : data) {
            brandIds.add(datum.getBrandId());
            itemIds.add(datum.getItemId());
            storehouseIds.add(datum.getStorehouseId());
        }
        QueryWrapper<Brand> brandQueryWrapper = new QueryWrapper<>();
        if(ToolUtil.isNotEmpty(brandIds)){
            brandQueryWrapper.in("brand_id", brandIds);
        }
        List<Brand> brandList = brandService.list(brandQueryWrapper);

        QueryWrapper<Items> itemsQueryWrapper = new QueryWrapper<>();
        if(ToolUtil.isNotEmpty(itemIds)){
            itemsQueryWrapper.in("item_id",itemIds);
        }
        List<Items> itemsList = itemsService.list(itemsQueryWrapper);

        QueryWrapper<Storehouse> storehouseQueryWrapper = new QueryWrapper<>();
        if(ToolUtil.isNotEmpty(storehouseIds)){
            storehouseQueryWrapper.in("storehouse_id",storehouseIds);
        }
        List<Storehouse> storeList = storehouseService.list(storehouseQueryWrapper);
        for (OutstockResult datum : data) {
            if(ToolUtil.isNotEmpty(brandList)) {
                for (Brand brand : brandList) {
                    if (brand.getBrandId().equals(datum.getBrandId())) {
                        BrandResult brandResult = new BrandResult();
                        ToolUtil.copyProperties(brand, brandResult);
                        datum.setBrandResult(brandResult);
                        break;
                    }
                }
            }
            if(ToolUtil.isNotEmpty(itemsList)) {
                for (Items items : itemsList) {
                    if (items.getItemId().equals(datum.getItemId())) {
                        ItemsResult itemsResult = new ItemsResult();
                        ToolUtil.copyProperties(items, itemsResult);
                        datum.setItemsResult(itemsResult);
                        break;
                    }
                }
            }
            if(ToolUtil.isNotEmpty(storeList)) {
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
}
