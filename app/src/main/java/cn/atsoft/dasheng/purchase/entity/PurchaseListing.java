package cn.atsoft.dasheng.purchase.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;

import java.sql.Time;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;

/**
 * <p>
 * 采购清单
 * </p>
 *
 * @author song
 * @since 2021-12-15
 */
@TableName("daoxin_purchase_listing")
public class PurchaseListing implements Serializable {

    private static final long serialVersionUID = 1L;



    /**
     * 交货时间
     */
    @TableField("delivery_time")
    private Time deliveryTime;
    /**
     * 品牌
     */
    @TableField("brand_id")
    private Long brandId;

    /**
     * 采购清单id
     */
    @TableId(value = "purchase_listing_id", type = IdType.ID_WORKER)
    private Long purchaseListingId;

    /**
     * 状态
     */
    @TableField("status")
    private Integer status;
    /**
     * 采购申请id
     */
    @TableField("purchase_ask_id")
    private Long purchaseAskId;

    @TableField("sku_id")
    private Long skuId;

    /**
     * 申请数量
     */
    @TableField("apply_number")
    private Long applyNumber;

    /**
     * 可用数量
     */
    @TableField("available_number")
    private Long availableNumber;

    /**
     * 交付日期
     */
    @TableField("delivery_date")
    private Date deliveryDate;

    /**
     * 备注
     */
    @TableField("note")
    private String note;

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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getPurchaseListingId() {
        return purchaseListingId;
    }

    public void setPurchaseListingId(Long purchaseListingId) {
        this.purchaseListingId = purchaseListingId;
    }

    public Long getPurchaseAskId() {
        return purchaseAskId;
    }

    public void setPurchaseAskId(Long purchaseAskId) {
        this.purchaseAskId = purchaseAskId;
    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public Long getApplyNumber() {
        return applyNumber;
    }

    public void setApplyNumber(Long applyNumber) {
        this.applyNumber = applyNumber;
    }

    public Long getAvailableNumber() {
        return availableNumber;
    }

    public void setAvailableNumber(Long availableNumber) {
        this.availableNumber = availableNumber;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
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

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public Time getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(Time deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    @Override
    public String toString() {
        return "PurchaseListing{" +
                "purchaseListingId=" + purchaseListingId +
                ", purchaseAskId=" + purchaseAskId +
                ", skuId=" + skuId +
                ", applyNumber=" + applyNumber +
                ", availableNumber=" + availableNumber +
                ", deliveryDate=" + deliveryDate +
                ", note=" + note +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", createUser=" + createUser +
                ", updateUser=" + updateUser +
                ", display=" + display +
                ", deptId=" + deptId +
                "}";
    }
}
