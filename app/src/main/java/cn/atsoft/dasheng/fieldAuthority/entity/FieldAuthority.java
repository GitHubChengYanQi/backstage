package cn.atsoft.dasheng.fieldAuthority.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 字段操作权限表
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-05-17
 */
@TableName("sys_field_authority")
public class FieldAuthority implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "authority_id", type = IdType.ID_WORKER)
    private Long authorityId;

    /**
     * 出入参实体类名称
     */
    @TableField("model_names")
    private String modelNames;

    /**
     * 自定义字段权限
     */
    @TableField("detail")
    private String detail;

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


    public Long getAuthorityId() {
        return authorityId;
    }

    public void setAuthorityId(Long authorityId) {
        this.authorityId = authorityId;
    }

    public String getModelNames() {
        return modelNames;
    }

    public void setModelNames(String modelNames) {
        this.modelNames = modelNames;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
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
        return "FieldAuthority{" +
        "authorityId=" + authorityId +
        ", modelNames=" + modelNames +
        ", detail=" + detail +
        ", display=" + display +
        ", createUser=" + createUser +
        ", updateUser=" + updateUser +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        "}";
    }
}
