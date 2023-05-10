package cn.atsoft.dasheng.sys.modular.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 部门造作记录表
 * </p>
 *
 * @author Captain_Jazz
 * @since 2023-05-09
 */
@TableName("sys_tenant_operation_log")
public class TenantOperationLog implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "tenant_operation_log_id", type = IdType.ID_WORKER)
    private Long tenantOperationLogId;

    @TableField("user_id")
    private Long userId;

    @TableField("remark")
    private String remark;

    @TableField("type")
    private Integer type;

      @TableField(value = "tenant_id", fill = FieldFill.INSERT)
    private Long tenantId;

    @TableField("tenant_bind_id")
    private Long tenantBindId;

    /**
     * 删除状态
     */
    @TableField("display")
    private Integer display;

    /**
     * 创建人
     */
      @TableField(value = "create_user", fill = FieldFill.INSERT)
    private Long createUser;

    /**
     * 更新人
     */
      @TableField(value = "update_user", fill = FieldFill.UPDATE)
    private Long updateUser;

    /**
     * 创建时间
     */
      @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 更新时间
     */
      @TableField(value = "update_time", fill = FieldFill.UPDATE)
    private Date updateTime;


    public Long getTenantOperationLogId() {
        return tenantOperationLogId;
    }

    public void setTenantOperationLogId(Long tenantOperationLogId) {
        this.tenantOperationLogId = tenantOperationLogId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Long getTenantBindId() {
        return tenantBindId;
    }

    public void setTenantBindId(Long tenantBindId) {
        this.tenantBindId = tenantBindId;
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

    @Override
    public String toString() {
        return "TenantOperationLog{" +
        "tenantOperationLogId=" + tenantOperationLogId +
        ", userId=" + userId +
        ", remark=" + remark +
        ", type=" + type +
        ", tenantId=" + tenantId +
        ", tenantBindId=" + tenantBindId +
        ", display=" + display +
        ", createUser=" + createUser +
        ", updateUser=" + updateUser +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        "}";
    }
}
