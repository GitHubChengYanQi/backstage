package cn.atsoft.dasheng.app.model.params;

import cn.atsoft.dasheng.erp.entity.AttributeValues;
import cn.atsoft.dasheng.erp.entity.ItemAttribute;
import lombok.Data;

@Data
public class SpuAttribute {

    private Attribute attribute;
    private Values values;
}
