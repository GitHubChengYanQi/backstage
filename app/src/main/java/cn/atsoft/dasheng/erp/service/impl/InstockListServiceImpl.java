package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.app.entity.*;
import cn.atsoft.dasheng.app.model.params.InstockParam;
import cn.atsoft.dasheng.app.model.params.StockDetailsParam;
import cn.atsoft.dasheng.app.model.params.StockParam;
import cn.atsoft.dasheng.app.model.result.BrandResult;
import cn.atsoft.dasheng.app.model.result.ItemsResult;
import cn.atsoft.dasheng.app.model.result.StorehouseResult;
import cn.atsoft.dasheng.app.service.*;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.InstockList;
import cn.atsoft.dasheng.erp.mapper.InstockListMapper;
import cn.atsoft.dasheng.erp.model.params.InstockListParam;
import cn.atsoft.dasheng.erp.model.result.InstockListResult;
import cn.atsoft.dasheng.erp.model.result.InstockOrderResult;
import cn.atsoft.dasheng.erp.service.InstockListService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import cn.atsoft.dasheng.sys.modular.system.model.result.UserResult;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
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
 * 入库清单 服务实现类
 * </p>
 *
 * @author song
 * @since 2021-10-06
 */
@Service
public class InstockListServiceImpl extends ServiceImpl<InstockListMapper, InstockList> implements InstockListService {
    @Autowired
    private InstockService instockService;
    @Autowired
    private StockService stockService;
    @Autowired
    private StockDetailsService stockDetailsService;
    @Autowired
    private UserService userService;
    @Autowired
    private ItemsService itemsService;
    @Autowired
    private BrandService brandService;
    @Autowired
    private StorehouseService storehouseService;


    @Override
    public void add(InstockListParam param) {
        InstockList entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(InstockListParam param) {
        this.removeById(getKey(param));
    }

    @Override
    public void update(InstockListParam param) {
        InstockList oldEntity = getOldEntity(param);
        InstockList newEntity = getEntity(param);
        if (oldEntity.getNumber() > 0) {
            long l = oldEntity.getNumber() - 1;
            newEntity.setNumber(l);
            ToolUtil.copyProperties(newEntity, oldEntity);
            this.updateById(newEntity);


            InstockParam instockParam = new InstockParam();
            instockParam.setState(1);
            instockParam.setBrandId(newEntity.getBrandId());
            instockParam.setItemId(newEntity.getItemId());
            instockParam.setStoreHouseId(newEntity.getStoreHouseId());
            instockParam.setCostPrice(newEntity.getCostPrice());
            instockParam.setSellingPrice(newEntity.getSellingPrice());
            instockParam.setInstockOrderId(newEntity.getInstockOrderId());
            instockService.add(instockParam);

            Stock stock = stockService.lambdaQuery().eq(Stock::getStorehouseId, newEntity.getStoreHouseId())
                    .and(i -> i.eq(Stock::getItemId, newEntity.getItemId()))
                    .and(i -> i.eq(Stock::getBrandId, newEntity.getBrandId()))
                    .one();
            StockParam stockParam = new StockParam();
            Long stockId = null;
            if (ToolUtil.isNotEmpty(stock)) {
                long number = stock.getInventory() + 1;
                stock.setInventory(number);
                ToolUtil.copyProperties(stock, stockParam);
                stockId = stockService.update(stockParam);
            } else {
                stockParam.setInventory(1L);
                stockParam.setItemId(newEntity.getItemId());
                stockParam.setBrandId(newEntity.getBrandId());
                stockParam.setStorehouseId(newEntity.getStoreHouseId());
                stockId = stockService.add(stockParam);
            }
            StockDetailsParam stockDetailsParam = new StockDetailsParam();
            stockDetailsParam.setStockId(stockId);
            stockDetailsParam.setStorehouseId(newEntity.getStoreHouseId());
            stockDetailsParam.setItemId(newEntity.getItemId());
            stockDetailsParam.setPrice(newEntity.getCostPrice());
            stockDetailsParam.setBrandId(newEntity.getBrandId());
            stockDetailsService.add(stockDetailsParam);

        } else {
            throw new ServiceException(500, "已经入库成功");
        }

    }

    @Override
    public InstockListResult findBySpec(InstockListParam param) {
        return null;
    }

    @Override
    public List<InstockListResult> findListBySpec(InstockListParam param) {
        return null;
    }

    @Override
    public PageInfo<InstockListResult> findPageBySpec(InstockListParam param) {
        Page<InstockListResult> pageContext = getPageContext();
        IPage<InstockListResult> page = this.baseMapper.customPageList(pageContext, param);
        format(page.getRecords());
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(InstockListParam param) {
        return param.getInstockListId();
    }

    private Page<InstockListResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private InstockList getOldEntity(InstockListParam param) {
        return this.getById(getKey(param));
    }

    private InstockList getEntity(InstockListParam param) {
        InstockList entity = new InstockList();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

    private void format(List<InstockListResult> data) {
        List<Long> itemIds = new ArrayList<>();
        List<Long> brandIds = new ArrayList<>();
        List<Long> storeIds = new ArrayList<>();
        for (InstockListResult datum : data) {
            itemIds.add(datum.getItemId());
            brandIds.add(datum.getBrandId());
            storeIds.add(datum.getStoreHouseId());
        }
        List<Items> items = itemIds.size() == 0 ? new ArrayList<>() : itemsService.lambdaQuery().in(Items::getItemId, itemIds).list();
        List<Brand> brands = brandIds.size() == 0 ? new ArrayList<>() : brandService.lambdaQuery().in(Brand::getBrandId, brandIds).list();
        List<Storehouse> storehouses = storeIds.size() == 0 ? new ArrayList<>() : storehouseService.lambdaQuery().in(Storehouse::getStorehouseId, storeIds).list();
        for (InstockListResult datum : data) {
            for (Items item : items) {
                if (item.getItemId().equals(datum.getItemId())) {
                    ItemsResult itemsResult = new ItemsResult();
                    ToolUtil.copyProperties(item, itemsResult);
                    datum.setItemsResult(itemsResult);
                    break;
                }
            }
            for (Brand brand : brands) {
                if (datum.getBrandId().equals(brand.getBrandId())) {
                    BrandResult brandResult = new BrandResult();
                    ToolUtil.copyProperties(brand, brandResult);
                    datum.setBrandResult(brandResult);
                    break;
                }
            }
            for (Storehouse storehouse : storehouses) {
                if (datum.getStoreHouseId().equals(storehouse.getStorehouseId())) {
                    StorehouseResult storehouseResult = new StorehouseResult();
                    ToolUtil.copyProperties(storehouse, storehouseResult);
                    datum.setStorehouseResult(storehouseResult);
                }
            }
        }
    }

}
