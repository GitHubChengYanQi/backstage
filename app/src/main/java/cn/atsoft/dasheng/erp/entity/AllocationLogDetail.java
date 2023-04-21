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
 * 
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-08-11
 */
@TableName("daoxin_erp_allocation_log_detail")
public class AllocationLogDetail implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "allocation_log_detail_id", type = IdType.ID_WORKER)
    private Long allocationLogDetailId;

    /**
     * 调拨logid
     */
    @TableField("allocation_log_id")
    private Long allocationLogId;

    /**
     * 调拨id
     */
    @TableField("allocation_id")
    private Long allocationId;

    @TableField("allocation_detail_id")
    private Long allocationDetailId;

    @TableField("sku_id")
    private Long skuId;

    @TableField("number")
    private Integer number;

    /**
     * 仓库库位id
     */
    @TableField("storehouse_positions_id")
    private Long storehousePositionsId;

    /**
     * 仓库id
     */
    @TableField("storehouse_id")
    private Long storehouseId;

    @TableField("brand_id")
    private Long brandId;

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

    /**
     * 调拨目标位置仓库库位id
     */
    @TableField("to_storehouse_positions_id")
    private Long toStorehousePositionsId;

    /**
     * 调拨目标位置仓库id
     */
    @TableField("to_storehouse_id")
    private Long toStorehouseId;

    @TableField("inkind_id")
    private Long inkindId;


    public Long getAllocationLogDetailId() {
        return allocationLogDetailId;
    }

    public void setAllocationLogDetailId(Long allocationLogDetailId) {
        this.allocationLogDetailId = allocationLogDetailId;
    }

    public Long getAllocationLogId() {
        return allocationLogId;
    }

    public void setAllocationLogId(Long allocationLogId) {
        this.allocationLogId = allocationLogId;
    }

    public Long getAllocationId() {
        return allocationId;
    }

    public void setAllocationId(Long allocationId) {
        this.allocationId = allocationId;
    }

    public Long getAllocationDetailId() {
        return allocationDetailId;
    }

    public void setAllocationDetailId(Long allocationDetailId) {
        this.allocationDetailId = allocationDetailId;
    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Long getStorehousePositionsId() {
        return storehousePositionsId;
    }

    public void setStorehousePositionsId(Long storehousePositionsId) {
        this.storehousePositionsId = storehousePositionsId;
    }

    public Long getStorehouseId() {
        return storehouseId;
    }

    public void setStorehouseId(Long storehouseId) {
        this.storehouseId = storehouseId;
    }

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
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

    public Long getToStorehousePositionsId() {
        return toStorehousePositionsId;
    }

    public void setToStorehousePositionsId(Long toStorehousePositionsId) {
        this.toStorehousePositionsId = toStorehousePositionsId;
    }

    public Long getToStorehouseId() {
        return toStorehouseId;
    }

    public void setToStorehouseId(Long toStorehouseId) {
        this.toStorehouseId = toStorehouseId;
    }

    public Long getInkindId() {
        return inkindId;
    }

    public void setInkindId(Long inkindId) {
        this.inkindId = inkindId;
    }

    @Override
    public String toString() {
        return "AllocationLogDetail{" +
        "allocationLogDetailId=" + allocationLogDetailId +
        ", allocationLogId=" + allocationLogId +
        ", allocationId=" + allocationId +
        ", allocationDetailId=" + allocationDetailId +
        ", skuId=" + skuId +
        ", number=" + number +
        ", storehousePositionsId=" + storehousePositionsId +
        ", storehouseId=" + storehouseId +
        ", brandId=" + brandId +
        ", createUser=" + createUser +
        ", updateUser=" + updateUser +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        ", display=" + display +
        ", deptId=" + deptId +
        ", toStorehousePositionsId=" + toStorehousePositionsId +
        ", toStorehouseId=" + toStorehouseId +
        ", inkindId=" + inkindId +
        "}";
    }
}
