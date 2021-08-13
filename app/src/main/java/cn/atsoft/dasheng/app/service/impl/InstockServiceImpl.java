package cn.atsoft.dasheng.app.service.impl;


import cn.atsoft.dasheng.app.entity.Brand;
import cn.atsoft.dasheng.app.entity.Items;
import cn.atsoft.dasheng.app.entity.Storehouse;
import cn.atsoft.dasheng.app.entity.Instock;
import cn.atsoft.dasheng.app.model.params.StockDetailsParam;
import cn.atsoft.dasheng.app.model.params.StockParam;
import cn.atsoft.dasheng.app.model.result.*;
import cn.atsoft.dasheng.app.service.*;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.mapper.InstockMapper;
import cn.atsoft.dasheng.app.model.params.InstockParam;
import cn.atsoft.dasheng.core.util.ToolUtil;
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
    private StockDetailsService stockDetailsService;
    @Autowired
    private StockService stockService;

    @Override
    public Long add(InstockParam param) {
        Instock entity = getEntity(param);
        this.save(entity);

        StockParam stockParam = new StockParam();
        // 取得入库表的仓库id,品牌id,产品id
        stockParam.setBrandId(entity.getBrandId());
        stockParam.setItemId(entity.getItemId());
        stockParam.setStorehouseId(entity.getStorehouseId());

        PageInfo<StockResult> pageBySpec = this.stockService.findPageBySpec(stockParam);

        StockDetailsParam stockDetailsParam = new StockDetailsParam();

        if (ToolUtil.isEmpty(pageBySpec.getData())){
            stockParam.setItemId(entity.getItemId());
            stockParam.setBrandId(entity.getBrandId());
            stockParam.setStorehouseId(entity.getStorehouseId());
            stockParam.setInventory(entity.getNumber());
            Long StockId = this.stockService.add(stockParam);

            stockDetailsParam.setStockId(StockId);
            stockDetailsParam.setPrice(entity.getPrice());
            stockDetailsParam.setStorageTime(entity.getRegisterTime());
            stockDetailsParam.setItemId(entity.getItemId());
            stockDetailsParam.setStorehouseId(entity.getStorehouseId());
            stockDetailsParam.setBrandId(entity.getBrandId());
            stockDetailsParam.setBarcode(entity.getBarcode());
            for (int j = 0 ; j < entity.getNumber() ; j++ ){
                this.stockDetailsService.add(stockDetailsParam);
            }
        }else {
            for (int i = 0 ; i < pageBySpec.getData().size() ; i++) {
                StockResult StockList = pageBySpec.getData().get(i);
                if (StockList.getItemId().equals(entity.getItemId())
                        && StockList.getBrandId().equals(entity.getBrandId())
                        && StockList.getStorehouseId().equals(entity.getStorehouseId())
                ) {

                    stockParam.setStockId(StockList.getStockId());
                    stockParam.setItemId(StockList.getItemId());
                    stockParam.setBrandId(StockList.getBrandId());
                    stockParam.setStorehouseId(StockList.getStorehouseId());
                    stockParam.setInventory(entity.getNumber()+StockList.getInventory());
                    this.stockService.update(stockParam);


                    stockDetailsParam.setStockId(StockList.getStockId());
                    stockDetailsParam.setItemId(StockList.getItemId());
                    stockDetailsParam.setStorehouseId(StockList.getStorehouseId());
                    stockDetailsParam.setBrandId(StockList.getBrandId());
                    stockDetailsParam.setPrice(entity.getPrice());
                    stockDetailsParam.setStorageTime(entity.getRegisterTime());
                    stockDetailsParam.setBarcode(entity.getBarcode());
                    for (int j = 0 ; j < entity.getNumber() ; j++ ){
                        this.stockDetailsService.add(stockDetailsParam);
                    }
                    break;
                }else  {
                    if ( i == pageBySpec.getData().size() - 1 ){
                        stockParam.setItemId(entity.getItemId());
                        stockParam.setBrandId(entity.getBrandId());
                        stockParam.setStorehouseId(entity.getStorehouseId());
                        stockParam.setInventory(entity.getNumber());
                        Long StockId = this.stockService.add(stockParam);

                        stockDetailsParam.setStockId(StockId);
                        stockDetailsParam.setPrice(entity.getPrice());
                        stockDetailsParam.setStorageTime(entity.getRegisterTime());
                        stockDetailsParam.setItemId(entity.getItemId());
                        stockDetailsParam.setStorehouseId(entity.getStorehouseId());
                        stockDetailsParam.setBrandId(entity.getBrandId());
                        stockDetailsParam.setBarcode(entity.getBarcode());
                        for (int j = 0 ; j < entity.getNumber() ; j++ ){
                            this.stockDetailsService.add(stockDetailsParam);
                        }
                    }
                }
            }
        }
        return entity.getInstockId();
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
        if(ToolUtil.isNotEmpty(storeIds)){
            storehouseQueryWrapper.in("storehouse_id",storeIds);
        }
        List<Storehouse> storeList = storehouseService.list(storehouseQueryWrapper);
        for (InstockResult datum : data) {
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
