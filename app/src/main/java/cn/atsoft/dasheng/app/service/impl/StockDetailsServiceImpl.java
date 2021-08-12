package cn.atsoft.dasheng.app.service.impl;


import cn.atsoft.dasheng.app.entity.Brand;
import cn.atsoft.dasheng.app.entity.Items;
import cn.atsoft.dasheng.app.entity.Storehouse;
import cn.atsoft.dasheng.app.model.result.BrandResult;
import cn.atsoft.dasheng.app.model.result.ItemsResult;
import cn.atsoft.dasheng.app.model.result.StorehouseResult;
import cn.atsoft.dasheng.app.service.BrandService;
import cn.atsoft.dasheng.app.service.ItemsService;
import cn.atsoft.dasheng.app.service.StorehouseService;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.StockDetails;
import cn.atsoft.dasheng.app.mapper.StockDetailsMapper;
import cn.atsoft.dasheng.app.model.params.StockDetailsParam;
import cn.atsoft.dasheng.app.model.result.StockDetailsResult;
import cn.atsoft.dasheng.app.service.StockDetailsService;
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
 * 仓库物品明细表 服务实现类
 * </p>
 *
 * @author
 * @since 2021-07-15
 */
@Service
public class StockDetailsServiceImpl extends ServiceImpl<StockDetailsMapper, StockDetails> implements StockDetailsService {
    @Autowired
    private StorehouseService storehouseService;
    @Autowired
    private ItemsService itemsService;
    @Autowired
    private BrandService brandService;

    @Override
    public Long add(StockDetailsParam param) {
        StockDetails entity = getEntity(param);
        this.save(entity);
        return entity.getStockItemId();
    }

    @Override
    public void delete(StockDetailsParam param) {
        this.removeById(getKey(param));
    }

    @Override
    public void update(StockDetailsParam param) {
        StockDetails oldEntity = getOldEntity(param);
        StockDetails newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public StockDetailsResult findBySpec(StockDetailsParam param) {
        return null;
    }

    @Override
    public List<StockDetailsResult> findListBySpec(StockDetailsParam param) {
        List<StockDetailsResult> stockDetailsResults = this.baseMapper.customList(param);
        return stockDetailsResults;
    }

    @Override
    public PageInfo<StockDetailsResult> findPageBySpec(StockDetailsParam param) {
        Page<StockDetailsResult> pageContext = getPageContext();
        IPage<StockDetailsResult> page = this.baseMapper.customPageList(pageContext, param);
        format(page.getRecords());
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(StockDetailsParam param) {
        return param.getStockItemId();
    }

    private Page<StockDetailsResult> getPageContext() {
        List<String> fields = new ArrayList<String>() {{
            add("iname");
            add("price");
            add("storageTime");
        }};
        return PageFactory.defaultPage(fields);
    }

    private StockDetails getOldEntity(StockDetailsParam param) {
        return this.getById(getKey(param));
    }

    private StockDetails getEntity(StockDetailsParam param) {
        StockDetails entity = new StockDetails();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

    public void format(List<StockDetailsResult> data) {
        List<Long> stoIds = new ArrayList<>();
        List<Long> itemIds = new ArrayList<>();
        List<Long> brandIds = new ArrayList<>();
        for (StockDetailsResult datum : data) {
            stoIds.add(datum.getStorehouseId());
            itemIds.add(datum.getItemsId());
            brandIds.add(datum.getBrandId());
        }
        QueryWrapper<Storehouse> storehouseQueryWrapper = new QueryWrapper<>();
        if(!stoIds.isEmpty()){
            storehouseQueryWrapper.in("storehouse_id",stoIds);
        }
        List<Storehouse> storehouseList = storehouseService.list(storehouseQueryWrapper);

        QueryWrapper<Items> itemsQueryWrapper = new QueryWrapper<>();
        if(!itemIds.isEmpty()){
            itemsQueryWrapper.in("item_id",itemIds);
        }
        List<Items> itemsList = itemsService.list(itemsQueryWrapper);

        QueryWrapper<Brand> brandQueryWrapper = new QueryWrapper<>();
        if(!brandIds.isEmpty()){
            brandQueryWrapper.in("brand_id",brandIds);
        }
        List<Brand> brandList = brandService.list(brandQueryWrapper);

        for (StockDetailsResult datum : data) {
            if(!storehouseList.isEmpty()) {
                for (Storehouse storehouse : storehouseList) {
                    if (storehouse.getStorehouseId().equals(datum.getStorehouseId())) {
                        StorehouseResult storehouseResult = new StorehouseResult();
                        ToolUtil.copyProperties(storehouse, storehouseResult);
                        datum.setStorehouseResult(storehouseResult);
                        break;
                    }
                }
            }
            if(!itemsList.isEmpty()) {
                for (Items items : itemsList) {
                    if (items.getItemId().equals(datum.getItemsId())) {
                        ItemsResult itemsResult = new ItemsResult();
                        ToolUtil.copyProperties(items, itemsResult);
                        datum.setItemsResult(itemsResult);
                        break;
                    }
                }
            }
            if(!brandList.isEmpty()) {
                for (Brand brand : brandList) {
                    if (datum.getBrandId().equals(brand.getBrandId())) {
                        BrandResult brandResult = new BrandResult();
                        ToolUtil.copyProperties(brand, brandResult);
                        datum.setBrandResult(brandResult);
                        break;
                    }
                }
            }
        }
    }
}
