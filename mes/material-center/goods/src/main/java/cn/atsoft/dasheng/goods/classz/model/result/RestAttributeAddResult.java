package cn.atsoft.dasheng.goods.classz.model.result;

import cn.atsoft.dasheng.goods.classz.entity.RestAttributeValues;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class RestAttributeAddResult {
    @ApiModelProperty("属性id")
    private Long attributeId;
    @ApiModelProperty("属性值集合")
    List<RestAttributeValues> attributeValues;
}
