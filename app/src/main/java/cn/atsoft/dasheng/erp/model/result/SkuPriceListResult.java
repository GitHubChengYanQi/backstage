package cn.atsoft.dasheng.erp.model.result;

import lombok.Data;

@Data
public class SkuPriceListResult {

    private Long skuId;

    private double price;

    public double getPrice() {
        return price/100;
    }
}