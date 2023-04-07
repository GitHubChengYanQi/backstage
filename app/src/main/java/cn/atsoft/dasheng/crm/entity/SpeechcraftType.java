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
 * 话术分类
 * </p>
 *
 * @author 
 * @since 2021-09-13
 */
@TableName("daoxin_crm_speechcraft_type")
public class SpeechcraftType implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 分类id
     */
      @TableId(value = "speechcraft_type_id", type = IdType.ID_WORKER)
    private Long speechcraftTypeId;

    /**
     * 分类排序
     */
    @TableField("speechcraft_type_sort")
    private Long speechcraftTypeSort;

    /**
     * 分类名称
     */
    @TableField("speechcraft_type_name")
    private String speechcraftTypeName;

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


    public Long getSpeechcraftTypeId() {
        return speechcraftTypeId;
    }

    public void setSpeechcraftTypeId(Long speechcraftTypeId) {
        this.speechcraftTypeId = speechcraftTypeId;
    }

    public Long getSpeechcraftTypeSort() {
        return speechcraftTypeSort;
    }

    public void setSpeechcraftTypeSort(Long speechcraftTypeSort) {
        this.speechcraftTypeSort = speechcraftTypeSort;
    }

    public String getSpeechcraftTypeName() {
        return speechcraftTypeName;
    }

    public void setSpeechcraftTypeName(String speechcraftTypeName) {
        this.speechcraftTypeName = speechcraftTypeName;
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
        return "SpeechcraftType{" +
        "speechcraftTypeId=" + speechcraftTypeId +
        ", speechcraftTypeSort=" + speechcraftTypeSort +
        ", speechcraftTypeName=" + speechcraftTypeName +
        ", createUser=" + createUser +
        ", updateUser=" + updateUser +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        ", display=" + display +
        "}";
    }
}
