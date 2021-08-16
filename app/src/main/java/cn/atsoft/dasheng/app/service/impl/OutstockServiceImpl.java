package cn.atsoft.dasheng.app.service.impl;


import cn.atsoft.dasheng.app.entity.*;
import cn.atsoft.dasheng.app.model.params.StockDetailsParam;
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
    private StockDetailsService stockDetailsService;
    @Autowired
    private BrandService brandService;
    @Autowired
    private ItemsService itemsService;
    @Autowired
    private StorehouseService storehouseService;


    @Override
    public Long add(OutstockParam param) {
        Outstock entity = getEntity(param);
        this.save(entity);
        return entity.getOutstockId();

    }

    @Override
    public void delete(OutstockParam param){
      Outstock byId = this.getById(param.getOutstockId());
      if (ToolUtil.isEmpty(byId)){
        throw new ServiceException(500,"删除目标不存在");
      }
      param.setDisplay(0);
      this.update(param);
    }

    @Override
    public void update(OutstockParam param) {

        Outstock entity = getEntity(param);
        StockParam stockParam = new StockParam();
        StockDetailsParam stockDetailsParam = new StockDetailsParam();
        // 取得入库表的仓库id,品牌id,产品id
        stockParam.setBrandId(entity.getBrandId());
        stockParam.setItemId(entity.getItemId());
        stockParam.setStorehouseId(entity.getStorehouseId());

        List<Stock> Stock = this.stockService.list();
        List<StockDetails> stockDetail = this.stockDetailsService.list();

        if (ToolUtil.isEmpty(Stock)) {
            throw new ServiceException(500, "仓库没有此产品或仓库库存不足！");
        } else {
            for (int i = 0; i < Stock.size(); i++) {

                Stock StockList = Stock.get(i);
                if (StockList.getItemId().equals(entity.getItemId())
                        && StockList.getBrandId().equals(entity.getBrandId())
                        && StockList.getStorehouseId().equals(entity.getStorehouseId())
                ) {
                    if (StockList.getInventory() == 0) {
                        throw new ServiceException(500, "此产品仓库库存不足！");
                    } else if (entity.getNumber() > StockList.getInventory()) {
                        throw new ServiceException(500, "此产品仓库库存不足,请重新输入数量，最大数量为：" + StockList.getInventory());
                    } else {
                        stockParam.setStockId(StockList.getStockId());
                        stockParam.setItemId(StockList.getItemId());
                        stockParam.setBrandId(StockList.getBrandId());
                        stockParam.setStorehouseId(StockList.getStorehouseId());
                        stockParam.setInventory(StockList.getInventory() - entity.getNumber());
                        this.stockService.update(stockParam);

                        if (ToolUtil.isEmpty(stockDetail)) {
                            throw new ServiceException(500, "库存明细里没有此产品或仓库库存不足！");
                        } else {
                            List stockItemIds = new ArrayList<>();
                            for (int j = 0; j < entity.getNumber(); j++) {
                                StockDetails StockDetailsList = stockDetail.get(j);
                                stockItemIds.add(StockDetailsList.getStockItemId());
                            }
                            this.stockDetailsService.removeByIds(stockItemIds);
                        }
                    }
                }
            }
        }
        Outstock oldEntity = getOldEntity(param);
        Outstock newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public OutstockResult findBySpec(OutstockParam param){
        return null;
    }

    @Override
    public List<OutstockResult> findListBySpec(OutstockParam param){
        return null;
    }

    @Override
    public PageInfo<OutstockResult> findPageBySpec(OutstockParam param){
        Page<OutstockResult> pageContext = getPageContext();
        IPage<OutstockResult> page = this.baseMapper.customPageList(pageContext, param);
        format(page.getRecords());
        return PageFactory.createPageInfo(page);
    }

    @Override
    public OutstockResult detail(Long id) {
        Outstock outstock = this.getById(id);
        OutstockResult outstockResult = new OutstockResult();
        ToolUtil.copyProperties(outstock, outstockResult);
        List<OutstockResult> results = new ArrayList<OutstockResult>() {{
            add(outstockResult);
        }};
        format(results);
        return results.get(0);
    }

    private Serializable getKey(OutstockParam param){
        return param.getOutstockId();
    }

    private Page<OutstockResult> getPageContext() {
        List<String> fields = new ArrayList<String>(){{
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
