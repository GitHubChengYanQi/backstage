package cn.atsoft.dasheng.orCode.model.result;

import cn.atsoft.dasheng.erp.model.result.SkuResult;
import lombok.Data;

@Data
public class SkuRequest {
    private String type;
    private SkuResult result;
}
