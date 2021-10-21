package cn.atsoft.dasheng.erp.model.params;

import lombok.Data;

@Data
public class SkuValuesRequest {
    private Long attributeId;
    private String valueName;
    private String attributeName;
    private Long attributeValueId;
}
