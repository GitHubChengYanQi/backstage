package cn.atsoft.dasheng.purchase.pojo;

import cn.atsoft.dasheng.erp.model.result.SkuResult;
import cn.atsoft.dasheng.purchase.model.result.PurchaseListingResult;
import lombok.Data;

import java.util.List;

@Data
public class ListingPlan {
    private Long skuId;
    private Long number;
    private SkuResult skuResult;
    private List<PurchaseListingResult> children;
}
