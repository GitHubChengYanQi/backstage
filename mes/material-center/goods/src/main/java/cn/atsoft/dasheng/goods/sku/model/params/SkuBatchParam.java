package cn.atsoft.dasheng.goods.sku.model.params;

import lombok.Data;

import java.util.List;

@Data
public class SkuBatchParam {
    List<RestSkuParam> skuList;
}
