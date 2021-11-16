package cn.atsoft.dasheng.form.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 自定义表单主表
 * </p>
 *
 * @author song
 * @since 2021-11-16
 */
@TableName("form_data")
public class Data implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 数据主键
     */
      @TableId(value = "data_id", type = IdType.ID_WORKER)
    private Long dataId;

    /**
     * 所属模块
     */
    @TableField("module")
    private String module;

    /**
     * 自定义表单Id
     */
    @TableField("form_id")
    private Long formId;

    /**
     * 0为主表数据
     */
    @TableField("main_id")
    private Long mainId;

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


    public Long getDataId() {
        return dataId;
    }

    public void setDataId(Long dataId) {
        this.dataId = dataId;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public Long getFormId() {
        return formId;
    }

    public void setFormId(Long formId) {
        this.formId = formId;
    }

    public Long getMainId() {
        return mainId;
    }

    public void setMainId(Long mainId) {
        this.mainId = mainId;
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
        return "Data{" +
        "dataId=" + dataId +
        ", module=" + module +
        ", formId=" + formId +
        ", mainId=" + mainId +
        ", display=" + display +
        ", createUser=" + createUser +
        ", updateUser=" + updateUser +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        "}";
    }
}
