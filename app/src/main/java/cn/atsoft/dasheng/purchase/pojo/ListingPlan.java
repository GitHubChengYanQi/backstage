package cn.atsoft.dasheng.purchase.pojo;

import cn.atsoft.dasheng.app.model.result.BrandResult;
import cn.atsoft.dasheng.erp.model.result.SkuResult;
import cn.atsoft.dasheng.purchase.model.result.PurchaseListingResult;
import lombok.Data;

import java.util.List;

@Data
public class ListingPlan {
    private Long skuId;
    private Long applyNumber;
    private Long stockNumber;
    private Long brandId;

    private SkuResult skuResult;
    private BrandResult brandResult;
    private List<PurchaseListingResult> children;


}
