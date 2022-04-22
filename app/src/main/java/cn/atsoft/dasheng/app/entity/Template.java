package cn.atsoft.dasheng.app.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import org.omg.CORBA.LongHolder;

import java.io.Serializable;

/**
 * <p>
 * 合同模板
 * </p>
 *
 * @author
 * @since 2021-07-21
 */
@TableName("daoxin_crm_template")
public class Template implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 合同分类id
     */
    @TableField("contract_class_id")
    private Long contractClassId;


    @TableField("file_id")
    private Long fileId;


    @TableField("replace_rule")
    private String replaceRule;

    /**
     * 合同模板id
     */
    @TableId(value = "template_id", type = IdType.ID_WORKER)
    private Long templateId;

    @TableField(value = "deptId", fill = FieldFill.INSERT_UPDATE)
    private Long deptId;

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    @TableField("module")
    private String module;

    /**
     * 合同姓名
     */
    @TableField("name")
    private String name;

    /**
     * 合同内容
     */
    @TableField("content")
    private String content;

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

    public Long getContractClassId() {
        return contractClassId;
    }

    public void setContractClassId(Long contractClassId) {
        this.contractClassId = contractClassId;
    }

    public Long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public String getReplaceRule() {
        return replaceRule;
    }

    public void setReplaceRule(String replaceRule) {
        this.replaceRule = replaceRule;
    }

    @Override
    public String toString() {
        return "Template{" +
                "templateId=" + templateId +
                ", name=" + name +
                ", content=" + content +
                ", createUser=" + createUser +
                ", updateUser=" + updateUser +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", display=" + display +
                "}";
    }
}
