package cn.atsoft.dasheng.orCode.model.result;

import cn.atsoft.dasheng.erp.model.params.InstockListParam;
import lombok.Data;

import java.util.List;

@Data
public class InKindRequest {

    private Long codeId;
    private Long Id;
    private Long inkindId;
    private String type;
    private Long number;
    private Long instockOrderId;
    private InstockListParam instockListParam;
    private Long brandId;
    private Integer costPrice;
    private Integer sellingPrice;
    private Long storehouse;
    private Long outstockOrderId;
    private Long outstockListingId;
    private List<Long> codeIds;  //批量二维码
    private List<BatchOutStockParam> batchOutStockParams;

    @Data
    public class BatchOutStockParam {
        private Long codeId;
        private Long number;
    }
}
