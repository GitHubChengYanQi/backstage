package cn.atsoft.dasheng.goods.sku.model.params;

import lombok.Data;

@Data
public class RestInitialNumber {
    private Long skuId;

    private Integer number;

    private Long tenantId;

    private Long price;

    private Integer type;

}
