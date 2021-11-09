package cn.atsoft.dasheng.orCode.model.result;

import cn.atsoft.dasheng.app.model.result.StorehouseResult;
import lombok.Data;

@Data
public class StoreHouseRequest {
    private String type;
    private StorehouseResult result;
}
