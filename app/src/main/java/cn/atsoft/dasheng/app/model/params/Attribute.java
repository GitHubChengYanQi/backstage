package cn.atsoft.dasheng.app.model.params;

import cn.atsoft.dasheng.erp.entity.AttributeValues;
import lombok.Data;

import java.util.List;

@Data
public class Attribute {
    private String attributeId;
    private String attribute;
    private Long id;
    private List<Values> attributeValues;
}
