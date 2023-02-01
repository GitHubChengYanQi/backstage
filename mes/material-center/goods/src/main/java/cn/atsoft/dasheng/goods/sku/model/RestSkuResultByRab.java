package cn.atsoft.dasheng.goods.sku.model;

import cn.atsoft.dasheng.goods.classz.model.RestAttributeInSpu;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class RestSkuResultByRab {
    private List<RestAttributeInSpu> tree;
    private List<Map<String,Object>> list;
}
