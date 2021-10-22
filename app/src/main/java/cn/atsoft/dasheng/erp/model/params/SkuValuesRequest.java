package cn.atsoft.dasheng.erp.model.params;

import lombok.Data;

import java.util.List;

@Data
public class SkuValuesRequest {
    List<ItemAttributeParam> attributeParams;
}
