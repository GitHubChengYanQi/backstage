package cn.atsoft.dasheng.erp.model.result;

import cn.atsoft.dasheng.erp.entity.AttributeValues;
import cn.atsoft.dasheng.erp.entity.ItemAttribute;
import lombok.Data;

@Data
public class BackSku {
    private AttributeValues attributeValues;
    private ItemAttribute itemAttribute;
}
