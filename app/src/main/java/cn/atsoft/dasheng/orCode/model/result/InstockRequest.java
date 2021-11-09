package cn.atsoft.dasheng.orCode.model.result;

import cn.atsoft.dasheng.app.model.result.InstockResult;
import lombok.Data;

@Data
public class InstockRequest {
    private String type;
    private InstockResult result;
}
