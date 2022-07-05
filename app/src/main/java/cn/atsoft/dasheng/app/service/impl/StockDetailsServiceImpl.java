package cn.atsoft.dasheng.app.service.impl;


import cn.atsoft.dasheng.Excel.pojo.StockDetailExcel;
import cn.atsoft.dasheng.app.entity.*;
import cn.atsoft.dasheng.app.model.result.*;
import cn.atsoft.dasheng.app.service.*;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.mapper.StockDetailsMapper;
import cn.atsoft.dasheng.app.model.params.StockDetailsParam;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.entity.Sku;
import cn.atsoft.dasheng.erp.entity.StorehousePositions;
import cn.atsoft.dasheng.erp.model.result.BackSku;
import cn.atsoft.dasheng.erp.model.result.SkuResult;
import cn.atsoft.dasheng.erp.model.result.SpuResult;
import cn.atsoft.dasheng.erp.model.result.StorehousePositionsResult;
import cn.atsoft.dasheng.erp.service.SkuService;
import cn.atsoft.dasheng.erp.service.StorehousePositionsService;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.orCode.model.result.InKindRequest;
import cn.atsoft.dasheng.purchase.pojo.ListingPlan;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private BrandService brandService;
    @Autowired
    private StorehousePositionsService positionsService;
    @Autowired
    private SkuService skuService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private PartsService partsService;

    @Autowired
    private ErpPartsDetailService erpPartsDetailService;

    @Override
    public Long add(StockDetailsParam param) {
        StockDetails entity = getEntity(param);
        this.save(entity);
        return entity.getStockItemId();
    }


    @Override
    public void delete(StockDetailsParam param) {
        param.setDisplay(0);
        this.updateById(getEntity(param));
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
        return this.baseMapper.customList(param);
    }

    @Override
    public List<StockDetailsResult> getDetailsBySkuId(Long id) {
        if (ToolUtil.isEmpty(id)) {
            throw new ServiceException(500, "缺少id");
        }
        List<StockDetails> details = this.query().eq("sku_id", id).list();
        if (ToolUtil.isEmpty(details)) {
            return null;
        }
        List<StockDetailsResult> detailsResults = BeanUtil.copyToList(details, StockDetailsResult.class, new CopyOptions());

        List<Long> brandIds = new ArrayList<Long>();
        List<Long> houseIds = new ArrayList<>();
        for (StockDetailsResult detailsResult : detailsResults) {
            brandIds.add(detailsResult.getBrandId());
            houseIds.add(detailsResult.getStorehouseId());
        }

        List<BrandResult> brandResults = brandService.getBrandResults(brandIds);
        List<StorehousePositions> positions = positionsService.list();
        List<StorehouseResult> storehouseResultList = storehouseService.getDetails(houseIds);
        for (StockDetailsResult detailsResult : detailsResults) {

            StorehousePositionsResult serviceDetail = positionsService.getDetail(detailsResult.getStorehousePositionsId(), positions);
            detailsResult.setPositionsResult(serviceDetail);
            for (StorehouseResult storehouseResult : storehouseResultList) {
                if (storehouseResult.getStorehouseId().equals(detailsResult.getStorehouseId())) {
                    detailsResult.setStorehouseResult(storehouseResult);
                    break;
                }
            }
            for (BrandResult brandResult : brandResults) {
                if (ToolUtil.isNotEmpty(detailsResult.getBrandId()) && brandResult.getBrandId().equals(detailsResult.getBrandId())) {
                    detailsResult.setBrandResult(brandResult);
                    break;
                }
            }

        }
        return detailsResults;
    }




    @Override

    public PageInfo<StockDetailsResult> findPageBySpec(StockDetailsParam param, DataScope dataScope) {
        Page<StockDetailsResult> pageContext = getPageContext();
        IPage<StockDetailsResult> page = this.baseMapper.customPageList(pageContext, param, dataScope);
        format(page.getRecords());
        return PageFactory.createPageInfo(page);
    }

    @Override
    public List<Long> backSkuByStoreHouse(Long id) {
        List<StockDetails> details = this.query().eq("storehouse_id", id).list();
        List<Long> skuIds = new ArrayList<>();
        if (ToolUtil.isNotEmpty(details)) {
            for (StockDetails detail : details) {
                skuIds.add(detail.getSkuId());
            }
        }
        return skuIds;
    }

    /**
     * 预购获取库存数量
     *
     * @param plans
     */
    @Override
    public void preorder(List<ListingPlan> plans) {
        if (ToolUtil.isNotEmpty(plans)) {

            List<Long> skuIds = new ArrayList<>();
            List<Long> brandIds = new ArrayList<>();
            for (ListingPlan plan : plans) {
                skuIds.add(plan.getSkuId());
                brandIds.add(plan.getBrandId());
            }
            List<StockDetails> details = this.query().in("sku_id", skuIds).in("brand_id", brandIds).list();
            for (ListingPlan plan : plans) {
                Long stockNumber = 0L;
                for (StockDetails detail : details) {
                    if (plan.getSkuId().equals(detail.getSkuId()) && plan.getBrandId().equals(detail.getBrandId())) {
                        stockNumber = stockNumber + detail.getNumber();
                    }
                }
                plan.setStockNumber(stockNumber);
            }
        }

    }

    @Override
    public List<StockDetailsResult> getStockDetails(Long stockId) {
        List<StockDetailsResult> detailsResults = new ArrayList<>();
        if (ToolUtil.isEmpty(stockId)) {
            return detailsResults;
        }
        List<StockDetails> details = this.query().eq("stock_id", stockId).eq("display", 1).list();

        for (StockDetails detail : details) {
            StockDetailsResult stockDetailsResult = new StockDetailsResult();
            ToolUtil.copyProperties(detail, stockDetailsResult);
            detailsResults.add(stockDetailsResult);
        }
        return detailsResults;
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
        List<Long> pIds = new ArrayList<>();
        List<Long> stoIds = new ArrayList<>();
        List<Long> customerIds = new ArrayList<>();
        List<Long> brandIds = new ArrayList<>();
        for (StockDetailsResult datum : data) {
            stoIds.add(datum.getStorehouseId());
            customerIds.add(datum.getCustomerId());
            brandIds.add(datum.getBrandId());
            pIds.add(datum.getStorehousePositionsId());
        }
        List<CustomerResult> results = customerService.getResults(customerIds);

        List<StorehousePositions> positions = pIds.size() == 0 ? new ArrayList<>() : positionsService.query().in("storehouse_positions_id", pIds).list();

        QueryWrapper<Storehouse> storehouseQueryWrapper = new QueryWrapper<>();
        if (!stoIds.isEmpty()) {
            storehouseQueryWrapper.in("storehouse_id", stoIds);
        }
        List<Storehouse> storehouseList = storehouseService.list(storehouseQueryWrapper);


        QueryWrapper<Brand> brandQueryWrapper = new QueryWrapper<>();
        if (!brandIds.isEmpty()) {
            brandQueryWrapper.in("brand_id", brandIds);
        }
        List<Brand> brandList = brandService.list(brandQueryWrapper);

        for (StockDetailsResult datum : data) {
            List<BackSku> backSkus = skuService.backSku(datum.getSkuId());
            SpuResult spuResult = skuService.backSpu(datum.getSkuId());
            datum.setBackSkus(backSkus);
            datum.setSpuResult(spuResult);

            for (CustomerResult result : results) {
                if (result.getCustomerId().equals(datum.getCustomerId())) {
                    datum.setCustomerResult(result);
                    break;
                }
            }

            if (ToolUtil.isNotEmpty(datum.getSkuId())) {
                Sku sku = skuService.getById(datum.getSkuId());
                datum.setSku(sku);
            }

            if (ToolUtil.isNotEmpty(positions)) {
                for (StorehousePositions position : positions) {
                    if (datum.getStorehousePositionsId() != null && position.getStorehousePositionsId().equals(datum.getStorehousePositionsId())) {
                        StorehousePositionsResult storehousePositionsResult = new StorehousePositionsResult();
                        ToolUtil.copyProperties(position, storehousePositionsResult);
                        datum.setStorehousePositionsResult(storehousePositionsResult);
                        break;
                    }
                }
            }

            if (!storehouseList.isEmpty()) {
                for (Storehouse storehouse : storehouseList) {
                    if (storehouse.getStorehouseId().equals(datum.getStorehouseId())) {
                        StorehouseResult storehouseResult = new StorehouseResult();
                        ToolUtil.copyProperties(storehouse, storehouseResult);
                        datum.setStorehouseResult(storehouseResult);
                        break;
                    }
                }
            }

            if (!brandList.isEmpty()) {
                for (Brand brand : brandList) {
                    if (ToolUtil.isNotEmpty(datum.getBrandId()) && datum.getBrandId().equals(brand.getBrandId())) {
                        BrandResult brandResult = new BrandResult();
                        ToolUtil.copyProperties(brand, brandResult);
                        datum.setBrandResult(brandResult);
                        break;
                    }
                }
            }
        }
    }
    @Override
    public List<StockDetailExcel> getStockDetail(){
        List<StockDetailExcel> stockDetailExcels = this.baseMapper.stockDetailExcelExport();
        List<Long>  skuIds = new ArrayList<>();
        List<Long> brandIds = new ArrayList<>();
        List<Long> customerIds = new ArrayList<>();
        List<Long> storeHousePositionIds = new ArrayList<>();

        /**
         * 添加id查询
         */
        for (StockDetailExcel stockDetailExcel : stockDetailExcels) {
            if (ToolUtil.isNotEmpty(stockDetailExcel.getSkuId())) {
                skuIds.add(stockDetailExcel.getSkuId());
            }
            if (ToolUtil.isNotEmpty(stockDetailExcel.getBrandId())) {
                brandIds.add(stockDetailExcel.getBrandId());
            }
            if (ToolUtil.isNotEmpty(stockDetailExcel.getCustomerId())) {
                customerIds.add(stockDetailExcel.getCustomerId());
            }
            if (ToolUtil.isNotEmpty(stockDetailExcel.getStorehousePositionsId())){
                storeHousePositionIds.add(stockDetailExcel.getStorehousePositionsId());
            }
        }
        List<SkuResult> skuResults = skuService.formatSkuResult(skuIds);
        List<BrandResult> brandResults = brandService.getBrandResults(brandIds);
        List<CustomerResult> customerResults = customerService.getResults(customerIds);
        List<StorehousePositionsResult> storehousePositionsResults = positionsService.positionsResults(storeHousePositionIds);
        for (StockDetailExcel stockDetailExcel : stockDetailExcels) {
            for (SkuResult skuResult : skuResults) {
                if (stockDetailExcel.getSkuId().equals(skuResult.getSkuId())) {
                    stockDetailExcel.setSkuResult(skuResult);
                    break;
                }
            }
            for (BrandResult brandResult : brandResults) {
                if (ToolUtil.isNotEmpty(stockDetailExcel.getBrandId()) && stockDetailExcel.getBrandId().equals(brandResult.getBrandId())) {
                    stockDetailExcel.setBrandResult(brandResult);
                }
            }
            for (CustomerResult customerResult : customerResults) {
                if (ToolUtil.isNotEmpty(stockDetailExcel.getCustomerId()) && stockDetailExcel.getCustomerId().equals(customerResult.getCustomerId())) {
                    stockDetailExcel.setCustomerResult(customerResult);
                }
            }
            for (StorehousePositionsResult storehousePositionsResult : storehousePositionsResults) {
                if (ToolUtil.isNotEmpty(stockDetailExcel.getStorehousePositionsId()) && stockDetailExcel.getStorehousePositionsId().equals(storehousePositionsResult.getStorehousePositionsId())) {
                    stockDetailExcel.setStorehousePositionsResult(storehousePositionsResult);
                }
            }
        }
        return stockDetailExcels;
    }
}
