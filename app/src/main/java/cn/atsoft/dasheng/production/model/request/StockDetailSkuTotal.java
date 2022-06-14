package cn.atsoft.dasheng.production.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StockDetailSkuTotal {
    private Long skuId;
    private Long number;
    private Long brandId;
}
