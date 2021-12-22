package cn.atsoft.dasheng.purchase.pojo;

import cn.atsoft.dasheng.purchase.model.params.PurchaseQuotationParam;
import lombok.Data;

import java.util.List;

@Data
public class QuotationParam {
    private List<PurchaseQuotationParam> quotationParams;
}
