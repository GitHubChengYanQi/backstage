package cn.atsoft.dasheng.erp.model.request;

import lombok.Data;

@Data
public class InstockViewDetail {
    private Long instockOrderId;
    private Long listsId;
    private Long skuId;
    private Long brandId;
    private Long customerId;
    private Integer listNum;
    private Integer errorNum;
    private Integer logNum;
}
