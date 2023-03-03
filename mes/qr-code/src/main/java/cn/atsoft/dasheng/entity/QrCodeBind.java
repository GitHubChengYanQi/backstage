package cn.atsoft.dasheng.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;

/**
 * <p>
 * 二维码绑定
 * </p>
 *
 * @author song
 * @since 2021-10-29
 */
@TableName("qr_code_bind")
public class QrCodeBind implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 类型
     */
    @TableField("type")
    private String type;
    /**
     * 绑定id
     */
    @TableId(value = "qr_code_bind_id", type = IdType.ID_WORKER)
    private Long orCodeBindId;

    /**
     * 二维码id
     */
    @TableField("qr_code_id")
    private Long orCodeId;

    /**
     * 来源
     */
    @TableField("source")
    private String source;

    /**
     * 表单id
     */
    @TableField("form_id")
    private Long formId;

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

    /**
     * 部门id
     */
    @TableField("deptId")
    private Long deptId;


    public Long getOrCodeBindId() {
        return orCodeBindId;
    }

    public void setOrCodeBindId(Long orCodeBindId) {
        this.orCodeBindId = orCodeBindId;
    }

    public Long getOrCodeId() {
        return orCodeId;
    }

    public void setOrCodeId(Long orCodeId) {
        this.orCodeId = orCodeId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Long getFormId() {
        return formId;
    }

    public void setFormId(Long formId) {
        this.formId = formId;
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

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "OrCodeBind{" +
                "orCodeBindId=" + orCodeBindId +
                ", orCodeId=" + orCodeId +
                ", source=" + source +
                ", formId=" + formId +
                ", createUser=" + createUser +
                ", updateUser=" + updateUser +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", display=" + display +
                ", deptId=" + deptId +
                "}";
    }
}
