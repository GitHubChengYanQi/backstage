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
 * 租户用户绑定表
 * </p>
 *
 * @author Captain_Jazz
 * @since 2023-04-19
 */
@TableName("sys_tenant_bind")
public class TenantBind implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 租户绑定id
     */
    @TableId(value = "tenant_bind_id", type = IdType.ID_WORKER)
    private Long tenantBindId;

    /**
     * 租户id
     */
      @TableField(value = "tenant_id")
    private Long tenantId;

    /**
     * 系统用户id
     */
    @TableField("user_id")
    private Long userId;
    /**
     * 是否启用
     */
    @TableField("status")
    private Integer status;

    /**
     * 删除状态
     */
    @TableField("display")
    private Integer display;
    /**
     * 部门
     */
    @TableField(value = "deptId" , fill = FieldFill.INSERT)
    private Long deptId;

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


    public Long getTenantBindId() {
        return tenantBindId;
    }

    public void setTenantBindId(Long tenantBindId) {
        this.tenantBindId = tenantBindId;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "TenantBind{" +
                "tenantBindId=" + tenantBindId +
                ", tenantId=" + tenantId +
                ", userId=" + userId +
                ", status=" + status +
                ", display=" + display +
                ", createUser=" + createUser +
                ", updateUser=" + updateUser +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
