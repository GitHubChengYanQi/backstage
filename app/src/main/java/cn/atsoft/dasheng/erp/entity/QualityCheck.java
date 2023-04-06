package cn.atsoft.dasheng.erp.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 质检表
 * </p>
 *
 * @author song
 * @since 2021-10-27
 */
@TableName("daoxin_erp_quality_check")
public class QualityCheck implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 质检id
     */
      @TableId(value = "quality_check_id", type = IdType.ID_WORKER)
    private Long qualityCheckId;

    /**
     * 名称
     */
    @TableField("name")
    private String name;

    /**
     * 简称
     */
    @TableField("simple_name")
    private String simpleName;

    /**
     * 质检分类
     */
    @TableField("quality_check_classification_id")
    private Long qualityCheckClassificationId;

    /**
     * 工具id
     */
    @TableField("tool")
    private String tool;

    /**
     * 备注
     */
    @TableField("note")
    private String note;

    /**
     * 规范
     */
    @TableField("norm")
    private String norm;

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
     * 状态
     */
    @TableField("display")
    private Integer display;

    /**
     * 状态
     */
    @TableField("type")
    private Integer type;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * 部门编号
     */
    @TableField(value = "deptId" , fill = FieldFill.INSERT)
    private Long deptId;

    /**
     * 附件
     */
    @TableField("attachment")
    private String attachment;

    /**
     * 编码
     */
    @TableField("coding")
    private String coding;


    public Long getQualityCheckId() {
        return qualityCheckId;
    }

    public void setQualityCheckId(Long qualityCheckId) {
        this.qualityCheckId = qualityCheckId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSimpleName() {
        return simpleName;
    }

    public void setSimpleName(String simpleName) {
        this.simpleName = simpleName;
    }

    public Long getQualityCheckClassificationId() {
        return qualityCheckClassificationId;
    }

    public void setQualityCheckClassificationId(Long qualityCheckClassificationId) {
        this.qualityCheckClassificationId = qualityCheckClassificationId;
    }

    public String getTool() {
        return tool;
    }

    public void setTool(String tool) {
        this.tool = tool;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getNorm() {
        return norm;
    }

    public void setNorm(String norm) {
        this.norm = norm;
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

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public String getCoding() {
        return coding;
    }

    public void setCoding(String coding) {
        this.coding = coding;
    }

    @Override
    public String toString() {
        return "QualityCheck{" +
        "qualityCheckId=" + qualityCheckId +
        ", name=" + name +
        ", simpleName=" + simpleName +
        ", qualityCheckClassificationId=" + qualityCheckClassificationId +
        ", tool=" + tool +
        ", note=" + note +
        ", norm=" + norm +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        ", createUser=" + createUser +
        ", updateUser=" + updateUser +
        ", display=" + display +
        ", deptId=" + deptId +
        ", attachment=" + attachment +
        ", coding=" + coding +
        "}";
    }
}
