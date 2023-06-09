package cn.atsoft.dasheng.erp.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;

/**
 * <p>
 * 盘点任务详情
 * </p>
 *
 * @author Captain_Jazz
 * @since 2021-12-27
 */
@TableName("daoxin_erp_inventory_detail")
public class InventoryDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("type")
    private String type;

    @TableField("part_condition")
    private String partCondition;

    @TableField("material_condition")
    private String materialCondition;

    @TableField("class_condition")
    private String classCondition;

    @TableField("brand_condition")
    private String brandCondition;

    @TableField("position_condition")
    private String positionCondition;

    @TableField("spu_id")
    private Long spuId;

    @TableField("real_number")
    private Long realNumber;

    @TableField("lock_status")
    private Integer lockStatus;

    @TableField("enclosure")
    private String enclosure;

    @TableField("anomaly_id")
    private Long anomalyId;

    @TableField("customer_id")
    private Long customerId;

    @TableField("brand_id")
    private Long brandId;

    @TableField("number")
    private Long number;


    @TableField("position_id")
    private Long positionId;

    /**
     * 盘点任务详情id
     */
    @TableId(value = "detail_id", type = IdType.ID_WORKER)
    private Long detailId;


    /**
     * 物料
     */
    @TableField("sku_id")
    private Long skuId;
    /**
     * 状态
     */
    @TableField("status")
    private Integer status;


    /**
     * 对应实物id
     */
    @TableField("inventory_id")
    private Long inventoryId;

    /**
     * 删除状态
     */
    @TableField("display")
    private Integer display;


    /**
     * 对应实物id
     */
    @TableField("inkind_id")
    private Long inkindId;

    @TableField(value = "create_user", fill = FieldFill.INSERT)
    private Long createUser;

    @TableField(value = "update_user", fill = FieldFill.UPDATE)
    private Long updateUser;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(value = "update_time", fill = FieldFill.UPDATE)
    private Date updateTime;

    /**
     * 部门id
     */
@TableField(value = "deptId" , fill = FieldFill.INSERT)
    private Long deptId;
    /**
     * 租户编号
     */
    @TableField(value = "tenant_id" , fill = FieldFill.INSERT)
    private Long tenantId;

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public Integer getDisplay() {
        return display;
    }

    public void setDisplay(Integer display) {
        this.display = display;
    }

    public Long getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(Long inventoryId) {
        this.inventoryId = inventoryId;
    }

    public Long getDetailId() {
        return detailId;
    }

    public void setDetailId(Long detailId) {
        this.detailId = detailId;
    }

    public Long getInkindId() {
        return inkindId;
    }

    public void setInkindId(Long inkindId) {
        this.inkindId = inkindId;
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

    public Long getPositionId() {
        return positionId;
    }

    public void setPositionId(Long positionId) {
        this.positionId = positionId;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public Long getAnomalyId() {
        return anomalyId;
    }

    public void setAnomalyId(Long anomalyId) {
        this.anomalyId = anomalyId;
    }

    public String getEnclosure() {
        return enclosure;
    }

    public void setEnclosure(String enclosure) {
        this.enclosure = enclosure;
    }

    public Integer getLockStatus() {
        return lockStatus;
    }

    public void setLockStatus(Integer lockStatus) {
        this.lockStatus = lockStatus;
    }

    public Long getRealNumber() {
        return realNumber;
    }

    public void setRealNumber(Long realNumber) {
        this.realNumber = realNumber;
    }

    public String getPartCondition() {
        return partCondition;
    }

    public void setPartCondition(String partCondition) {
        this.partCondition = partCondition;
    }

    public String getMaterialCondition() {
        return materialCondition;
    }

    public void setMaterialCondition(String materialCondition) {
        this.materialCondition = materialCondition;
    }

    public String getClassCondition() {
        return classCondition;
    }

    public void setClassCondition(String classCondition) {
        this.classCondition = classCondition;
    }

    public String getBrandCondition() {
        return brandCondition;
    }

    public void setBrandCondition(String brandCondition) {
        this.brandCondition = brandCondition;
    }

    public String getPositionCondition() {
        return positionCondition;
    }

    public void setPositionCondition(String positionCondition) {
        this.positionCondition = positionCondition;
    }

    public Long getSpuId() {
        return spuId;
    }

    public void setSpuId(Long spuId) {
        this.spuId = spuId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "InventoryDetail{" +
                "detailId=" + detailId +
                ", inkindId=" + inkindId +
                ", createUser=" + createUser +
                ", updateUser=" + updateUser +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", deptId=" + deptId +
                "}";
    }
}
