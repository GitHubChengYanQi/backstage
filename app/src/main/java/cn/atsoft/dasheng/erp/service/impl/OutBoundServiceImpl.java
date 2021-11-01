package cn.atsoft.dasheng.erp.service.impl;

import cn.atsoft.dasheng.app.entity.*;
import cn.atsoft.dasheng.app.model.params.DeliveryParam;
import cn.atsoft.dasheng.app.model.params.OutstockOrderParam;
import cn.atsoft.dasheng.app.service.*;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.entity.ApplyDetails;
import cn.atsoft.dasheng.erp.entity.OutstockApply;
import cn.atsoft.dasheng.erp.entity.OutstockListing;
import cn.atsoft.dasheng.erp.model.params.OutstockApplyParam;
import cn.atsoft.dasheng.erp.service.ApplyDetailsService;
import cn.atsoft.dasheng.erp.service.OutBoundService;
import cn.atsoft.dasheng.erp.service.OutstockApplyService;
import cn.atsoft.dasheng.erp.service.OutstockListingService;
import cn.atsoft.dasheng.model.exception.ServiceException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class OutBoundServiceImpl implements OutBoundService {
    @Autowired
    private ApplyDetailsService applyDetailsService;
    @Autowired
    private StockService stockService;
    @Autowired
    private OutstockListingService outstockListingService;
    @Autowired
    private StockDetailsService stockDetailsService;
    @Autowired
    private OutstockService outstockService;
    @Autowired
    private OutstockOrderService outstockOrderService;
    @Autowired
    private OutstockApplyService outstockApplyService;
    @Autowired
    private DeliveryDetailsService deliveryDetailsService;
    @Autowired
    private DeliveryService deliveryService;


    @Override
    public String judgeOutBound(Long outstockOrderId, Long stockHouseId) {

        OutstockOrder outstockOrder = outstockOrderService.lambdaQuery().eq(OutstockOrder::getOutstockOrderId, outstockOrderId).one();
        if (outstockOrder.getState() != 0) {
            throw new ServiceException(500, "已出库！");
        }

        Long end = 0L;
        List<OutstockListing> outstockListings = outstockListingService.lambdaQuery()
                .in(OutstockListing::getOutstockOrderId, outstockOrderId)
                .list();

        List<Stock> stocks = stockService.lambdaQuery().in(Stock::getStorehouseId, stockHouseId).list();
        if (ToolUtil.isEmpty(stocks)) {
            throw new ServiceException(500, "此仓库没有物品，请确认库存！");
        }
        if (ToolUtil.isEmpty(outstockListings)) {
            throw new ServiceException(500, "没有此清单");
        }

        List<Stock> sendOutstock = new ArrayList<>();
        List<Stock> stocksList = new ArrayList<>();
        List<OutstockApply> outstockApplies = new ArrayList<>();
        for (OutstockListing outstockListing : outstockListings) {
            int i = 0;
            for (Stock stock : stocks) {
                i++;
                if (outstockListing.getBrandId() != null &&
                        outstockListing.getSkuId() != null &&
                        stock.getBrandId() != null &&
                        stock.getSkuId() != null &&
                        stock.getBrandId().equals(outstockListing.getBrandId()) &&
                        stock.getSkuId().equals(outstockListing.getSkuId())) {
                    Long number = outstockListing.getNumber();
                    Long inventory = stock.getInventory();
                    if (inventory < number) {
                        throw new ServiceException(500, "产品数量不足，请确认数量！");
                    }
                    long l = number;
                    stock.setInventory(l);
                    sendOutstock.add(stock);
                    Stock updateStock = new Stock();
                    ToolUtil.copyProperties(stock, updateStock);
                    end = inventory - number;
                    updateStock.setInventory(end);

                    stocksList.add(updateStock);

                    QueryWrapper<OutstockApply> outstockApplyQueryWrapper = new QueryWrapper<>();
                    outstockApplyQueryWrapper.in("outstock_apply_id", outstockListing.getOutstockApplyId());
                    List<OutstockApply> list = outstockApplyService.list(outstockApplyQueryWrapper);
                    if (list.size() > 0) {
                        for (OutstockApply outstockApply : list) {
                            outstockApplies.add(outstockApply);
                        }
                    }

                    break;
                } else {
                    if (i == stocks.size()) {
                        throw new ServiceException(500, "没有这个商品");
                    }
                }
            }
        }


        List<Outstock> Outstocks = new ArrayList<>();
        List<StockDetails> stockDetailsList = new ArrayList<>();
        StockDetails stockDetails = new StockDetails();
        for (Stock stock : sendOutstock) {

            for (int i = 0; i < stock.getInventory(); i++) {
                stockDetails.setStage(2);
                stockDetails.setStockId(stock.getStockId());
                stockDetails.setSkuId(stock.getSkuId());
                stockDetails.setBrandId(stock.getBrandId());

                List<StockDetails> list = stockDetailsService.lambdaQuery().in(StockDetails::getStockId, stock.getStockId())
                        .in(StockDetails::getBrandId, stock.getBrandId())
                        .in(StockDetails::getSkuId, stock.getSkuId())
                        .orderByAsc(StockDetails::getCreateTime)
                        .in(StockDetails::getStage, 1)
                        .list();

                StockDetails details = list.get(i);
                details.setStage(2);
                stockDetailsList.add(details);
                Outstock outstock = new Outstock();
                outstock.setBrandId(details.getBrandId());
                outstock.setSkuId(details.getSkuId());
                outstock.setStockId(details.getStockId());
                outstock.setStorehouseId(details.getStorehouseId());
                outstock.setOutstockOrderId(outstockOrderId);
                outstock.setStockItemId(details.getStockItemId());
                Outstocks.add(outstock);


            }
        }
        outstockService.saveBatch(Outstocks);
        List<Stock> updateStocks = new ArrayList<>();
        stockDetailsService.updateBatchById(stockDetailsList);

        for (Stock stock : stocksList) {
            updateStocks.add(stock);
        }
        stockService.updateBatchById(updateStocks);

//        List<StockDetails> details = stockDetailsService.lambdaQuery().
//                in(StockDetails::getStockId, stockDetails.getStockId())
//                .and(i -> i.in(StockDetails::getBrandId, stockDetails.getBrandId())
//                        .and(j -> j.in(StockDetails::getItemId, stockDetails.getItemId()))
//                        .and(z -> z.in(StockDetails::getStage, 1))).list();


        outstockOrder.setState(1);
        outstockOrder.setStorehouseId(stockHouseId);
        OutstockOrderParam outstockOrderParam = new OutstockOrderParam();
        ToolUtil.copyProperties(outstockOrder, outstockOrderParam);
        outstockOrderService.update(outstockOrderParam);
        ;
        for (OutstockApply outstockApply : outstockApplies) {
            OutstockApplyParam outstockApplyParam = new OutstockApplyParam();
            outstockApplyParam.setOutstockApplyId(outstockApply.getOutstockApplyId());
            outstockApplyParam.setApplyState(3);
            outstockApplyService.update(outstockApplyParam);
        }


        return "出库成功";
    }

    @Override
    @Transactional
    public String aKeyDelivery(OutstockApplyParam outstockApplyParam) {

        Long outstockApplyId = outstockApplyParam.getOutstockApplyId();
        QueryWrapper<OutstockListing> listingQueryWrapper = new QueryWrapper<>();
        listingQueryWrapper.in("outstock_apply_id", outstockApplyId);
        List<OutstockListing> list = outstockListingService.list(listingQueryWrapper);
        Long deliveryId = list.get(0).getDeliveryId();

        Delivery delivery = deliveryService.lambdaQuery().eq(Delivery::getDeliveryId, deliveryId).one();
        DeliveryParam deliveryParam = new DeliveryParam();
        ToolUtil.copyProperties(delivery, deliveryParam);
        deliveryParam.setDeliveryWay(outstockApplyParam.getDeliveryWay());
        if (outstockApplyParam.getOutstockApplyId() != null && outstockApplyParam.getDeliveryWay() == 0) {
            deliveryParam.setLogisticsCompany(outstockApplyParam.getLogisticsCompany());
            deliveryParam.setLogisticsNumber(outstockApplyParam.getLogisticsNumber());
        } else if (outstockApplyParam.getOutstockApplyId() != null && outstockApplyParam.getDeliveryWay() == 1) {
            deliveryParam.setDriverName(outstockApplyParam.getDriverName());
            deliveryParam.setDriverPhone(outstockApplyParam.getDriverPhone());
            deliveryParam.setLicensePlate(outstockApplyParam.getLicensePlate());
        }
        deliveryService.update(deliveryParam);

        List<ApplyDetails> applyDetails = applyDetailsService.lambdaQuery()
                .in(ApplyDetails::getOutstockApplyId, outstockApplyParam.getOutstockApplyId())
                .list();
        if (ToolUtil.isEmpty(applyDetails)) {
            throw new ServiceException(500, "请确定发货申请明细");
        }
        for (int i = 0; i < applyDetails.size(); i++) {
            boolean f = backItem(outstockApplyParam.getStockId(),
                    applyDetails.get(i).getBrandId(),
                    applyDetails.get(i).getSkuId(),
                    applyDetails.get(i).getNumber());
            if (!f) {
                throw new ServiceException(500, "此仓库没有物品，请确认库存！");
            }
        }
        long l = -1L;

        OutstockOrder outstockOrder = outstockOrderService.lambdaQuery().eq(OutstockOrder::getOutstockApplyId, outstockApplyParam.getOutstockApplyId()).one();
        if (ToolUtil.isEmpty(outstockOrder)) {
            throw new ServiceException(500, "产品不存在！");
        }

        for (ApplyDetails applyDetail : applyDetails) {


            List<Stock> stocks = stockService.lambdaQuery().in(Stock::getStorehouseId, outstockApplyParam.getStockId())
                    .and(i -> i.in(Stock::getBrandId, applyDetail.getBrandId()))
                    .and(j -> j.in(Stock::getSkuId, applyDetail.getSkuId())).list();

            List<Stock> stockList = new ArrayList<>();
            if (ToolUtil.isEmpty(stocks)) {
                throw new ServiceException(500, "请检查库存是否有此物品");
            }


            for (Stock stock : stocks) {
                l = stock.getInventory() - applyDetail.getNumber();
                stock.setInventory(l);
                stockList.add(stock);
                List<StockDetails> details = stockDetailsService.lambdaQuery().in(StockDetails::getStockId, stock.getStockId())
                        .and(i -> i.in(StockDetails::getBrandId, stock.getBrandId()))
                        .and(i -> i.in(StockDetails::getSkuId, stock.getSkuId()))
                        .and(i -> i.in(StockDetails::getStage, 1))
                        .list();

                if (ToolUtil.isEmpty(details)) {
                    throw new ServiceException(500, "请确认库存");
                }
                if (l >= 0) {
                    List<Outstock> outstocks = new ArrayList<>();
                    List<DeliveryDetails> deliveryDetailsList = new ArrayList<>();
                    if (applyDetail.getNumber() == 0) {
                        throw new ServiceException(500, "出库数量不能为0");
                    }
                    for (int i = 0; i < applyDetail.getNumber(); i++) {
                        StockDetails stockDetails = details.get(i);
                        stockDetails.setStage(3);
                        DeliveryDetails deliveryDetails = new DeliveryDetails();
                        deliveryDetails.setStockItemId(stockDetails.getStockItemId());
                        deliveryDetails.setSkuId(stockDetails.getSkuId());
                        deliveryDetails.setBrandId(stockDetails.getBrandId());
                        deliveryDetails.setDeliveryId(deliveryId);
                        deliveryDetailsList.add(deliveryDetails);

                        Outstock outstock = new Outstock();
                        outstock.setBrandId(stockDetails.getBrandId());
                        outstock.setSkuId(stockDetails.getSkuId());
                        outstock.setStorehouseId(stockDetails.getStorehouseId());
                        outstock.setOutstockOrderId(outstockOrder.getOutstockOrderId());
                        outstock.setStockId(stockDetails.getStockId());
                        outstock.setStockItemId(stockDetails.getStockItemId());
                        outstock.setState(1L);
                        outstocks.add(outstock);
                    }
                    deliveryDetailsService.saveBatch(deliveryDetailsList);
                    stockDetailsService.updateBatchById(details);
                    outstockService.saveBatch(outstocks);
                }


            }

            stockService.updateBatchById(stockList);

            OutstockApply outstockApply = outstockApplyService.lambdaQuery()
                    .eq(OutstockApply::getOutstockApplyId, applyDetail.getOutstockApplyId())
                    .one();

            outstockApply.setApplyState(3);
            OutstockApplyParam outstockApplyParamUpdate = new OutstockApplyParam();
            ToolUtil.copyProperties(outstockApply, outstockApplyParamUpdate);
            outstockApplyService.update(outstockApplyParamUpdate);
        }


        outstockOrder.setState(2);
        outstockOrder.setStorehouseId(outstockApplyParam.getStockId());
        OutstockOrderParam outstockOrderParam = new OutstockOrderParam();
        ToolUtil.copyProperties(outstockOrder, outstockOrderParam);
        outstockOrderService.update(outstockOrderParam);
        return null;
    }

    boolean backItem(Long id, Long brandId, Long skuId, Long number) {
        boolean f = false;
        List<Stock> stocks = stockService.lambdaQuery().in(Stock::getStorehouseId, id).list();
        for (Stock stock : stocks) {
            if (stock.getBrandId().equals(brandId) && stock.getSkuId().equals(skuId)) {
                if (stock.getInventory() < number) {
                    throw new ServiceException(500, "产品数量不足，请确认数量！");
                }
                return true;
            }
        }
        if (ToolUtil.isEmpty(stocks)) {
            return false;
        }
        return f;
    }
}
