package cn.atsoft.dasheng.audit.entity;

import cn.atsoft.dasheng.form.pojo.AuditRule;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 审批配置表
 * </p>
 *
 * @author Sing
 * @since 2021-11-10
 */
@TableName(autoResultMap = true)
@Accessors(chain = true)
public class ActivitiAudit implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "audit_id", type = IdType.ID_WORKER)
    private Long auditId;

    /**
     * 动作
     */
    @TableField("action")
    private String action;

    /**
     * 步骤Id
     */
    @TableField("setps_id")
    private Long setpsId;

    /**
     * person（指定人），supervisor（主管），optional（自主选择）
     */
    @TableField("type")
    private String type;

//    /**
//     * 审批规则
//     */
//    @TableField("rule")
//    private String rule;

    @TableField(typeHandler = FastjsonTypeHandler.class)
    private AuditRule rule;

    /**
     * 单据类型
     */
    @TableField("form_type")
    private String formType;

    /**
     * 单据动作
     */
    @TableField("documents_status_id")
    private Long documentsStatusId;

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

    public Long getDocumentsStatusId() {
        return documentsStatusId;
    }

    public void setDocumentsStatusId(Long documentsStatusId) {
        this.documentsStatusId = documentsStatusId;
    }

    public Long getAuditId() {
        return auditId;
    }

    public void setAuditId(Long auditId) {
        this.auditId = auditId;
    }

    public Long getSetpsId() {
        return setpsId;
    }

    public void setSetpsId(Long setpsId) {
        this.setpsId = setpsId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

//    public String getRule() {
//        return rule;
//    }
//
//    public void setRule(String rule) {
//        this.rule = rule;
//    }


    public String getFormType() {
        return formType;
    }

    public void setFormType(String formType) {
        this.formType = formType;
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

    public AuditRule getRule() {
        return rule;
    }

    public void setRule(AuditRule rule) {
        this.rule = rule;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    @Override
    public String toString() {
        return "ActivitiAudit{" +
                "auditId=" + auditId +
                ", setpsId=" + setpsId +
                ", type=" + type +
//                ", rule=" + rule +
                ", display=" + display +
                ", createUser=" + createUser +
                ", updateUser=" + updateUser +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                "}";
    }
}
