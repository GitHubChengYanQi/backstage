package cn.atsoft.dasheng.goods.classz.model;

import cn.atsoft.dasheng.goods.classz.entity.RestAttribute;
import cn.atsoft.dasheng.goods.classz.entity.RestAttributeValues;
import lombok.Data;

import java.util.List;

@Data
public class RavAndRabByRestClass {

    private List<RestAttributeValues> value;
    private RestAttribute attribute;
}
