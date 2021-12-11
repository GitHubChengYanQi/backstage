package cn.atsoft.dasheng.erp.model.result;

import lombok.Data;
import org.omg.CORBA.PRIVATE_MEMBER;

import java.util.List;

@Data
public class InstockRequest {
    private Long brandId;
    private Long itemId;
    private Long number;
    private Long barcode;
    private Integer costprice;
    private Integer sellingPrice;
    private Long skuId;
    private Long qualityTaskDetailId;
    private Long instockNumber;
}
