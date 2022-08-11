package cn.atsoft.dasheng.orCode.model.result;

import cn.atsoft.dasheng.erp.model.result.InstockOrderResult;
import lombok.Data;

@Data
public class InstockRequest {
    private String type;
    private InstockOrderResult result;
}
