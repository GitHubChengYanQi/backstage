package cn.atsoft.dasheng.erp.model.result;

import cn.atsoft.dasheng.erp.entity.AttributeValues;
import cn.atsoft.dasheng.erp.entity.ItemAttribute;
import lombok.Data;

import java.util.List;

@Data
public class CategoryRequest {
    private ItemAttribute attribute;
    private List<AttributeValues> value;
}
