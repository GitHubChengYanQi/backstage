package cn.atsoft.dasheng.erp.pojo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class FreeInStockParam {
    @NotNull
    private Long storeHouseId;
    @NotNull
    private List<Long> inkindIds;
    @NotNull
    private Long positionsId;

    private String type;

    private List<PositionsInStock> inStocks;  //库位入库

    @Data
    public static class PositionsInStock {
        private Long inStockListId;
        private Long inkind;
        private Long positionsId;
        private Long storeHouseId;
    }
}
