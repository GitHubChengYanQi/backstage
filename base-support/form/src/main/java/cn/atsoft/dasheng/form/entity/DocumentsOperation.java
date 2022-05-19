package cn.atsoft.dasheng.form.entity;

import cn.atsoft.dasheng.form.pojo.CanDo;
import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;
import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 单据权限操作
 * </p>
 *
 * @author
 * @since 2022-05-18
 */
@TableName("daoxin_documents_operation")
public class DocumentsOperation implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 详情
     */
    @TableId(value = "details_id", type = IdType.ID_WORKER)
    private Long detailsId;

    /**
     * 通用单据权限
     */
    @TableField("permissions_id")
    private Long permissionsId;

    /**
     * 角色
     */
    @TableField("role_id")
    private Long roleId;

    /**
     * 可操作
     */
    @TableField("operational")
    private String operational;

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


    public Long getDetailsId() {
        return detailsId;
    }

    public void setDetailsId(Long detailsId) {
        this.detailsId = detailsId;
    }

    public Long getPermissionsId() {
        return permissionsId;
    }

    public void setPermissionsId(Long permissionsId) {
        this.permissionsId = permissionsId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getOperational() {
        return operational;
    }

    public void setOperational(String operational) {
        this.operational = operational;
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

    @Override
    public String toString() {
        return "DocumentsOperation{" +
                "detailsId=" + detailsId +
                ", permissionsId=" + permissionsId +
                ", roleId=" + roleId +
                ", operational=" + operational +
                ", createUser=" + createUser +
                ", updateUser=" + updateUser +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", display=" + display +
                "}";
    }
}
