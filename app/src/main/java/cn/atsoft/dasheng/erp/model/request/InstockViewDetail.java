package cn.atsoft.dasheng.erp.model.request;

import cn.atsoft.dasheng.app.model.result.BrandResult;
import cn.atsoft.dasheng.erp.model.result.SkuSimpleResult;
import lombok.Data;

@Data
public class InstockViewDetail {
    private Long instockOrderId;
    private Long listsId;
    private Long skuId;
    private Long brandId;
    private Long customerId;
    private Integer listNum;
    private Integer listCount;
    private Integer errorCount;
    private Integer errorNum;
    private Integer logNum;
    private Integer logCount;
    private SkuSimpleResult skuResult;
    private BrandResult brandResult;
}
