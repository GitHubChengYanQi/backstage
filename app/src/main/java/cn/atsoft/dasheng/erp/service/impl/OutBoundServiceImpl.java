package cn.atsoft.dasheng.erp.service.impl;

import cn.atsoft.dasheng.app.entity.Stock;
import cn.atsoft.dasheng.app.service.StockService;
import cn.atsoft.dasheng.app.service.StorehouseService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.entity.ApplyDetails;
import cn.atsoft.dasheng.erp.service.ApplyDetailsService;
import cn.atsoft.dasheng.erp.service.OutBoundService;
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

    @Override
    public String judgeOutBound(Long applyId, Long stockHouseId) {
        List<ApplyDetails> applyDetails = applyDetailsService.lambdaQuery().in(ApplyDetails::getOutstockApplyId, applyId).list();

        List<Stock> stocks = stockService.lambdaQuery().in(Stock::getStorehouseId, stockHouseId).list();
        if (ToolUtil.isNotEmpty(applyDetails) && ToolUtil.isNotEmpty(stocks)) {
            for (ApplyDetails applyDetail : applyDetails) {
                for (Stock stock : stocks) {
                    if (applyDetail.getBrandId().equals(stock.getBrandId()) && applyDetail.getItemId().equals(stock.getItemId())) {
                        if (applyDetail.getNumber() > stock.getInventory()) {
                            throw new ServiceException(500, "当前库存数量不足");
                        }
                    }
                }
            }

        }

        return null;
    }
}
