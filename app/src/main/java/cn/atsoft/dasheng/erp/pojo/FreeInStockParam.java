package cn.atsoft.dasheng.erp.pojo;

import lombok.Data;

@Data
public class FreeInStockParam {
    private Long number;
    private Long storeHouseId;
    private Long codeId;
    private Long positionsId;
}
