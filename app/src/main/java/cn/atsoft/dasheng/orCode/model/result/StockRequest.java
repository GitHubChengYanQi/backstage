package cn.atsoft.dasheng.orCode.model.result;

import cn.atsoft.dasheng.app.model.result.StockResult;
import lombok.Data;

@Data
public class StockRequest {
    private String type;
    private StockResult result;
}
