package cn.atsoft.dasheng.goods.sku.model;


import cn.atsoft.dasheng.goods.classz.entity.RestAttribute;
import cn.atsoft.dasheng.goods.classz.model.RestAttributes;
import cn.atsoft.dasheng.goods.classz.model.RestValues;
import lombok.Data;

@Data
public class RestSkuJson {
    private RestAttributes attribute;
    private RestValues values;
}
