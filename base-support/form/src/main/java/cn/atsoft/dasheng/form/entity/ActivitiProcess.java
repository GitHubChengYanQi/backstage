package cn.atsoft.dasheng.form.entity;

import cn.atsoft.dasheng.form.pojo.ProcessEnum;
import cn.atsoft.dasheng.form.pojo.ProcessModuleEnum;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;

import java.io.Serializable;

/**
 * <p>
 * 流程主表
 * </p>
 *
 * @author Sing
 * @since 2021-11-10
 */
@TableName("activiti_process")
public class ActivitiProcess implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 流程ID，主键
     */
    @TableId(value = "process_id", type = IdType.ID_WORKER)
    private Long processId;

    /**
     * 模块
     */
    @TableField("module")

    private String module;
    /**
     * 启用状态
     */
    @TableField("status")
    private Integer status;


    /**
     * 名称
     */
    @TableField("process_name")
    private String processName;

    /**
     * 分类Id
     */
    @TableField("category_id")
    private Long categoryId;

    /**
     * 类型：ship（工艺），audit（审核）
     */
    @TableField("type")

    private String type;

    /**
     * 工艺表Id或表单Id
     */
    @TableField("form_id")
    private Long formId;

    @TableField("url")
    private String url;


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

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


    public Long getProcessId() {
        return processId;
    }

    public void setProcessId(Long processId) {
        this.processId = processId;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getFormId() {
        return formId;
    }

    public void setFormId(Long formId) {
        this.formId = formId;
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

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "ActivitiProcess{" +
                "processId=" + processId +
                ", processName=" + processName +
                ", categoryId=" + categoryId +
                ", type=" + type +
                ", formId=" + formId +
                ", display=" + display +
                ", createUser=" + createUser +
                ", updateUser=" + updateUser +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                "}";
    }
}
