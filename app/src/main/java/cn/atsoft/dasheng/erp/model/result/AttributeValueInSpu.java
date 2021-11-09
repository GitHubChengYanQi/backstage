package cn.atsoft.dasheng.erp.model.result;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel
public class AttributeValueInSpu {
    private Long attributeId;
    private Long id;
    private String name;
}
