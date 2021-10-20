package cn.atsoft.dasheng.erp.model.params;

import cn.atsoft.dasheng.erp.entity.AttributeValues;
import cn.atsoft.dasheng.erp.entity.ItemAttribute;
import lombok.Data;

import java.util.List;

@Data
public class CategoryRequest {
    private List<ItemAttributeParam>  spuRequests;
    private List<AttributeValues> value;
    private Long attributeId;
}
