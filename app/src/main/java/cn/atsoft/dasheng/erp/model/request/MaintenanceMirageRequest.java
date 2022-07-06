package cn.atsoft.dasheng.erp.model.request;

import cn.atsoft.dasheng.erp.model.result.SkuSimpleResult;
import cn.atsoft.dasheng.erp.model.result.StorehousePositionsResult;
import cn.atsoft.dasheng.orCode.model.result.StoreHousePositionsRequest;
import lombok.Data;

import java.util.List;

@Data
public class MaintenanceMirageRequest {
   private StorehousePositionsResult storehousePositionsResult;
   private List<SkuSimpleResult> skuSimpleResultList;
}
