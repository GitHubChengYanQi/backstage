package cn.atsoft.dasheng.app.service.impl;


import cn.atsoft.dasheng.app.entity.*;
import cn.atsoft.dasheng.app.model.params.StockDetailsParam;
import cn.atsoft.dasheng.app.model.params.StockParam;
import cn.atsoft.dasheng.app.model.result.*;
import cn.atsoft.dasheng.app.service.*;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.mapper.InstockMapper;
import cn.atsoft.dasheng.app.model.params.InstockParam;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.entity.AttributeValues;
import cn.atsoft.dasheng.erp.entity.Sku;
import cn.atsoft.dasheng.erp.entity.StorehousePositions;
import cn.atsoft.dasheng.erp.model.params.InstockRequest;
import cn.atsoft.dasheng.erp.model.result.BackSku;
import cn.atsoft.dasheng.erp.model.result.SpuResult;
import cn.atsoft.dasheng.erp.service.SkuService;
import cn.atsoft.dasheng.erp.service.StorehousePositionsService;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jodd.cli.Param;
import org.apache.ibatis.reflection.ReflectionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.*;

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
    private StorehousePositionsService storehousePositionsService;
    @Autowired
    private StockDetailsService stockDetailsService;
    @Autowired
    private StockService stockService;
    @Autowired
    private SkuService skuService;

    @Override
    public Long add(InstockParam param) {
        Integer count = skuService.query().in("sku_id", param.getSkuId()).in("display",1).count();
        if (count <= 0) {
            throw new ServiceException(500, "请填写合法数据");
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
    @Transactional
    public void update(InstockParam param) {
        Integer count = skuService.query().in("sku_id", param.getSkuId()).in("display",1).count();
        if (count <= 0) {
            throw new ServiceException(500, "请填写合法数据");
        }
        List<StockDetails> stockDetails = new ArrayList<>();
        List<Instock> instocks = new ArrayList<>();

        StockDetails stockDetail = new StockDetails();
        Date date = new Date();

        for (Instock instock : param.getInstocks()) {
            Long stockId = null;
            Stock stock = stockService.lambdaQuery().eq(Stock::getStorehouseId, instock.getStoreHouseId())
                    .and(i -> i.eq(Stock::getSkuId, instock.getSkuId())).one();
            StockParam stockParam = new StockParam();
            if (ToolUtil.isNotEmpty(stock)) {
                ToolUtil.copyProperties(stock, stockParam);
                long l = stockParam.getInventory() + 1;
                stockParam.setInventory(l);
                stockId = stockService.update(stockParam);
            } else {
                stockParam.setStorehouseId(instock.getStoreHouseId());
                stockParam.setSkuId(instock.getSkuId());
                stockParam.setInventory(1L);
                stockId = stockService.add(stockParam);
            }
            instock.setState(1);
            instocks.add(instock);

            stockDetail.setStockId(stockId);
            stockDetail.setPrice(instock.getCostPrice());
            stockDetail.setStorehouseId(instock.getStoreHouseId());
            stockDetail.setSkuId(instock.getSkuId());
            stockDetail.setBarcode(instock.getBarcode());

            stockDetail.setStorageTime(date);
            stockDetail.setStorageTime(instock.getRegisterTime());
            stockDetails.add(stockDetail);
        }
        stockDetailsService.saveBatch(stockDetails);
        this.updateBatchById(instocks);

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
        IPage<InstockResult> page = this.baseMapper.customPageList(pageContext, param, dataScope);
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

        List<Long> storeIds = new ArrayList<>();
        List<Long> skuIds = new ArrayList<>();
        for (InstockResult datum : data) {
            brandIds.add(datum.getBrandId());

            storeIds.add(datum.getStoreHouseId());
            skuIds.add(datum.getSkuId());
        }
        QueryWrapper<Brand> brandQueryWrapper = new QueryWrapper<>();
        if (ToolUtil.isNotEmpty(brandIds)) {
            brandQueryWrapper.in("brand_id", brandIds);
        }
        List<Brand> brandList = brandService.list(brandQueryWrapper);

        QueryWrapper<Storehouse> storehouseQueryWrapper = new QueryWrapper<>();
        if (ToolUtil.isNotEmpty(storeIds)) {
            storehouseQueryWrapper.in("storehouse_id", storeIds);
        }
        List<Storehouse> storeList = storehouseService.list(storehouseQueryWrapper);
        for (InstockResult datum : data) {

            List<BackSku> backSkus = skuService.backSku(datum.getSkuId());
            SpuResult spuResult = skuService.backSpu(datum.getSkuId());
            datum.setBackSkus(backSkus);
            datum.setSpuResult(spuResult);

            if (ToolUtil.isNotEmpty(datum.getStorehousePositionsId())){
                StorehousePositions storehousePositionsServiceById = storehousePositionsService.getById(datum.getStorehousePositionsId());
                datum.setStorehousePositions(storehousePositionsServiceById);
            }

            if (ToolUtil.isNotEmpty(datum.getSkuId())){
                Sku sku = skuService.getById(datum.getSkuId());
                datum.setSku(sku);
            }


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
