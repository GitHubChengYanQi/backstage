package cn.atsoft.dasheng.purchase.model.result;

import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

@Data
public class ViewResult {
    private String  month;
    private String  year;
    private Integer sellerCount;
    private Integer skuCount;
    private Integer purchaseNumber;
    private Integer totalPrice;
    private Date craeteTime;


    public Double getTotalPrice() {
        return BigDecimal.valueOf(totalPrice).divide(BigDecimal.valueOf(100),2, RoundingMode.HALF_UP).doubleValue();
    }
}
