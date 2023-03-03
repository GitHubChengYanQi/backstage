package cn.atsoft.dasheng.goods.classz.model;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel
public class RestAttributeValueInSpu {
    private Long attributeId;
    private Long id;
    private String name;
}
