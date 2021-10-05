package cn.atsoft.dasheng.app.service.impl;


import cn.atsoft.dasheng.app.entity.*;
import cn.atsoft.dasheng.app.model.params.StockParam;
import cn.atsoft.dasheng.app.model.result.*;
import cn.atsoft.dasheng.app.service.*;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.mapper.InstockMapper;
import cn.atsoft.dasheng.app.model.params.InstockParam;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.exception.ServiceException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jodd.cli.Param;
import org.apache.ibatis.reflection.ReflectionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
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
        if (param.getNumber()>1000) {
            throw new ServiceException(500,"一次性入库数量上限1000");
        }
        Instock entity = getEntity(param);
        this.save(entity);
        return entity.getInstockId();
    }

    @Override
    public void delete(InstockParam param) {
        Instock byId = this.getById(param.getInstockId());
        if (ToolUtil.isEmpty(byId)) {
            throw new ServiceException(500, "删除目标不存在");
        }
        param.setDisplay(0);
        Instock oldEntity = getOldEntity(param);
        Instock newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);

    }

    @Override
    public void update(InstockParam param) {
        Instock oldEntity = getOldEntity(param);
        Instock newEntity = getEntity(param);

        Instock entity = getEntity(param);
        StockParam stockParam = new StockParam();
        List<Stock> Stock = this.stockService.list();
        StockDetails stockDetails = new StockDetails();
        boolean useFlag = false;
        try {
            if (ToolUtil.isEmpty(Stock)) {
                stockParam.setItemId(entity.getItemId());
                stockParam.setBrandId(entity.getBrandId());
                stockParam.setStorehouseId(entity.getStoreHouseId());
                stockParam.setInventory(entity.getNumber());
                Long StockId = this.stockService.add(stockParam);

                stockDetails.setStockId(StockId);
                stockDetails.setPrice(entity.getCostPrice());
                stockDetails.setStorageTime(entity.getRegisterTime());
                stockDetails.setItemId(entity.getItemId());
                stockDetails.setStorehouseId(entity.getStoreHouseId());
                stockDetails.setBrandId(entity.getBrandId());
                stockDetails.setBarcode(entity.getBarcode());

                if (ToolUtil.isNotEmpty(stockDetails)) {
                    List<StockDetails> list = new ArrayList<>();
                    for (int j = 0; j < entity.getNumber(); j++) {
                        list.add(stockDetails);
                    }
                    this.stockDetailsService.saveBatch(list);
                }
            } else {
                // 判断仓库是否有相同数据
                // 有相同数据则增加数量和明细数据
                List<StockParam> stockParamList = new ArrayList<>();
                List<StockDetails> stockDetailsList = new ArrayList<>();
                for (Stock StockList : Stock) {
                    if (StockList.getItemId().equals(entity.getItemId())
                            && StockList.getBrandId().equals(entity.getBrandId())
                            && StockList.getStorehouseId().equals(entity.getStoreHouseId())
                    ) {
                        stockParam.setStockId(StockList.getStockId());
                        stockParam.setItemId(entity.getItemId());
                        stockParam.setBrandId(entity.getBrandId());
                        stockParam.setStorehouseId(entity.getStoreHouseId());
                        stockParam.setInventory(entity.getNumber() + StockList.getInventory());
//                        this.stockService.update(stockParam);
                        stockParamList.add(stockParam);
                        stockDetails.setStockId(StockList.getStockId());
                        stockDetails.setPrice(entity.getCostPrice());
                        stockDetails.setStorageTime(entity.getRegisterTime());
                        stockDetails.setItemId(entity.getItemId());
                        stockDetails.setStorehouseId(entity.getStoreHouseId());
                        stockDetails.setBrandId(entity.getBrandId());
                        stockDetails.setBarcode(entity.getBarcode());
                        List<StockDetails> list = new ArrayList<>();
                        if (ToolUtil.isNotEmpty(stockDetails)) {
                            for (int j = 0; j < entity.getNumber(); j++) {
                                list.add(stockDetails);
                            }
                            useFlag = true;
//                            this.stockDetailsService.saveBatch(list);
                            for (StockDetails details : list) {
                                stockDetailsList.add(details);
                            }

                        }
                    }
                }
                Collection<Stock> updateList = new ArrayList<>();
                ToolUtil.copyProperties(stockParamList,updateList);
                this.stockService.saveOrUpdateBatch(updateList);
                this.stockDetailsService.saveBatch(stockDetailsList);
                if (!useFlag) {
                    stockParam.setItemId(entity.getItemId());
                    stockParam.setBrandId(entity.getBrandId());
                    stockParam.setStorehouseId(entity.getStoreHouseId());
                    stockParam.setInventory(entity.getNumber());
                    Long StockId = this.stockService.add(stockParam);

                    stockDetails.setStockId(StockId);
                    stockDetails.setPrice(entity.getCostPrice());
                    stockDetails.setStorageTime(entity.getRegisterTime());
                    stockDetails.setItemId(entity.getItemId());
                    stockDetails.setStorehouseId(entity.getStoreHouseId());
                    stockDetails.setBrandId(entity.getBrandId());
                    stockDetails.setBarcode(entity.getBarcode());
                    List<StockDetails> list = new ArrayList<>();
                    for (int j = 0; j < entity.getNumber(); j++) {
                        list.add(stockDetails);
                    }
                    this.stockDetailsService.saveBatch(list);
                }
            }
            ToolUtil.copyProperties(newEntity, oldEntity);
            this.updateById(newEntity);
        } catch (ReflectionException e) {
            //
        }

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
    public PageInfo<InstockResult> findPageBySpec(InstockParam param, DataScope dataScope) {
        Page<InstockResult> pageContext = getPageContext();
        IPage<InstockResult> page = this.baseMapper.customPageList(pageContext, param,dataScope);
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
            storeIds.add(datum.getStoreHouseId());
        }
        QueryWrapper<Brand> brandQueryWrapper = new QueryWrapper<>();
        if (ToolUtil.isNotEmpty(brandIds)) {
            brandQueryWrapper.in("brand_id", brandIds);
        }
        List<Brand> brandList = brandService.list(brandQueryWrapper);

        QueryWrapper<Items> itemsQueryWrapper = new QueryWrapper<>();
        if (ToolUtil.isNotEmpty(itemIds)) {
            itemsQueryWrapper.in("item_id", itemIds);
        }
        List<Items> itemsList = itemsService.list(itemsQueryWrapper);

        QueryWrapper<Storehouse> storehouseQueryWrapper = new QueryWrapper<>();
        if (ToolUtil.isNotEmpty(storeIds)) {
            storehouseQueryWrapper.in("storehouse_id", storeIds);
        }
        List<Storehouse> storeList = storehouseService.list(storehouseQueryWrapper);
        for (InstockResult datum : data) {
            if (ToolUtil.isNotEmpty(brandList)) {
                for (Brand brand : brandList) {
                    if (brand.getBrandId().equals(datum.getBrandId())) {
                        BrandResult brandResult = new BrandResult();
                        ToolUtil.copyProperties(brand, brandResult);
                        datum.setBrandResult(brandResult);
                        break;
                    }
                }
            }
            if (ToolUtil.isNotEmpty(itemsList)) {
                for (Items items : itemsList) {
                    if (items.getItemId().equals(datum.getItemId())) {
                        ItemsResult itemsResult = new ItemsResult();
                        ToolUtil.copyProperties(items, itemsResult);
                        datum.setItemsResult(itemsResult);
                        break;
                    }
                }
            }
            if (ToolUtil.isNotEmpty(storeList)) {
                for (Storehouse storehouse : storeList) {
                    if (storehouse.getStorehouseId().equals(datum.getStoreHouseId())) {
                        StorehouseResult storehouseResult = new StorehouseResult();
                        ToolUtil.copyProperties(storehouse, storehouseResult);
                        datum.setStorehouseResult(storehouseResult);
                        break;
                    }
                }
            }
        }
    }
}
