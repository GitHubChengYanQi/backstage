package cn.atsoft.dasheng.erp.pojo;

import lombok.Data;

import java.util.List;

@Data
public class FreeInStockParam {
    private Long storeHouseId;
    private List<Long> codeIds;
    private Long positionsId;
}
