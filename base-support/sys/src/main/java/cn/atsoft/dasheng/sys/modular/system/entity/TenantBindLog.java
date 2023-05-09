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
 * 邀请记录  申请记录
 * </p>
 *
 * @author Captain_Jazz
 * @since 2023-05-09
 */
@TableName("sys_tenant_bind_log")
public class TenantBindLog implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "tenant_bind_log_id", type = IdType.ID_WORKER)
    private Long tenantBindLogId;

    @TableField("user_id")
    private Long userId;

    @TableField("audit_user")
    private Long auditUser;

    @TableField("tenant_id")
    private Long tenantId;

    @TableField("status")
    private Integer status;

    /**
     * 邀请记录/申请记录
     */
    @TableField("type")
    private String type;

    @TableField("delete_user")
    private Long deleteUser;

    @TableField("delete_time")
    private Date deleteTime;

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

    /**
     * 邀请人
     */
    @TableField("inviter_user")
    private Long inviterUser;

    /**
     * 部门id
     */
      @TableField(value = "deptId", fill = FieldFill.INSERT)
    private Long deptId;


    public Long getTenantBindLogId() {
        return tenantBindLogId;
    }

    public void setTenantBindLogId(Long tenantBindLogId) {
        this.tenantBindLogId = tenantBindLogId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getAuditUser() {
        return auditUser;
    }

    public void setAuditUser(Long auditUser) {
        this.auditUser = auditUser;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getDeleteUser() {
        return deleteUser;
    }

    public void setDeleteUser(Long deleteUser) {
        this.deleteUser = deleteUser;
    }

    public Date getDeleteTime() {
        return deleteTime;
    }

    public void setDeleteTime(Date deleteTime) {
        this.deleteTime = deleteTime;
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

    public Long getInviterUser() {
        return inviterUser;
    }

    public void setInviterUser(Long inviterUser) {
        this.inviterUser = inviterUser;
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
        return "TenantBindLog{" +
                "tenantBindLogId=" + tenantBindLogId +
                ", userId=" + userId +
                ", auditUser=" + auditUser +
                ", tenantId=" + tenantId +
                ", status=" + status +
                ", type='" + type + '\'' +
                ", deleteUser=" + deleteUser +
                ", deleteTime=" + deleteTime +
                ", display=" + display +
                ", createUser=" + createUser +
                ", updateUser=" + updateUser +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", inviterUser=" + inviterUser +
                ", deptId=" + deptId +
                '}';
    }
}
