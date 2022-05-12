package cn.atsoft.dasheng.app.service.impl;


import cn.atsoft.dasheng.app.entity.*;
import cn.atsoft.dasheng.app.model.result.*;
import cn.atsoft.dasheng.app.pojo.AddStockParam;
import cn.atsoft.dasheng.app.service.*;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.mapper.StockMapper;
import cn.atsoft.dasheng.app.model.params.StockParam;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.entity.Inkind;
import cn.atsoft.dasheng.erp.entity.Sku;
import cn.atsoft.dasheng.erp.entity.StorehousePositionsBind;
import cn.atsoft.dasheng.erp.model.result.BackSku;
import cn.atsoft.dasheng.erp.model.result.SkuResult;
import cn.atsoft.dasheng.erp.model.result.SpuResult;
import cn.atsoft.dasheng.erp.service.InkindService;
import cn.atsoft.dasheng.erp.service.SkuService;
import cn.atsoft.dasheng.erp.service.StorehousePositionsBindService;
import cn.atsoft.dasheng.erp.service.StorehousePositionsService;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.orCode.entity.OrCodeBind;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 仓库总表 服务实现类
 * </p>
 *
 * @author
 * @since 2021-07-15
 */
@Service
public class StockServiceImpl extends ServiceImpl<StockMapper, Stock> implements StockService {
    @Autowired
    private StorehouseService storehouseService;
    @Autowired
    private BrandService brandService;
    @Autowired
    private StockDetailsService stockDetailsService;
    @Autowired
    private SkuService skuService;
    @Autowired
    private InkindService inkindService;
    @Autowired
    private StorehousePositionsService positionsService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private StorehousePositionsBindService positionsBindService;

    @Override
    public Long add(StockParam param) {
        Stock entity = getEntity(param);
        this.save(entity);
        return entity.getStockId();
    }

    @Override
    public void delete(StockParam param) {
        param.setDisplay(0);
        this.updateById(getEntity(param));
    }

    @Override
    public Long update(StockParam param) {
        Stock oldEntity = getOldEntity(param);
        Stock newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(oldEntity);
        return oldEntity.getStockId();
    }

    @Override
    public StockResult findBySpec(StockParam param) {
        return null;
    }

    @Override
    public List<StockResult> findListBySpec(StockParam param) {
        List<StockResult> stockResults = null;
        if (param.getItemId() != null) {
            stockResults = this.baseMapper.brand(param);
        } else if (param.getStorehouseId() != null) {
            stockResults = this.baseMapper.item(param);
        } else {
            stockResults = this.baseMapper.customList(param);
        }
        format(stockResults);
        return stockResults;
    }

    /**
     * 更新库存
     *
     * @param stockId
     */
    @Override
    public void updateNumber(List<Long> stockId) {
        List<Stock> stockList = this.listByIds(stockId);

        List<StockDetails> stockDetails = stockDetailsService.query().in("stock_id", stockId).list();

        Map<Long, List<StockDetails>> map = new HashMap<>();
        for (StockDetails stockDetail : stockDetails) {

            List<StockDetails> detailsList = map.get(stockDetail.getStockId());

            if (ToolUtil.isEmpty(detailsList)) {
                List<StockDetails> details = new ArrayList<StockDetails>() {{
                    add(stockDetail);
                }};
                map.put(stockDetail.getStockId(), details);
            } else {
                detailsList.add(stockDetail);
                map.put(stockDetail.getStockId(), detailsList);
            }
        }

        for (Stock stock : stockList) {
            List<StockDetails> details = map.get(stock.getStockId());
            Long number = 0L;
            if (ToolUtil.isNotEmpty(details)) {
                for (StockDetails detail : details) {
                    number = number + detail.getNumber();
                }
            }
            stock.setInventory(number);
        }
        this.updateBatchById(stockList);
    }

    //添加库存
    @Override
    @Transactional
    public void addDetails(AddStockParam stockParam) {
        Stock stock = this.lambdaQuery().eq(Stock::getStorehouseId, stockParam.getStoreHouseId())  //查询仓库
                .eq(Stock::getSkuId, stockParam.getSkuId())
                .eq(Stock::getBrandId, stockParam.getBrandId())
                .one();
        Long stockId;
        if (ToolUtil.isNotEmpty(stock)) {
            stockId = stock.getStockId();
        } else {   //新建相同物料的库存
            Stock newStock = new Stock();
            newStock.setBrandId(stockParam.getBrandId());
            newStock.setSkuId(stockParam.getSkuId());
            newStock.setStorehouseId(stockParam.getStoreHouseId());
            this.save(newStock);
            stockId = newStock.getStockId();
        }
        StockDetails stockDetails = new StockDetails();
        stockDetails.setStockId(stockId);
        stockDetails.setNumber(stockParam.getNumber());
        stockDetails.setStorehousePositionsId(positionsService.judgePosition(stockParam.getPositionsId()));
        stockDetails.setQrCodeId(stockParam.getCodeId());
        stockDetails.setInkindId(stockParam.getInkindId());
        stockDetails.setBrandId(stockParam.getBrandId());
        stockDetails.setStorehouseId(stockParam.getStoreHouseId());
        stockDetails.setSkuId(stockParam.getSkuId());
        stockDetailsService.save(stockDetails);

        //修改实物
        Inkind inkind = new Inkind();
        inkind.setNumber(stockParam.getNumber());
        inkind.setType("1");
        inkindService.update(inkind, new QueryWrapper<Inkind>() {{
            eq("inkind_id", stockParam.getInkindId());
        }});

        this.updateNumber(new ArrayList<Long>() {{  //更改stock数量
            add(stockId);
        }});
    }

    @Override
    public PageInfo<StockResult> findPageBySpec(StockParam param, DataScope dataScope) {
        Page<StockResult> pageContext = getPageContext();
        IPage<StockResult> page = this.baseMapper.customPageList(pageContext, param, dataScope);
        format(page.getRecords());
        return PageFactory.createPageInfo(page);
    }

    @Override
    public void batchDelete(List<Long> Ids) {
        Stock stock = new Stock();
        stock.setDisplay(0);
        QueryWrapper<Stock> stockQueryWrapper = new QueryWrapper<>();
        stockQueryWrapper.in("stock_id", Ids);
        this.update(stock, stockQueryWrapper);
    }

    /**
     * 通过实物查找库存
     *
     * @param inkinds
     * @param houseId
     * @return
     */
    @Override
    public List<Stock> getStockByInKind(List<Inkind> inkinds, List<Long> houseId) {
        if (ToolUtil.isEmpty(inkinds)) {
            throw new ServiceException(500, "没有实物");
        }

        List<Long> brandIds = new ArrayList<>();
        List<Long> skuIds = new ArrayList<>();
        List<Long> customerIds = new ArrayList<>();

        for (Inkind inkind : inkinds) {
            brandIds.add(inkind.getBrandId());
            skuIds.add(inkind.getSkuId());
            customerIds.add(inkind.getCustomerId());
        }

        return this.query().in("sku_id", skuIds)
                .in("brand_id", brandIds)
                .in("customer_id", customerIds)
                .in("storehouse_id", houseId).list();
    }


    private Serializable getKey(StockParam param) {
        return param.getStockId();
    }

    private Page<StockResult> getPageContext() {
        List<String> fields = new ArrayList<String>() {{
            add("bname");
            add("pname");
            add("iname");
        }};
        return PageFactory.defaultPage(fields);
    }

    private Stock getOldEntity(StockParam param) {
        return this.getById(getKey(param));
    }

    private Stock getEntity(StockParam param) {
        Stock entity = new Stock();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

    public void format(List<StockResult> data) {
        List<Long> storeIds = new ArrayList<>();
        List<Long> brandIds = new ArrayList<>();
        List<Long> stockIds = new ArrayList<>();
        List<Long> skuIds = new ArrayList<>();
        List<Long> customerIds = new ArrayList<>();
        for (StockResult datum : data) {
            storeIds.add(datum.getStorehouseId());
            skuIds.add(datum.getSkuId());
            brandIds.add(datum.getBrandId());
            stockIds.add(datum.getStockId());
            customerIds.add(datum.getCustomerId());
        }

        QueryWrapper<Storehouse> storehouseQueryWrapper = new QueryWrapper<>();
        if (!storeIds.isEmpty()) {
            storehouseQueryWrapper.in("storehouse_id", storeIds);
        }
        List<Storehouse> storeList = storehouseService.list(storehouseQueryWrapper);


        QueryWrapper<Brand> brandQueryWrapper = new QueryWrapper<>();
        if (!brandIds.isEmpty()) {
            brandQueryWrapper.in("brand_id", brandIds);
        }
        List<Sku> skus = skuIds.size() == 0 ? new ArrayList<>() : skuService.query().in("sku_id", skuIds).list();

        List<CustomerResult> results = customerService.getResults(customerIds);

        List<Brand> brandList = brandService.list(brandQueryWrapper);
        List<StockDetails> stockDetails = new ArrayList<>();
        if (ToolUtil.isNotEmpty(storeIds) && ToolUtil.isNotEmpty(skus) && ToolUtil.isNotEmpty(brandIds)) {
            stockDetails = stockDetailsService.lambdaQuery()
                    .in(StockDetails::getStockId, stockIds)
                    .and(i -> i.in(StockDetails::getBrandId, brandIds))
                    .and(i -> i.in(StockDetails::getItemId, skus))
                    .list();
        }


        for (StockResult datum : data) {
            SpuResult spuResult = skuService.backSpu(datum.getSkuId());
            List<BackSku> backSkus = skuService.backSku(datum.getSkuId());
            datum.setBackSkus(backSkus);
            datum.setSpuResult(spuResult);

            for (CustomerResult result : results) {
                if (result.getCustomerId().equals(datum.getCustomerId())) {
                    datum.setCustomerResult(result);
                }
            }


            if (ToolUtil.isNotEmpty(datum.getSkuId())) {
                Sku sku = skuService.getById(datum.getSkuId());
                datum.setSku(sku);
            }

            if (!storeList.isEmpty()) {
                for (Storehouse storehouse : storeList) {
                    if (ToolUtil.isNotEmpty(datum.getStorehouseId()) && ToolUtil.isNotEmpty(storehouse.getStorehouseId()) && datum.getStorehouseId().equals(storehouse.getStorehouseId())) {
                        StorehouseResult storehouseResult = new StorehouseResult();
                        ToolUtil.copyProperties(storehouse, storehouseResult);
                        datum.setStorehouseResult(storehouseResult);
                        break;
                    }
                }
            }

            if (!brandList.isEmpty()) {
                for (Brand brand : brandList) {
                    if (datum.getBrandId() != null && datum.getBrandId().equals(brand.getBrandId())) {
                        BrandResult brandResult = new BrandResult();
                        ToolUtil.copyProperties(brand, brandResult);
                        datum.setBrandResult(brandResult);
                        break;
                    }
                }
            }
            if (ToolUtil.isNotEmpty(stockDetails)) {
                for (StockDetails stockDetail : stockDetails) {
                    if (stockDetail.getStockId()
                            .equals(datum.getStockId()) && stockDetail.getBrandId()
                            .equals(datum.getBrandId()) && datum.getItemId()
                            .equals(stockDetail.getItemId())) {

                        if (ToolUtil.isNotEmpty(stockDetail.getPrice())) {
                            datum.setSalePrice(Math.toIntExact(stockDetail.getPrice()));
                        }

                    }
                }
            }
        }
    }
}
