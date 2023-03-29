package cn.atsoft.dasheng.app.model.params;

import lombok.Data;

@Data
public class InventoryCorrectionParam {
    private Long positionId;

    private Long skuId;

    private Long brandId;

    private Long customerId;

    private Long inkindId;

    private Integer number;
}
