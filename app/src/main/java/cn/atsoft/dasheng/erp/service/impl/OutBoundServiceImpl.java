package cn.atsoft.dasheng.erp.service.impl;

import cn.atsoft.dasheng.app.entity.*;
import cn.atsoft.dasheng.app.model.params.OutstockOrderParam;
import cn.atsoft.dasheng.app.service.*;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.entity.ApplyDetails;
import cn.atsoft.dasheng.erp.entity.OutstockListing;
import cn.atsoft.dasheng.erp.model.params.OutstockApplyParam;
import cn.atsoft.dasheng.erp.service.ApplyDetailsService;
import cn.atsoft.dasheng.erp.service.OutBoundService;
import cn.atsoft.dasheng.erp.service.OutstockListingService;
import cn.atsoft.dasheng.model.exception.ServiceException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OutBoundServiceImpl implements OutBoundService {
    @Autowired
    private ApplyDetailsService applyDetailsService;
    @Autowired
    private StockService stockService;
    @Autowired
    private StorehouseService storehouseService;
    @Autowired
    private OutstockListingService outstockListingService;
    @Autowired
    private StockDetailsService stockDetailsService;
    @Autowired
    private OutstockService outstockService;
    @Autowired
    private OutstockOrderService outstockOrderService;


    @Override
    public String judgeOutBound(Long outstockOrderId, Long stockHouseId) {
        Long end = 0L;
        List<OutstockListing> outstockListings = outstockListingService.lambdaQuery()
                .in(OutstockListing::getOutstockOrderId, outstockOrderId)
                .list();

        List<Stock> stocks = stockService.lambdaQuery().in(Stock::getStorehouseId, stockHouseId).list();
        if (ToolUtil.isEmpty(stocks)) {
            throw new ServiceException(500, "此仓库没有物品");
        }
        if (ToolUtil.isEmpty(outstockListings)) {
            throw new ServiceException(500, "没有此清单");
        }

        List<Stock> sendOutstock = new ArrayList<>();
        List<Stock> stocksList = new ArrayList<>();
        for (OutstockListing outstockListing : outstockListings) {
            int i = 0;
            for (Stock stock : stocks) {
                i++;
                if (stock.getBrandId().equals(outstockListing.getBrandId()) && stock.getItemId().equals(outstockListing.getItemId())) {
                    Long number = outstockListing.getNumber();
                    Long inventory = stock.getInventory();
                    if (inventory < number) {
                        throw new ServiceException(500, "商品数量不足");
                    }
                    long l = number;
                    stock.setInventory(l);
                    sendOutstock.add(stock);
                    Stock updateStock = new Stock();
                    ToolUtil.copyProperties(stock, updateStock);
                    end = inventory - number;
                    updateStock.setInventory(end);

                    stocksList.add(updateStock);

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
                stockDetails.setItemId(stock.getItemId());
                stockDetails.setBrandId(stock.getBrandId());

                List<StockDetails> list = stockDetailsService.lambdaQuery().in(StockDetails::getStockId, stock.getStockId())
                        .in(StockDetails::getBrandId, stock.getBrandId())
                        .in(StockDetails::getItemId, stock.getItemId())
                        .orderByAsc(StockDetails::getCreateTime)
                        .in(StockDetails::getStage, 1)
                        .list();

                StockDetails details = list.get(i);
                details.setStage(2);
                stockDetailsList.add(details);
                Outstock outstock = new Outstock();
                outstock.setBrandId(details.getBrandId());
                outstock.setItemId(details.getItemId());
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


        OutstockOrder outstockOrder = outstockOrderService.lambdaQuery().eq(OutstockOrder::getOutstockOrderId, outstockOrderId).one();
        outstockOrder.setState(1);
        outstockOrder.setStorehouseId(stockHouseId);
        OutstockOrderParam outstockOrderParam = new OutstockOrderParam();
        ToolUtil.copyProperties(outstockOrder, outstockOrderParam);
        outstockOrderService.update(outstockOrderParam);
        return "出库成功";
    }

    @Override
    public String aKeyDelivery(OutstockApplyParam outstockApplyParam) {
        List<ApplyDetails> applyDetails = applyDetailsService.lambdaQuery()
                .in(ApplyDetails::getOutstockApplyId, outstockApplyParam.getOutstockApplyId())
                .list();
        if (ToolUtil.isEmpty(applyDetails)) {
            throw  new ServiceException(500,"请确定发货申请明细");
        }
        for (int i = 0; i < applyDetails.size(); i++) {
            boolean f = backItem(outstockApplyParam.getStockId(),
                    applyDetails.get(i).getBrandId(),
                    applyDetails.get(i).getItemId(),
                    applyDetails.get(i).getNumber());
            if (!f) {
                throw new ServiceException(500, "库存没有此产品");
            }
        }
        long l = -1L;

        for (ApplyDetails applyDetail : applyDetails) {
            List<Stock> stocks = stockService.lambdaQuery().in(Stock::getStorehouseId, outstockApplyParam.getStockId())
                    .and(i -> i.in(Stock::getBrandId, applyDetail.getBrandId()))
                    .and(j -> j.in(Stock::getItemId, applyDetail.getItemId())).list();

            List<Stock> stockList = new ArrayList<>();
            if (ToolUtil.isEmpty(stocks)) {
                throw  new ServiceException(500,"请检查库存是否有此物品");
            }
            for (Stock stock : stocks) {
                l = stock.getInventory() - applyDetail.getNumber();
                stock.setInventory(l);
                stockList.add(stock);
                List<StockDetails> details = stockDetailsService.lambdaQuery().in(StockDetails::getStockId, stock.getStockId())
                        .and(i -> i.in(StockDetails::getBrandId, stock.getBrandId()))
                        .and(i -> i.in(StockDetails::getItemId, stock.getItemId())).list();

                if (l >= 0) {
                    for (int i = 0; i < l; i++) {
                        StockDetails stockDetails = details.get(i);
                        stockDetails.setStage(3);
                    }
                    stockDetailsService.updateBatchById(details);
                }

            }
            stockService.updateBatchById(stockList);

        }

        OutstockOrder outstockOrder = outstockOrderService.lambdaQuery().eq(OutstockOrder::getOutstockApplyId, outstockApplyParam.getOutstockApplyId()).one();
        outstockOrder.setState(2);
        outstockOrder.setStorehouseId(outstockApplyParam.getStockId());
        OutstockOrderParam outstockOrderParam = new OutstockOrderParam();
        ToolUtil.copyProperties(outstockOrder, outstockOrderParam);
        outstockOrderService.update(outstockOrderParam);
        return null;
    }

    boolean backItem(Long id, Long brandId, Long itemId, Long number) {
        boolean f = false;
        List<Stock> stocks = stockService.lambdaQuery().in(Stock::getStorehouseId, id).list();
        for (Stock stock : stocks) {
            if (stock.getBrandId().equals(brandId) && stock.getItemId().equals(itemId)) {
                if (stock.getInventory() < number) {
                    throw new ServiceException(500, "商品数量不足");
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
