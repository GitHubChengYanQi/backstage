package cn.atsoft.dasheng.erp.model.result;

import lombok.Data;
import org.omg.CORBA.PRIVATE_MEMBER;

import java.util.Date;
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
    /**
     * 到货时间
     */
    private Date receivedDate;

    /**
     * 有效日期
     */
    private Date effectiveDate;

    /**
     * 生产日期
     */
    private Date manufactureDate;

    /**
     * 批号
     */
    private String lotNumber;

    /**
     * 流水号
     */
    private String serialNumber;
}
