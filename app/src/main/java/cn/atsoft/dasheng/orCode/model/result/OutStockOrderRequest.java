package cn.atsoft.dasheng.orCode.model.result;

import cn.atsoft.dasheng.app.model.result.OutstockOrderResult;
import cn.atsoft.dasheng.app.model.result.OutstockResult;
import lombok.Data;

@Data
public class OutStockOrderRequest {
    private String type;
    private OutstockOrderResult result;
}
