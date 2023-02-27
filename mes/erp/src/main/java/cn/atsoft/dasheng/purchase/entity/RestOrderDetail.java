package cn.atsoft.dasheng.purchase.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 订单明细表
 * </p>
 *
 * @author song
 * @since 2022-02-23
 */
@TableName("daoxin_crm_order_detail")
public class RestOrderDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("remark")
    private String remark;

    /**
     * 详情id
     */
    @TableId(value = "detail_id", type = IdType.ID_WORKER)
    private Long detailId;

    /**
     * 订单id
     */
    @TableField("order_id")
    private Long orderId;

    @TableField("sku_id")
    private Long skuId;

    @TableField("brand_id")
    private Long brandId;

    @TableField("customer_id")
    private Long customerId;

    /**
     * 预购数量
     */
    @TableField("preorde_number")
    private Long preordeNumber;

    /**
     * 采购数量
     */
    @TableField("purchase_number")
    private Long purchaseNumber;

    /**
     * 单位id
     */
    @TableField("unit_id")
    private Long unitId;

    /**
     * 单价
     */
    @TableField("one_price")
    private Integer onePrice;

    /**
     * 总价
     */
    @TableField("total_price")
    private Integer totalPrice;

    /**
     * 票据类型
     */
    @TableField("paper_type")
    private Integer paperType;

    /**
     * 锐率
     */
    @TableField("rate")
    private Long rate;

    /**
     * 交货日期
     */
    @TableField("delivery_date")
    private Integer deliveryDate;

    /**
     * 创建者
     */
    @TableField(value = "create_user", fill = FieldFill.INSERT)
    private Long createUser;

    /**
     * 修改者
     */
    @TableField(value = "update_user", fill = FieldFill.UPDATE)
    private Long updateUser;

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
     * 状态
     */
    @TableField("display")
    private Integer display;

    /**
     * 部门id
     */
    @TableField("deptId")
    private Long deptId;

    /**
     * 到货数量
     */
    @TableField("arrival_number")
    private Integer arrivalNumber;

    public Integer getArrivalNumber() {
        return arrivalNumber;
    }

    public void setArrivalNumber(Integer arrivalNumber) {
        this.arrivalNumber = arrivalNumber;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getDetailId() {
        return detailId;
    }

    public void setDetailId(Long detailId) {
        this.detailId = detailId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getPreordeNumber() {
        return preordeNumber;
    }

    public void setPreordeNumber(Long preordeNumber) {
        this.preordeNumber = preordeNumber;
    }

    public Long getPurchaseNumber() {
        return purchaseNumber;
    }

    public void setPurchaseNumber(Long purchaseNumber) {
        this.purchaseNumber = purchaseNumber;
    }

    public Long getUnitId() {
        return unitId;
    }

    public void setUnitId(Long unitId) {
        this.unitId = unitId;
    }

    public Integer getOnePrice() {
        return onePrice;
    }

    public void setOnePrice(Integer onePrice) {
        this.onePrice = onePrice;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Integer totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Integer getPaperType() {
        return paperType;
    }

    public void setPaperType(Integer paperType) {
        this.paperType = paperType;
    }

    public Long getRate() {
        return rate;
    }

    public void setRate(Long rate) {
        this.rate = rate;
    }

    public Integer getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Integer deliveryDate) {
        this.deliveryDate = deliveryDate;
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

    public Integer getDisplay() {
        return display;
    }

    public void setDisplay(Integer display) {
        this.display = display;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    @Override
    public String toString() {
        return "OrderDetail{" +
                "detailId=" + detailId +
                ", orderId=" + orderId +
                ", skuId=" + skuId +
                ", brandId=" + brandId +
                ", customerId=" + customerId +
                ", preordeNumber=" + preordeNumber +
                ", purchaseNumber=" + purchaseNumber +
                ", unitId=" + unitId +
                ", onePrice=" + onePrice +
                ", totalPrice=" + totalPrice +
                ", paperType=" + paperType +
                ", rate=" + rate +
                ", deliveryDate=" + deliveryDate +
                ", createUser=" + createUser +
                ", updateUser=" + updateUser +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", display=" + display +
                ", deptId=" + deptId +
                "}";
    }
}
