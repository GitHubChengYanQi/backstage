package cn.atsoft.dasheng.app.model.request;

import cn.atsoft.dasheng.app.model.result.StorehouseResult;
import cn.atsoft.dasheng.orCode.model.result.StoreHouseRequest;
import lombok.Data;

@Data
public class StoreHouseAndSkuNumber {
    private Long skuId;
    private StorehouseResult storehouseResult;
    private Long storehouseId;
    private Long number;
}
