package cn.atsoft.dasheng.orCode.model.result;

import cn.atsoft.dasheng.erp.model.result.SpuResult;
import lombok.Data;

@Data
public class SpuRequest {
    private String type;
    private SpuResult result;
}
