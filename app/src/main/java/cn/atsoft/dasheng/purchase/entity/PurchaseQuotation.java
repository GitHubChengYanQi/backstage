package cn.atsoft.dasheng.purchase.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;

/**
 * <p>
 * 采购报价表单
 * </p>
 *
 * @author Captain_Jazz
 * @since 2021-12-22
 */
@TableName("daoxin_purchase_quotation")
public class PurchaseQuotation implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 付款方式
     */
    @TableField("payment_method")
    private String paymentMethod;
    /**
     * 税额
     */
    @TableField("tax_price")
    private Long taxPrice;
    /**
     * 采购报价id
     */
    @TableId(value = "purchase_quotation_id", type = IdType.ID_WORKER)
    private Long purchaseQuotationId;

    /**
     * 物料id
     */
    @TableField("sku_id")
    private Long skuId;

    /**
     * 品牌id
     */
    @TableField("brand_id")
    private Long brandId;

    /**
     * 价格
     */
    @TableField("price")
    private Long price;

    /**
     * 供应商id
     */
    @TableField("customer_id")
    private Long customerId;

    /**
     * 报价有效期
     */
    @TableField("period_of_validity")
    private Date periodOfValidity;

    /**
     * 数量
     */
    @TableField("total")
    private Integer total;

    /**
     * 税前单价
     */
    @TableField("pre_tax")
    private Long preTax;

    /**
     * 运费价格
     */
    @TableField("freight")
    private Long freight;

    /**
     * 税后单价
     */
    @TableField("after_tax")
    private Long afterTax;

    /**
     * 是否包含运费
     */
    @TableField("is_freight")
    private Integer isFreight;

    /**
     * 来源
     */
    @TableField("source")
    private String source;

    /**
     * 来源id
     */
    @TableField("source_id")
    private Long sourceId;

    /**
     * 关联表id
     */
    @TableField("form_id")
    private Long formId;

    /**
     * 交付日期
     */
    @TableField("delivery_date")
    private Date deliveryDate;

    /**
     * 票据类型
     */
    @TableField("Invoice_type")
    private String InvoiceType;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 修改时间
     */
    @TableField(value = "update_time", fill = FieldFill.UPDATE)
    private Date updateTime;

    /**
     * 创建用户
     */
    @TableField(value = "create_user", fill = FieldFill.INSERT)
    private Long createUser;

    /**
     * 修改用户
     */
    @TableField(value = "update_user", fill = FieldFill.UPDATE)
    private Long updateUser;

    /**
     * 状态
     */
    @TableField("display")
    private Long display;

    /**
     * 部门id
     */
    @TableField("deptId")
    private Long deptId;

    /**
     * 主题
     */
    @TableField("theme")
    private String theme;

    /**
     * 来源
     */
    @TableField("origin")
    private String origin;

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }



    public Long getPurchaseQuotationId() {
        return purchaseQuotationId;
    }

    public void setPurchaseQuotationId(Long purchaseQuotationId) {
        this.purchaseQuotationId = purchaseQuotationId;
    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public Date getPeriodOfValidity() {
        return periodOfValidity;
    }

    public void setPeriodOfValidity(Date periodOfValidity) {
        this.periodOfValidity = periodOfValidity;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Long getPreTax() {
        return preTax;
    }

    public void setPreTax(Long preTax) {
        this.preTax = preTax;
    }

    public Long getFreight() {
        return freight;
    }

    public void setFreight(Long freight) {
        this.freight = freight;
    }

    public Long getAfterTax() {
        return afterTax;
    }

    public void setAfterTax(Long afterTax) {
        this.afterTax = afterTax;
    }

    public Integer getIsFreight() {
        return isFreight;
    }

    public void setIsFreight(Integer isFreight) {
        this.isFreight = isFreight;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Long getSourceId() {
        return sourceId;
    }

    public void setSourceId(Long sourceId) {
        this.sourceId = sourceId;
    }

    public Long getFormId() {
        return formId;
    }

    public void setFormId(Long fornId) {
        this.formId = fornId;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getInvoiceType() {
        return InvoiceType;
    }

    public void setInvoiceType(String InvoiceType) {
        this.InvoiceType = InvoiceType;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Long getCreateUser() {
        return createUser;
    }

    public void setCreateUser(Long createUser) {
        this.createUser = createUser;
    }

    public Long getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(Long updateUser) {
        this.updateUser = updateUser;
    }

    public Long getDisplay() {
        return display;
    }

    public void setDisplay(Long display) {
        this.display = display;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Long getTaxPrice() {
        return taxPrice;
    }

    public void setTaxPrice(Long taxPrice) {
        this.taxPrice = taxPrice;
    }

    @Override
    public String toString() {
        return "PurchaseQuotation{" +
                "purchaseQuotationId=" + purchaseQuotationId +
                ", skuId=" + skuId +
                ", price=" + price +
                ", customerId=" + customerId +
                ", periodOfValidity=" + periodOfValidity +
                ", total=" + total +
                ", preTax=" + preTax +
                ", freight=" + freight +
                ", afterTax=" + afterTax +
                ", isFreight=" + isFreight +
                ", source=" + source +
                ", sourceId=" + sourceId +
                ", formId=" + formId +
                ", deliveryDate=" + deliveryDate +
                ", InvoiceType = " + InvoiceType +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", createUser=" + createUser +
                ", updateUser=" + updateUser +
                ", display=" + display +
                ", deptId=" + deptId +
                "}";
    }
}
