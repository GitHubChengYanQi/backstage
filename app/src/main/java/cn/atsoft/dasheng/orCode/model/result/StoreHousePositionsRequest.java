package cn.atsoft.dasheng.orCode.model.result;

import cn.atsoft.dasheng.erp.model.result.StorehousePositionsResult;
import lombok.Data;

@Data
public class StoreHousePositionsRequest {
    private String type;
    private StorehousePositionsResult result;
}
