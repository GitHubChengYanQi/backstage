package cn.atsoft.dasheng.goods.spu.model;

import cn.atsoft.dasheng.goods.classz.model.RestAttributes;
import cn.atsoft.dasheng.goods.sku.model.params.RestSkuParam;
import lombok.Data;

import java.util.List;

@Data
public class RestSpuAttribute {
    private List<RestAttributes>  spuRequests;
    private List<RestSkuParam> values;
}
