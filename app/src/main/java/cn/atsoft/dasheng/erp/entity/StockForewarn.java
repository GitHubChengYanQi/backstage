package cn.atsoft.dasheng.erp.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;
import java.io.Serializable;

/**
 * <p>
 * 库存预警设置
 * </p>
 *
 * @author sjl
 * @since 2022-12-05
 */
@TableName("daoxin_erp_stock_forewarn")
public class StockForewarn implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 预警序号
     */
    @TableId(value = "forewarn_id", type = IdType.ID_WORKER)
    private Long forewarnId;

    /**
     * 类型
     */
    @TableField("type")
    private String type;

    /**
     * 预警内容
     */
    @TableField("form_id")
    private Long formId;

    /**
     * 库存下限
     */
    @TableField(value = "inventory_floor", updateStrategy = FieldStrategy.IGNORED)
    private Integer inventoryFloor;

    /**
     * 库存上限
     */
    @TableField(value = "inventory_ceiling", updateStrategy = FieldStrategy.IGNORED)
    private Integer inventoryCeiling;

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


    public Long getForewarnId() {
        return forewarnId;
    }

    public void setForewarnId(Long forewarnId) {
        this.forewarnId = forewarnId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getFormId() {
        return formId;
    }

    public void setFormId(Long formId) {
        this.formId = formId;
    }

    public Integer getInventoryFloor() {
        return inventoryFloor;
    }

    public void setInventoryFloor(Integer inventoryFloor) {
        this.inventoryFloor = inventoryFloor;
    }

    public Integer getInventoryCeiling() {
        return inventoryCeiling;
    }

    public void setInventoryCeiling(Integer inventoryCeiling) {
        this.inventoryCeiling = inventoryCeiling;
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
    @Override
    public String toString() {
        return "StockForewarn{" +
                "forewarnId=" + forewarnId +
                ", type='" + type + '\'' +
                ", formId=" + formId +
                ", inventoryFloor=" + inventoryFloor +
                ", inventoryCeiling=" + inventoryCeiling +
                ", display=" + display +
                ", createUser=" + createUser +
                ", updateUser=" + updateUser +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
