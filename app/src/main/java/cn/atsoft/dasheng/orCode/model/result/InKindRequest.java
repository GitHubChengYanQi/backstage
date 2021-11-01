package cn.atsoft.dasheng.orCode.model.result;

import lombok.Data;

@Data
public class InKindRequest {
    private Long codeId;
    private Long skuId;
    private String type;
    private Long spuId;
}
