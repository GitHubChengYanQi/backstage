package cn.atsoft.dasheng.purchase.model.result;

import cn.atsoft.dasheng.core.util.ToolUtil;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

@Data
@ApiModel
public class ViewResult {
    private String  month;
    private String  year;
    private Integer sellerCount;
    private Integer skuCount;
    private Long purchaseNumber;
    private Long totalPrice;
    private Double totalAmount;
    private Date createTime;
    private Integer inStockCount;
    private Integer inStockRate;
    private Long deficientPrice;
    private Long paymentPrice;


    public Double getTotalPrice() {
        if (ToolUtil.isEmpty(deficientPrice) || totalPrice.equals(0L)){
            return null;
        }
        return BigDecimal.valueOf(totalPrice).divide(BigDecimal.valueOf(100),2, RoundingMode.HALF_UP).doubleValue();
    }
    public Double getDeficientPrice() {
        if (ToolUtil.isEmpty(deficientPrice) || deficientPrice.equals(0L)){
            return null;
        }
        return BigDecimal.valueOf(deficientPrice).divide(BigDecimal.valueOf(100),2, RoundingMode.HALF_UP).doubleValue();
    }

    public Double getPaymentPrice() {
        if (ToolUtil.isEmpty(paymentPrice) || paymentPrice.equals(0L)){
            return null;
        }
        return BigDecimal.valueOf(paymentPrice).divide(BigDecimal.valueOf(100),2, RoundingMode.HALF_UP).doubleValue();
    }

    public Double getTotalAmount() {
        if (ToolUtil.isEmpty(totalAmount) || totalAmount.equals(0L)){
            return null;
        }
        return BigDecimal.valueOf(totalAmount).divide(BigDecimal.valueOf(100),2, RoundingMode.HALF_UP).doubleValue();
    }

}
