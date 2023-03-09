package cn.atsoft.dasheng.app.model.request;

import cn.atsoft.dasheng.app.entity.ErpPartsDetail;
import cn.atsoft.dasheng.app.model.result.ErpPartsDetailResult;
import cn.atsoft.dasheng.erp.model.result.SkuSimpleResult;
import lombok.Data;

import java.util.List;

@Data
public class InStockByBom {
    private Long bomId;

    private Long skuId;

    private Double number;

    private SkuSimpleResult skuResult;

    private List<ErpPartsDetailResult> detailList;

}
