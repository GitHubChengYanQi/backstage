package cn.atsoft.dasheng.form.entity;

import cn.atsoft.dasheng.form.pojo.StepsType;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;

/**
 * <p>
 * 流程步骤表
 * </p>
 *
 * @author Sing
 * @since 2021-11-10
 */
@TableName("activiti_steps")
public class ActivitiSteps implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 步骤类型
     */
    @TableField("step_type")
    private String stepType;
    /**
     * 步骤Id，主键
     */
    @TableId(value = "setps_id", type = IdType.ID_WORKER)
    private Long setpsId;
    /**
     * 分支
     */
    @TableField("conditionNodes")
    private String conditionNodes;


    /**
     * 流程ID
     */
    @TableField("process_id")
    private Long processId;

    /**
     * 步骤类型：ship（工艺），setp（工序），audit（审核），audit_process（审核流程）
     */
    @TableField("type")
    @JSONField(serialzeFeatures= SerializerFeature.WriteEnumUsingToString)
    private StepsType type;

    /**
     * 分表Id
     */
    @TableField("form_id")
    private Long formId;

    /**
     * 上一步
     */
    @TableField("supper")
    private Long supper;

    /**
     * 下级步骤
     */
    @TableField("children")
    private String children;

    /**
     * 所有后续步骤
     */
    @TableField("childrens")
    private String childrens;

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


    public Long getSetpsId() {
        return setpsId;
    }

    public void setSetpsId(Long setpsId) {
        this.setpsId = setpsId;
    }

    public Long getProcessId() {
        return processId;
    }

    public void setProcessId(Long processId) {
        this.processId = processId;
    }

    public StepsType getType() {
        return type;
    }

    public void setType(StepsType type) {
        this.type = type;
    }

    public Long getFormId() {
        return formId;
    }

    public void setFormId(Long formId) {
        this.formId = formId;
    }

    public Long getSupper() {
        return supper;
    }

    public void setSupper(Long supper) {
        this.supper = supper;
    }

    public String getChildren() {
        return children;
    }

    public void setChildren(String children) {
        this.children = children;
    }

    public String getChildrens() {
        return childrens;
    }

    public void setChildrens(String childrens) {
        this.childrens = childrens;
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

    public String getConditionNodes() {
        return conditionNodes;
    }

    public void setConditionNodes(String conditionNodes) {
        this.conditionNodes = conditionNodes;
    }

    public String getStepType() {
        return stepType;
    }

    public void setStepType(String stepType) {
        this.stepType = stepType;
    }

    @Override
    public String toString() {
        return "ActivitiSteps{" +
                "setpsId=" + setpsId +
                ", processId=" + processId +
                ", type=" + type +
                ", formId=" + formId +
                ", supper=" + supper +
                ", children=" + children +
                ", childrens=" + childrens +
                ", display=" + display +
                ", createUser=" + createUser +
                ", updateUser=" + updateUser +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                "}";
    }
}
