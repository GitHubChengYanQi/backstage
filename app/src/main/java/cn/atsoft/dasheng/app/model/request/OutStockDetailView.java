package cn.atsoft.dasheng.app.model.request;

import cn.atsoft.dasheng.app.model.result.BrandResult;
import cn.atsoft.dasheng.erp.model.result.SkuSimpleResult;
import lombok.Data;

@Data
public class OutStockDetailView {

    private SkuSimpleResult skuResult;
    private Long skuId;
    private Long brandId;
    private BrandResult brandResult;
    private Integer pickSkuCount;
    private Integer pickNumCount;
    private Integer outSkuCount;
    private Integer outNumCount;
}
