package cn.atsoft.dasheng.production.model.request;

import cn.atsoft.dasheng.erp.model.result.SkuResult;
import cn.atsoft.dasheng.erp.model.result.StorehousePositionsResult;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class SkuInStockTree {
    private Map<String,Object> skuMap;
    private StorehousePositionsResult storehousePositionsResult;
    private List<SkuInStockTree> children;
}
