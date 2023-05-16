package cn.atsoft.dasheng.storehouse.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 仓库物料分类绑定表
 * </p>
 *
 * @author Captain_Jazz
 * @since 2023-05-15
 */
@TableName("daoxin_erp_storehouse_spu_class_bind")
public class StorehouseSpuClassBind implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "storehouse_spu_class_bind_id", type = IdType.ID_WORKER)
    private Long storehouseSpuClassBindId;

    @TableField("spu_class_id")
    private Long spuClassId;

    @TableField("storehouse_id")
    private Long storehouseId;

    /**
     * 创建时间
     */
      @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

      @TableField(value = "tenant_id", fill = FieldFill.INSERT)
    private Long tenantId;

    /**
     * 创建者
     */
      @TableField(value = "create_user", fill = FieldFill.INSERT)
    private Long createUser;

    /**
     * 修改时间
     */
      @TableField(value = "update_time", fill = FieldFill.UPDATE)
    private Date updateTime;

    /**
     * 修改者
     */
      @TableField(value = "update_user", fill = FieldFill.UPDATE)
    private Long updateUser;

    /**
     * 状态
     */
    @TableField("display")
    private Integer display;

    /**
     * 部门编号
     */
      @TableField(value = "deptId", fill = FieldFill.INSERT)
    private Long deptId;


    public Long getStorehouseSpuClassBindId() {
        return storehouseSpuClassBindId;
    }

    public void setStorehouseSpuClassBindId(Long storehouseSpuClassBindId) {
        this.storehouseSpuClassBindId = storehouseSpuClassBindId;
    }

    public Long getSpuClassId() {
        return spuClassId;
    }

    public void setSpuClassId(Long spuClassId) {
        this.spuClassId = spuClassId;
    }

    public Long getStorehouseId() {
        return storehouseId;
    }

    public void setStorehouseId(Long storehouseId) {
        this.storehouseId = storehouseId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Long getCreateUser() {
        return createUser;
    }

    public void setCreateUser(Long createUser) {
        this.createUser = createUser;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Long getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(Long updateUser) {
        this.updateUser = updateUser;
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
        return "StorehouseSpuClassBind{" +
        "storehouseSpuClassBindId=" + storehouseSpuClassBindId +
        ", spuClassId=" + spuClassId +
        ", storehouseId=" + storehouseId +
        ", createTime=" + createTime +
        ", tenantId=" + tenantId +
        ", createUser=" + createUser +
        ", updateTime=" + updateTime +
        ", updateUser=" + updateUser +
        ", display=" + display +
        ", deptId=" + deptId +
        "}";
    }
}
