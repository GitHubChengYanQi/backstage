package cn.atsoft.dasheng.production.model.request;

import lombok.Data;

@Data
public class StoreHouseNameAndSkuNumber {
    private Long storehousePositionsId;
    private String storeHousePositionsName;
    private Integer number;
}
