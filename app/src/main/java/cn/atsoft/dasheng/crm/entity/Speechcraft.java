package cn.atsoft.dasheng.crm.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 话术基础资料
 * </p>
 *
 * @author 
 * @since 2021-09-11
 */
@TableName("daoxin_crm_speechcraft")
public class Speechcraft implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "speechcraft_id", type = IdType.ID_WORKER)
    private Long speechcraftId;

    /**
     * 标题
     */
    @TableField("speechcraft_title")
    private String speechcraftTitle;

    /**
     * 详情
     */
    @TableField("speechcraft_details")
    private String speechcraftDetails;

    /**
     * 关键词
     */
    @TableField("speechcraft_key")
    private String speechcraftKey;

    /**
     * 话术分类
     */
    @TableField("speechcraft_type")
    private Long speechcraftType;



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
    @TableField(value = "deptId",fill =FieldFill.INSERT)
    private Long deptId;

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    @TableField(value = "tenant_id" , fill = FieldFill.INSERT)
    private Long tenantId;

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Long getSpeechcraftId() {
        return speechcraftId;
    }

    public void setSpeechcraftId(Long speechcraftId) {
        this.speechcraftId = speechcraftId;
    }

    public String getSpeechcraftTitle() {
        return speechcraftTitle;
    }

    public void setSpeechcraftTitle(String speechcraftTitle) {
        this.speechcraftTitle = speechcraftTitle;
    }

    public String getSpeechcraftDetails() {
        return speechcraftDetails;
    }

    public void setSpeechcraftDetails(String speechcraftDetails) {
        this.speechcraftDetails = speechcraftDetails;
    }

    public String getSpeechcraftKey() {
        return speechcraftKey;
    }

    public void setSpeechcraftKey(String speechcraftKey) {
        this.speechcraftKey = speechcraftKey;
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
    public Long getSpeechcraftType() {
        return speechcraftType;
    }

    public void setSpeechcraftType(Long speechcraftType) {
        this.speechcraftType = speechcraftType;
    }
    @Override
    public String toString() {
        return "Speechcraft{" +
        "speechcraftId=" + speechcraftId +
        ", speechcraftTitle=" + speechcraftTitle +
        ", speechcraftDetails=" + speechcraftDetails +
        ", speechcraftKey=" + speechcraftKey +
        ", createUser=" + createUser +
        ", updateUser=" + updateUser +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        ", display=" + display +
        "}";
    }
}
