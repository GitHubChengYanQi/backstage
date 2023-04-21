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
 * 话术分类详细
 * </p>
 *
 * @author cheng
 * @since 2021-09-13
 */
@TableName("daoxin_crm_speechcraft_type_detail")
public class SpeechcraftTypeDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 话术分类详细id
     */
      @TableId(value = "speechcraft_type_detail_id", type = IdType.ID_WORKER)
    private Long speechcraftTypeDetailId;

    /**
     * 名称
     */
    @TableField("name")
    private String name;

    /**
     * 话术分类id
     */
    @TableField("speechcraft_type_id")
    private Long speechcraftTypeId;

    /**
     * 排序
     */
    @TableField("sort")
    private Long sort;

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

    public Long getSpeechcraftTypeDetailId() {
        return speechcraftTypeDetailId;
    }

    public void setSpeechcraftTypeDetailId(Long speechcraftTypeDetailId) {
        this.speechcraftTypeDetailId = speechcraftTypeDetailId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getSpeechcraftTypeId() {
        return speechcraftTypeId;
    }

    public void setSpeechcraftTypeId(Long speechcraftTypeId) {
        this.speechcraftTypeId = speechcraftTypeId;
    }

    public Long getSort() {
        return sort;
    }

    public void setSort(Long sort) {
        this.sort = sort;
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
        return "SpeechcraftTypeDetail{" +
        "speechcraftTypeDetailId=" + speechcraftTypeDetailId +
        ", name=" + name +
        ", speechcraftTypeId=" + speechcraftTypeId +
        ", sort=" + sort +
        ", createUser=" + createUser +
        ", updateUser=" + updateUser +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        ", display=" + display +
        "}";
    }
}
