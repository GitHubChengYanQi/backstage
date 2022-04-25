package cn.atsoft.dasheng.crm.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.models.auth.In;

import java.io.Serializable;

/**
 * <p>
 * 单据状态
 * </p>
 *
 * @author song
 * @since 2022-04-23
 */
@TableName("daoxin_documents_status")
public class DocumentsStatus implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 通用单据状态id
     */
      @TableId(value = "documents_status_id", type = IdType.ID_WORKER)
    private Long documentsStatusId;

    /**
     * 动作
     */
    @TableField("action")
    private String action;

    /**
     * 表单状态
     */
    @TableField("form_status")
    private Integer formStatus;

    /**
     * 表单类型
     */
    @TableField("form_type")
    private String formType;

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


    public Long getDocumentsStatusId() {
        return documentsStatusId;
    }

    public void setDocumentsStatusId(Long documentsStatusId) {
        this.documentsStatusId = documentsStatusId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Integer getFormStatus() {
        return formStatus;
    }

    public void setFormStatus(Integer formStatus) {
        this.formStatus = formStatus;
    }

    public String getFormType() {
        return formType;
    }

    public void setFormType(String formType) {
        this.formType = formType;
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
        return "DocumentsStatus{" +
        "documentsStatusId=" + documentsStatusId +
        ", action=" + action +
        ", formStatus=" + formStatus +
        ", formType=" + formType +
        ", createUser=" + createUser +
        ", updateUser=" + updateUser +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        ", display=" + display +
        "}";
    }
}
