package cn.atsoft.dasheng.crm.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;

/**
 * <p>
 * 供应商供应物料
 * </p>
 *
 * @author song
 * @since 2021-12-20
 */
@TableName("daoxin_supply")
public class Supply implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("supplier_model")
    private String supplierModel;

    @TableId(value = "supply_id", type = IdType.ID_WORKER)
    private Long supplyId;
    /**
     * 品牌
     */
    @TableField("brand_id")
    private Long brandId;
    /**
     * sku
     */
    @TableField("sku_id")
    private Long skuId;

    /**
     * 物料品牌绑定主表
     */
    @TableField("sku_brand_bind")
    private Long skuBrandBind;


    /**
     * 供应商id
     */
    @TableField("customer_id")
    private Long customerId;

    /**
     * 删除状态
     */
    @TableField("display")
    private Integer display;

    @TableField(value = "create_user", fill = FieldFill.INSERT)
    private Long createUser;

    @TableField(value = "update_user", fill = FieldFill.UPDATE)
    private Long updateUser;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(value = "update_time", fill = FieldFill.UPDATE)
    private Date updateTime;

    public String getSupplierModel() {
        return supplierModel;
    }

    public void setSupplierModel(String supplierModel) {
        this.supplierModel = supplierModel;
    }
    @TableField(value = "deptId", fill = FieldFill.INSERT)
    private Long deptId;

    @TableField(value = "tenant_id" , fill = FieldFill.INSERT)
    private Long tenantId;

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }
    public Long getSupplyId() {
        return supplyId;
    }

    public void setSupplyId(Long supplyId) {
        this.supplyId = supplyId;
    }

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

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public Integer getDisplay() {
        return display;
    }

    public void setDisplay(Integer display) {
        this.display = display;
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

    public Long getSkuBrandBind() {
        return skuBrandBind;
    }

    public void setSkuBrandBind(Long skuBrandBind) {
        this.skuBrandBind = skuBrandBind;
    }

    @Override
    public String toString() {
        return "Supply{" +
                "supplierModel='" + supplierModel + '\'' +
                ", supplyId=" + supplyId +
                ", brandId=" + brandId +
                ", skuId=" + skuId +
                ", skuBrandBind=" + skuBrandBind +
                ", customerId=" + customerId +
                ", display=" + display +
                ", createUser=" + createUser +
                ", updateUser=" + updateUser +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
