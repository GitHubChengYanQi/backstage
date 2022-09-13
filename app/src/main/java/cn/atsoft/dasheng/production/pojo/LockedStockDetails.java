package cn.atsoft.dasheng.production.pojo;

import lombok.Data;

@Data
public class LockedStockDetails {
    private Long skuId;
    private Long brandId;
    private Integer number;
}
