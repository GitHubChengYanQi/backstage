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
 * @since 2023-05-12
 */
@TableName("sys_tenant_invite_log")
public class TenantInviteLog implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "tenant_invite_log_id", type = IdType.ID_WORKER)
    private Long tenantInviteLogId;

      @TableField(value = "tenant_id")
    private Long tenantId;

    /**
     * 部门id
     */
      @TableField(value = "deptId")
    private Long deptId;

    /**
     * 删除状态
     */
    @TableField("display")
    private Integer display;

    /**
     * 邀请人
     */
    @TableField("inviter_user")
    private Long inviterUser;

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


    public Long getTenantInviteLogId() {
        return tenantInviteLogId;
    }

    public void setTenantInviteLogId(Long tenantInviteLogId) {
        this.tenantInviteLogId = tenantInviteLogId;
    }

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

    public Integer getDisplay() {
        return display;
    }

    public void setDisplay(Integer display) {
        this.display = display;
    }

    public Long getInviterUser() {
        return inviterUser;
    }

    public void setInviterUser(Long inviterUser) {
        this.inviterUser = inviterUser;
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
        return "TenantInviteLog{" +
        "tenantInviteLogId=" + tenantInviteLogId +
        ", tenantId=" + tenantId +
        ", deptId=" + deptId +
        ", display=" + display +
        ", inviterUser=" + inviterUser +
        ", createUser=" + createUser +
        ", updateUser=" + updateUser +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        "}";
    }
}
