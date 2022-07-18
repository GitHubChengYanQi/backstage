package cn.atsoft.dasheng.erp.model.result;

import cn.atsoft.dasheng.app.entity.StockDetails;
import lombok.Data;

import java.util.List;

@Data
public class MaintenanceSelectSku {
    private SkuSimpleResult skuResult;
    private List<StockDetails> stockDetails;
}
