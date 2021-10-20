package cn.atsoft.dasheng.erp.model.result;

import lombok.Data;

@Data
public class ItemAttributeValueResult {
    private Long valueId;
    private String valueName;
    private String attributeName;
    private Long attributeId;
}
