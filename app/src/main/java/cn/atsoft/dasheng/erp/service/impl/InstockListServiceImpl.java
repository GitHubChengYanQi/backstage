package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.app.entity.*;
import cn.atsoft.dasheng.app.model.params.InstockParam;
import cn.atsoft.dasheng.app.model.params.StockDetailsParam;
import cn.atsoft.dasheng.app.model.params.StockParam;
import cn.atsoft.dasheng.app.model.result.BrandResult;
import cn.atsoft.dasheng.app.model.result.StockDetailsResult;
import cn.atsoft.dasheng.app.model.result.StorehouseResult;
import cn.atsoft.dasheng.app.service.*;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.Inkind;
import cn.atsoft.dasheng.erp.entity.InstockList;
import cn.atsoft.dasheng.erp.entity.InstockOrder;
import cn.atsoft.dasheng.erp.entity.Sku;
import cn.atsoft.dasheng.erp.mapper.InstockListMapper;
import cn.atsoft.dasheng.erp.model.params.InstockListParam;
import cn.atsoft.dasheng.erp.model.result.*;
import cn.atsoft.dasheng.erp.pojo.InstockListRequest;
import cn.atsoft.dasheng.erp.service.InkindService;
import cn.atsoft.dasheng.erp.service.InstockListService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.service.InstockOrderService;
import cn.atsoft.dasheng.erp.service.SkuService;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.orCode.service.OrCodeBindService;
import cn.atsoft.dasheng.production.model.request.JobBookingDetailCount;
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
import java.util.stream.Collectors;

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
    private BrandService brandService;
    @Autowired
    private StorehouseService storehouseService;
    @Autowired
    private SkuService skuService;
    @Autowired
    private InstockOrderService orderService;
    @Autowired
    private OrCodeBindService codeBindService;
    @Autowired
    private InkindService inkindService;

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
        if (oldEntity.getNumber() > param.getNumber()) {
            throw new ServiceException(500, "数量不符");
        }
        if (oldEntity.getNumber() > 0) {
            long l = oldEntity.getNumber() - param.getNum();
            newEntity.setNumber(l);
            ToolUtil.copyProperties(newEntity, oldEntity);
            this.updateById(newEntity);

            InstockParam instockParam = new InstockParam();
            instockParam.setState(1);
            instockParam.setBrandId(newEntity.getBrandId());
            instockParam.setSkuId(newEntity.getSkuId());
            instockParam.setStoreHouseId(newEntity.getStoreHouseId());
            instockParam.setCostPrice(newEntity.getCostPrice());
            instockParam.setSellingPrice(newEntity.getSellingPrice());
            instockParam.setNumber(param.getNum());
            instockParam.setInstockOrderId(newEntity.getInstockOrderId());
            instockParam.setStorehousePositionsId(newEntity.getStorehousePositionsId());
            instockService.add(instockParam);

            Stock stock = stockService.lambdaQuery().eq(Stock::getStorehouseId, newEntity.getStoreHouseId())
                    .and(i -> i.eq(Stock::getSkuId, newEntity.getSkuId()))
                    .eq(Stock::getBrandId, newEntity.getBrandId())
                    .one();

            StockParam stockParam = new StockParam();
            Long stockId = null;
            if (ToolUtil.isNotEmpty(stock)) {    //库存有此物 合并库存数量
                long number = stock.getInventory() + param.getNum();
                stock.setInventory(number);
                ToolUtil.copyProperties(stock, stockParam);
                stockId = stockService.update(stockParam);
            } else {   //库存没有此物 新增此物
                stockParam.setInventory(param.getNum());
                stockParam.setBrandId(newEntity.getBrandId());
                stockParam.setSkuId(newEntity.getSkuId());
                stockParam.setStorehouseId(newEntity.getStoreHouseId());
                stockId = stockService.add(stockParam);
            }
            StockDetailsParam stockDetailsParam = new StockDetailsParam();
            stockDetailsParam.setStockId(stockId);
            stockDetailsParam.setNumber(param.getNum());
            if (ToolUtil.isNotEmpty(param.getCodeId())) {
                stockDetailsParam.setQrCodeid(param.getCodeId());
                stockDetailsParam.setInkindId(param.getInkindId());
            }
            stockDetailsParam.setStorehouseId(newEntity.getStoreHouseId());
            stockDetailsParam.setPrice(newEntity.getCostPrice());
            stockDetailsParam.setBrandId(newEntity.getBrandId());
            stockDetailsParam.setQrCodeid(param.getCodeId());
            stockDetailsParam.setSkuId(newEntity.getSkuId());
            stockDetailsParam.setStorehousePositionsId(newEntity.getStorehousePositionsId());
            stockDetailsService.add(stockDetailsParam);

            //修改入库单状态
            updateOrderState(param.getInstockOrderId());

        } else {
            throw new ServiceException(500, "已经入库成功");
        }

    }

    private Long getInkindId(Long qrcodeId) {

        Long formId = codeBindService.getFormId(qrcodeId);

        Inkind inkind = inkindService.getById(formId);
        if (ToolUtil.isEmpty(inkind)) {
            throw new ServiceException(500, "二维码不正确");
        }
        Long inkindId = inkind.getInkindId();
        return inkindId;
    }

    /**
     * 修改入库单状态
     *
     * @param instockOrderId
     */
    private void updateOrderState(Long instockOrderId) {
        InstockOrder instockOrder = new InstockOrder();
        instockOrder.setInstockOrderId(instockOrderId);
        instockOrder.setState(1);
        boolean t = true;
        List<InstockList> instockLists = this.query().eq("instock_order_id", instockOrderId).list();
        for (InstockList instockList : instockLists) {
            if (instockList.getNumber() != 0) {
                t = false;
                break;
            }
        }
        if (t) {
            instockOrder.setState(2);
        }
        orderService.updateById(instockOrder);
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

    @Override
    @Transactional
    public void batchInstock(InstockListParam param) {
        InstockList instockList = this.getById(param.getInstockListId()); //判断清单
        Long number = 0L;
        if (instockList.getNumber() == 0) {
            throw new ServiceException(500, "已经全部入库");
        }
        for (InstockListRequest request : param.getRequests()) {  //获取入库的总数量
            number = number + request.getInkind().getNumber();
        }
        //判断清单数量
        if (instockList.getNumber() < number) {   //判断清单数量
            throw new ServiceException(500, "数量不符");
        }
        //修改清单数量
        instockList.setNumber(instockList.getNumber() - number);
        this.updateById(instockList);

        Stock stock = stockService.query().eq("brand_id", instockList.getBrandId())   //判断库存
                .eq("sku_id", instockList.getSkuId())
                .eq("storehouse_id", instockList.getStoreHouseId())
                .one();
        Long stockId = null;
        if (ToolUtil.isNotEmpty(stock)) {   //相同合并数量
            long addNumber = stock.getInventory() + number;
            stock.setInventory(addNumber);
            stockService.updateById(stock);
            stockId = stock.getStockId();
        } else {   //不相同 新建
            Stock newStock = new Stock();
            newStock.setInventory(number);
            newStock.setBrandId(instockList.getBrandId());
            newStock.setSkuId(instockList.getSkuId());
            newStock.setStorehouseId(instockList.getStoreHouseId());
            stockService.save(newStock);
            stockId = newStock.getStockId();
        }
        List<StockDetails> stockDetailsList = new ArrayList<>();
        List<Instock> instocks = new ArrayList<>();
        for (InstockListRequest request : param.getRequests()) {

            Inkind inkind = request.getInkind();
            if (!instockList.getSkuId().equals(inkind.getInkindId()) && !instockList.getBrandId().equals(inkind.getBrandId())) {
                throw new ServiceException(500, "物料与入库单不符");
            }

            StockDetails stockDetail = new StockDetails();
            stockDetail.setNumber(request.getInkind().getNumber());
            stockDetail.setSkuId(request.getInkind().getSkuId());
            stockDetail.setBrandId(request.getInkind().getBrandId());
            stockDetail.setStockId(stockId);
            stockDetail.setQrCodeId(request.getCodeId());
            stockDetail.setStorehouseId(param.getStoreHouseId());
            stockDetail.setStorehousePositionsId(param.getStorehousePositionsId());
            Long inkindId = getInkindId(request.getCodeId());
            stockDetail.setInkindId(inkindId);
            stockDetailsList.add(stockDetail);
            //入库明细
            Instock instock = new Instock();
            instock.setState(1);
            instock.setBrandId(request.getInkind().getBrandId());
            instock.setSkuId(request.getInkind().getSkuId());
            instock.setStoreHouseId(param.getStoreHouseId());
            instock.setNumber(request.getInkind().getNumber());
            instock.setInstockOrderId(instockList.getInstockOrderId());
            instock.setStorehousePositionsId(param.getStorehousePositionsId());
            instocks.add(instock);
        }
        stockDetailsService.saveBatch(stockDetailsList);
        instockService.saveBatch(instocks);
        //修改入库单状态
        updateOrderState(param.getInstockOrderId());
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

    @Override
    public void format(List<InstockListResult> data) {

        List<Long> skuIds = new ArrayList<>();
        List<Long> brandIds = new ArrayList<>();
        List<Long> storeIds = new ArrayList<>();
        for (InstockListResult datum : data) {
            skuIds.add(datum.getSkuId());
            brandIds.add(datum.getBrandId());
            storeIds.add(datum.getStoreHouseId());
        }
        List<Sku> skus = skuIds.size() == 0 ? new ArrayList<>() : skuService.query().in("sku_id", skuIds).list();

        List<Brand> brands = brandIds.size() == 0 ? new ArrayList<>() : brandService.lambdaQuery().in(Brand::getBrandId, brandIds).list();

        List<Storehouse> storehouses = storeIds.size() == 0 ? new ArrayList<>() : storehouseService.lambdaQuery().in(Storehouse::getStorehouseId, storeIds).list();
//        List<StockDetails> stockDetails = skuIds.size() == 0 ? new ArrayList<>() : stockDetailsService.query().in("sku_id", skuIds).list();
//        List<StockDetails> stockDetailsTotal = new ArrayList<>();
//        stockDetails.parallelStream().collect(Collectors.groupingBy(detail->detail.getSkuId(),Collectors.toList())).forEach(
//                (id, transfer) -> {
//                    transfer.stream().reduce((a, b) -> {
//                        return new StockDetails(){{
//                            setSkuId(a.getSkuId());
//                            setNumber(a.getNumber() + b.getNumber());
//                        }};
//                    }).ifPresent(stockDetailsTotal::add);
//                }
//        );
        for (InstockListResult datum : data) {

            datum.setNotNumber(datum.getNumber()-datum.getRealNumber());

            List<BackSku> backSkus = skuService.backSku(datum.getSkuId());
            datum.setBackSkus(backSkus);
            SpuResult backSpu = skuService.backSpu(datum.getSkuId());
            datum.setSpuResult(backSpu);

            if (ToolUtil.isNotEmpty(datum.getSkuId())) {
                Sku sku = skuService.getById(datum.getSkuId());
                datum.setSku(sku);
            }

            if (ToolUtil.isNotEmpty(skus)) {
                for (Sku sku : skus) {
                    if (datum.getSkuId() != null && sku.getSkuId().equals(datum.getSkuId())) {
                        SkuResult skuResult = new SkuResult();
                        ToolUtil.copyProperties(sku, skuResult);
                        datum.setSkuResult(skuResult);

                        break;
                    }
                }
            }
//            for (StockDetails details : stockDetailsTotal) {
//                if (datum.getSkuId().equals(details.getSkuId())){
//                    Map<String,Object> stockDetial = new HashMap<>();
//                    stockDetial.put("skuId",datum.getSkuId());
//                    stockDetial.put("number",details.getNumber());
//                    datum.setStockDetails(stockDetial);
//                }
//            }
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
