package cn.atsoft.dasheng.erp.model.request;

import lombok.Data;

@Data
public class SkuBrandPositionNumber {
    private Long positoinId;
    private Long skuId;
    private Long brandId;
    private Integer number;
}
