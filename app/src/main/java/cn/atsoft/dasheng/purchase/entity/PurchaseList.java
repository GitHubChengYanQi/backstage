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
 * 预购管理
 * </p>
 *
 * @author Captain_Jazz
 * @since 2023-03-04
 */
@TableName("daoxin_purchase_list")
public class PurchaseList implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "purchase_list_id", type = IdType.ID_WORKER)
    private Long purchaseListId;

    @TableField("sku_id")
    private Long skuId;

    /**
     * 品牌id
     */
    @TableField("brand_id")
    private Long brandId;

    /**
     * 供应商id
     */
    @TableField("customer_id")
    private Long customerId;

    /**
     * 数量
     */
    @TableField("number")
    private Integer number;

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

    @TableField(value = "deptId", fill = FieldFill.INSERT)
    private Long deptId;

    /**
     * 状态 创建采购计划 98  采购计划完成99
     */
    @TableField("status")
    private Integer status;


    public Long getPurchaseListId() {
        return purchaseListId;
    }

    public void setPurchaseListId(Long purchaseListId) {
        this.purchaseListId = purchaseListId;
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

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "PurchaseList{" +
        "purchaseListId=" + purchaseListId +
        ", skuId=" + skuId +
        ", brandId=" + brandId +
        ", customerId=" + customerId +
        ", number=" + number +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        ", createUser=" + createUser +
        ", updateUser=" + updateUser +
        ", display=" + display +
        ", deptId=" + deptId +
        ", status=" + status +
        "}";
    }
}
