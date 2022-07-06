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
 * 养护申请子表
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-06-28
 */
@TableName("daoxin_erp_maintenance_detail")
public class MaintenanceDetail implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "maintenance_detail_id", type = IdType.ID_WORKER)
    private Long maintenanceDetailId;

    @TableField("maintenance_id")
    private Long maintenanceId;

    @TableField("done_number")
    private Integer doneNumber;

    @TableField("storehouse_positions_id")
    private Long storehousePositionsId;

    @TableField("inkind_id")
    private Long inkindId;

    @TableField("sku_id")
    private Long skuId;

    @TableField("number")
    private Integer number;

    /**
     * 品牌id
     */
    @TableField("brand_id")
    private Long brandId;

    /**
     * 创建者
     */
      @TableField(value = "create_user", fill = FieldFill.INSERT)
    private Long createUser;

    /**
     * 创建时间
     */
      @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 修改者
     */
      @TableField(value = "update_user", fill = FieldFill.UPDATE)
    private Long updateUser;

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
     * 状态
     */
    @TableField("status")
    private Integer status;


    public Long getMaintenanceDetailId() {
        return maintenanceDetailId;
    }

    public void setMaintenanceDetailId(Long maintenanceDetailId) {
        this.maintenanceDetailId = maintenanceDetailId;
    }

    public Long getMaintenanceId() {
        return maintenanceId;
    }

    public void setMaintenanceId(Long maintenanceId) {
        this.maintenanceId = maintenanceId;
    }

    public Long getInkindId() {
        return inkindId;
    }

    public void setInkindId(Long inkindId) {
        this.inkindId = inkindId;
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(Long updateUser) {
        this.updateUser = updateUser;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getDoneNumber() {
        return doneNumber;
    }

    public void setDoneNumber(Integer doneNumber) {
        this.doneNumber = doneNumber;
    }

    public Long getStorehousePositionsId() {
        return storehousePositionsId;
    }

    public void setStorehousePositionsId(Long storehousePositionsId) {
        this.storehousePositionsId = storehousePositionsId;
    }

    @Override
    public String toString() {
        return "MaintenanceDetail{" +
        "maintenanceDetailId=" + maintenanceDetailId +
        ", maintenanceId=" + maintenanceId +
        ", inkindId=" + inkindId +
        ", skuId=" + skuId +
        ", number=" + number +
        ", brandId=" + brandId +
        ", createUser=" + createUser +
        ", createTime=" + createTime +
        ", updateUser=" + updateUser +
        ", updateTime=" + updateTime +
        ", display=" + display +
        ", deptId=" + deptId +
        ", status=" + status +
        "}";
    }
}
