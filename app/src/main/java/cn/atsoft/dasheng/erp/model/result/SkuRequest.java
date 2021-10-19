package cn.atsoft.dasheng.erp.model.result;

import cn.atsoft.dasheng.erp.entity.AttributeValues;
import cn.atsoft.dasheng.erp.entity.ItemAttribute;
import lombok.Data;

import java.util.List;

@Data
public class SkuRequest {
    private ItemAttribute attribute;
    private List<AttributeValues> value;
}
