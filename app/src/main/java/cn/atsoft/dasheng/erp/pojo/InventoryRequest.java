package cn.atsoft.dasheng.inventory.pojo;

import lombok.Data;

import java.util.List;

@Data
public class  InventoryRequest {

    private List<InkindParam> inkindParams;
    private Long positionId;
    private Long storeHouseId;
    private List<outStockInkindParam> outStockInkindParams;
    @Data
    public class InkindParam {
        private Long inkindId;
        private Long number;
    }
    @Data
    public class outStockInkindParam{
        private Long inkindId;

    }
}
