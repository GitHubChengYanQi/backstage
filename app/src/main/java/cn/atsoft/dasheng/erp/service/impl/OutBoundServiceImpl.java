package cn.atsoft.dasheng.erp.service.impl;

import cn.atsoft.dasheng.app.entity.Stock;
import cn.atsoft.dasheng.app.entity.Storehouse;
import cn.atsoft.dasheng.app.service.StockService;
import cn.atsoft.dasheng.app.service.StorehouseService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.entity.ApplyDetails;
import cn.atsoft.dasheng.erp.entity.OutstockListing;
import cn.atsoft.dasheng.erp.service.ApplyDetailsService;
import cn.atsoft.dasheng.erp.service.OutBoundService;
import cn.atsoft.dasheng.erp.service.OutstockListingService;
import cn.atsoft.dasheng.model.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Override
    public String judgeOutBound(Long outstockOrderId, Long stockHouseId) {
        List<OutstockListing> outstockListings = outstockListingService.lambdaQuery().in(OutstockListing::getOutstockOrderId, outstockOrderId).list();

        List<Stock> stocks = stockService.lambdaQuery().in(Stock::getStorehouseId, stockHouseId).list();
        if (ToolUtil.isEmpty(stocks)) {
            throw new ServiceException(500, "此仓库没有物品");
        }
        if (ToolUtil.isEmpty(outstockListings)) {
            throw new ServiceException(500, "没有此清单");
        }


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
                    break;
                }else {
                    if (i == stocks.size() ){
                        throw new ServiceException(500, "没有这个商品");
                    }
                }
            }
        }

        return "出库成功";
    }
}
