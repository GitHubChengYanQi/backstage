package cn.atsoft.dasheng.purchase.model.request;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProcurementDetailSkuTotal {
    private Long skuId;
    private Long total;
}
