package cn.atsoft.dasheng.orCode.model.result;

import cn.atsoft.dasheng.app.model.result.OutstockResult;
import lombok.Data;

@Data
public class OutStockRequest {
    private String type;
    private OutstockResult outstockResult;
}
