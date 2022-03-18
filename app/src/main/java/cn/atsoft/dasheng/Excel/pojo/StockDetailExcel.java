package cn.atsoft.dasheng.Excel.pojo;

import cn.atsoft.dasheng.app.model.result.BrandResult;
import cn.atsoft.dasheng.app.model.result.CustomerResult;
import cn.atsoft.dasheng.erp.model.result.SkuResult;
import lombok.Data;

@Data
public class StockDetailExcel {
    private SkuResult skuResult;
    private Integer num;
    private String brandName;
    private Long brandId;
    private Long customerId;
    private Long customerName;
    private BrandResult brandResult;
    private CustomerResult customerResult;
    private Long storehousePositionsId;
    private Integer skuSum;






//    number AS "number",
//    customer_id AS "customerId",
//    sku_id AS "skuId",
//    storehouse_positions_id AS "storehousePositionsId",
//    brand_id AS "brandId",
//    storehouse_id AS "storehouseId",
//    stock_item_id AS "stockItemId",
//    stock_id AS "stockId",
//    display AS "display" ,
//    stage AS "stage",
//    sum(number) AS "skuSum"
}
