package cn.atsoft.dasheng.wedrive.space.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 微信微盘空间
 * </p>
 *
 * @author Captain_Jazz
 * @since 2023-03-31
 */
@TableName("wx_wedrive_space")
public class    WxWedriveSpace implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 空间id
     */
      @TableId(value = "space_id", type = IdType.ID_WORKER)
    private String spaceId;

    /**
     * 空间名称
     */
    @TableField("space_name")
    private String spaceName;

    /**
     * 权限
     */
    @TableField("auth_list")
    private String authList;
    /**
     * 权限
     */
    @TableField("type")
    private String type;

    /**
     * 创建者
     */
      @TableField(value = "create_user", fill = FieldFill.INSERT)
    private Long createUser;

    /**
     * 部门编号
     */
    @TableField(value = "deptId" , fill = FieldFill.UPDATE)
    private Long deptId;

    /**
     * 状态
     */
    @TableField("display")
    private Integer display;

    /**
     * 修改时间
     */
      @TableField(value = "update_time", fill = FieldFill.UPDATE)
    private Date updateTime;

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


    public String getSpaceId() {
        return spaceId;
    }

    public void setSpaceId(String spaceId) {
        this.spaceId = spaceId;
    }

    public String getSpaceName() {
        return spaceName;
    }

    public void setSpaceName(String spaceName) {
        this.spaceName = spaceName;
    }

    public String getAuthList() {
        return authList;
    }

    public void setAuthList(String authList) {
        this.authList = authList;
    }

    public Long getCreateUser() {
        return createUser;
    }

    public void setCreateUser(Long createUser) {
        this.createUser = createUser;
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

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "WxWedriveSpace{" +
                "spaceId='" + spaceId + '\'' +
                ", spaceName='" + spaceName + '\'' +
                ", authList='" + authList + '\'' +
                ", type='" + type + '\'' +
                ", createUser=" + createUser +
                ", deptId=" + deptId +
                ", display=" + display +
                ", updateTime=" + updateTime +
                ", createTime=" + createTime +
                ", updateUser=" + updateUser +
                '}';
    }
}
