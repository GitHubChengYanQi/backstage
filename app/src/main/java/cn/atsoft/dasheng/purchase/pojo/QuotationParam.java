package cn.atsoft.dasheng.purchase.pojo;

import cn.atsoft.dasheng.purchase.model.params.PurchaseQuotationParam;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class QuotationParam {
    @NotNull
    private List<PurchaseQuotationParam> quotationParams;

    private Long CustomerId;
    @NotNull
    private String source;
    @NotNull
    private Long sourceId;

}
