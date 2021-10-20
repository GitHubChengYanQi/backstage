package cn.atsoft.dasheng.erp.model.params;

import lombok.Data;

import java.util.List;

@Data
public class SpuRequest {
    private Integer attributeId;
    private List<AttributeValuesParam> attributeValuesParams;
}
