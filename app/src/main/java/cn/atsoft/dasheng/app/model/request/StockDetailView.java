package cn.atsoft.dasheng.app.model.request;

import cn.atsoft.dasheng.erp.model.result.SkuSimpleResult;
import lombok.Data;

@Data
public class StockDetailView {
    private Long skuId;
    private Long brandId;
    private Integer number;
    private SkuSimpleResult skuSimpleResult;

    private Long spuClassId;
    private Long materialId;
    private Long storehouseId;
    private Long customerId;


    //物料种类数量
    private Integer skuCount;
    //物料分类名称
    private String className;
    //材质名称
    private String materialName;
    //仓库名称
    private String storehouseName;
    //供应商名称
    private String customerName;
}
