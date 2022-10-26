package cn.atsoft.dasheng.app.model.request;

import lombok.Data;

@Data
public class StockDetailView {
    private Long skuId;
    private Long brandId;
    private Integer number;
}
