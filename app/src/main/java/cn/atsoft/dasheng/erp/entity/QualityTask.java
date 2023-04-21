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
 * 质检任务
 * </p>
 *
 * @author
 * @since 2021-11-16
 */
@TableName("daoxin_erp_quality_task")
public class QualityTask implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "quality_task_id", type = IdType.ID_WORKER)
    private Long qualityTaskId;
    /**
     * 多个人负责人
     */
    @TableField("userIds")
    private String userIds;
    /**
     * 表单id
     */
    @TableField("data_id")
    private Long dataId;

    /**
     * 主任务分派状态
     */
    @TableField("status")
    private Long status;
    /**
     * 时间
     */
    @TableField("time")
    private Date time;
    /**
     * 地点
     */
    @TableField("address")
    private String address;

    /**
     * 接头人
     */
    @TableField("person")
    private String person;
    /**
     * 电话
     */
    @TableField("phone")
    private String phone;
    /**
     * 备注
     */
    @TableField("note")
    private String note;
    /**
     * 父级id
     */
    @TableField("parent_id")
    private Long parentId;

    /**
     * 类型
     */
    @TableField("type")
    private String type;


    /**
     * 类型
     */
    @TableField("url")
    private String url;

    /**
     * 编码
     */
    @TableField("coding")
    private String coding;

    /**
     * 负责人
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

    /**
     * 来源
     */
    @TableField("source")
    private Long source;

    /**
     * 来源id
     */
    @TableField("source_id")
    private String sourceId;
    /**
     * 主题
     */
    @TableField("theme")
    private String theme;
    /**
     * 来源Json
     */
    @TableField("origin")
    private String origin;

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

    @TableField("state")
    private Integer state;


    /**
     * 部门id
     */
@TableField(value = "deptId" , fill = FieldFill.INSERT)
    private Long deptId;
    /**
     * 租户编号
     */
    @TableField(value = "tenant_id" , fill = FieldFill.INSERT)
    private Long tenantId;

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Long getStatus() {
        return status;
    }


    public Long getSource() {
        return source;
    }

    public void setSource(Long source) {
        this.source = source;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Long getQualityTaskId() {
        return qualityTaskId;
    }

    public void setQualityTaskId(Long qualityTaskId) {
        this.qualityTaskId = qualityTaskId;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCoding() {
        return coding;
    }

    public void setCoding(String coding) {
        this.coding = coding;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getUserIds() {
        return userIds;
    }

    public void setUserIds(String userIds) {
        this.userIds = userIds;
    }

    public Long getDataId() {
        return dataId;
    }

    public void setDataId(Long dataId) {
        this.dataId = dataId;
    }

    @Override
    public String toString() {
        return "QualityTask{" +
                "qualityTaskId=" + qualityTaskId +
                ", time=" + time +
                ", userId=" + userId +
                ", remark=" + remark +
                ", createUser=" + createUser +
                ", updateUser=" + updateUser +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", display=" + display +
                ", deptId=" + deptId +
                ", state=" + state +
                "}";
    }
}
