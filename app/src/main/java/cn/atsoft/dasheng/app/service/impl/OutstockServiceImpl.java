package cn.atsoft.dasheng.app.service.impl;


import cn.atsoft.dasheng.app.entity.*;
import cn.atsoft.dasheng.app.model.params.DeliveryDetailsParam;
import cn.atsoft.dasheng.app.model.result.*;
import cn.atsoft.dasheng.app.service.*;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.mapper.OutstockMapper;
import cn.atsoft.dasheng.app.model.params.OutstockParam;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.entity.Sku;
import cn.atsoft.dasheng.erp.model.result.SkuResult;
import cn.atsoft.dasheng.erp.service.SkuService;
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
    private BrandService brandService;
    @Autowired
    private ItemsService itemsService;
    @Autowired
    private StorehouseService storehouseService;
    @Autowired
    private SkuService skuService;

    @Override
    public Long add(OutstockParam param) {
        Outstock entity = getEntity(param);
        this.save(entity);
        return entity.getOutstockId();

    }

    @Override
    public void delete(OutstockParam param) {
        Outstock byId = this.getById(param.getOutstockId());
        if (ToolUtil.isEmpty(byId)) {
            throw new ServiceException(500, "删除目标不存在");
        }
        param.setDisplay(0);
        this.update(param);
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
    public PageInfo<OutstockResult> findPageBySpec(OutstockParam param, DataScope dataScope) {
        Page<OutstockResult> pageContext = getPageContext();
        IPage<OutstockResult> page = this.baseMapper.customPageList(pageContext, param, dataScope);
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
        List<Long> skuIds = new ArrayList<>();
        List<Long> storehouseIds = new ArrayList<>();
        for (OutstockResult datum : data) {
            brandIds.add(datum.getBrandId());
            skuIds.add(datum.getSkuId());
            storehouseIds.add(datum.getStorehouseId());
        }
        QueryWrapper<Brand> brandQueryWrapper = new QueryWrapper<>();
        if (ToolUtil.isNotEmpty(brandIds)) {
            brandQueryWrapper.in("brand_id", brandIds);
        }
        List<Brand> brandList = brandService.list(brandQueryWrapper);


        List<Sku> skus = skuIds.size() == 0 ? new ArrayList<>() : skuService.query().in("sku_id", skuIds).list();


        QueryWrapper<Storehouse> storehouseQueryWrapper = new QueryWrapper<>();
        if (ToolUtil.isNotEmpty(storehouseIds)) {
            storehouseQueryWrapper.in("storehouse_id", storehouseIds);
        }
        List<Storehouse> storeList = storehouseService.list(storehouseQueryWrapper);
        for (OutstockResult datum : data) {
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
            if (ToolUtil.isNotEmpty(skus)) {
                for (Sku sku : skus) {
                    if (datum.getSkuId() != null && sku.getSkuId().equals(datum.getSkuId())) {
                        SkuResult skuResult = new SkuResult();
                        ToolUtil.copyProperties(sku, skuResult);
                        datum.setSkuResult(skuResult);
                    }
                }
            }
            if (ToolUtil.isNotEmpty(storeList)) {
                for (Storehouse storehouse : storeList) {
                    if (storehouse.getStorehouseId().equals(datum.getStorehouseId())) {
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
