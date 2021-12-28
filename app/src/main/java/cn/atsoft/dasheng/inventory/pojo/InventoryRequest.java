package cn.atsoft.dasheng.inventory.pojo;

import lombok.Data;

@Data
public class InventoryRequest {
    private Long qrcodeId;
    private Long storeHouseId;
    private Long inventoryId;
}
