package cn.atsoft.dasheng.view.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * VIEW
 * </p>
 *
 * @author 
 * @since 2022-05-20
 */
@TableName("sku_supply_view")
public class SkuSupplyView implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("sku_id")
    private Long skuId;

    /**
     * 客户id
     */
    @TableField("customer_id")
    private Long customerId;

    /**
     * 客户名称
     */
    @TableField("customer_name")
    private String customerName;

    /**
     * 品牌id
     */
    @TableField("brand_id")
    private Long brandId;

    /**
     * 品牌名称
     */
    @TableField("brand_name")
    private String brandName;


    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    @Override
    public String toString() {
        return "SkuSupplyView{" +
        "skuId=" + skuId +
        ", customerId=" + customerId +
        ", customerName=" + customerName +
        ", brandId=" + brandId +
        ", brandName=" + brandName +
        "}";
    }
}
