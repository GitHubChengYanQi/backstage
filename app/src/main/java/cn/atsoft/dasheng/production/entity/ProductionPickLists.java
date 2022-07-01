package cn.atsoft.dasheng.production.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * <p>
 * 领料单
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-03-25
 */
@TableName("daoxin_production_pick_lists")
public class ProductionPickLists implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 领料单
     */
      @TableId(value = "pick_lists_id", type = IdType.ID_WORKER)
    private Long pickListsId;

    /**
     * 领取物料码
     */
    @TableField("pick_lists_name")
    private String pickListsName;
    /**
     * 领取物料码
     */
    @TableField("coding")
    private String coding;

    @TableField("user_id")
    private Long userId;

    @TableField("user_ids")
    private Long userIds;

    /**
     * 来源
     */
    @TableField("source")
    private String source;
    /**
     * 附件
     */
    @TableField("enclosure")
    private String enclosure;
    /**
     * remark
     */
    @TableField("remarks")
    private String remarks;

    /**
     * remark
     */
    @TableField("note")
    private String note;

    /**
     * 来源id
     */
    @TableField("source_id")
    private Long sourceId;

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
    @TableField("deptId")
    private Long deptId;

    /**
     * 状态
     */
    @TableField("status")
    private Long status;

    public Long getUserIds() {
        return userIds;
    }

    public void setUserIds(Long userIds) {
        this.userIds = userIds;
    }

    public Long getPickListsId() {
        return pickListsId;
    }

    public void setPickListsId(Long pickListsId) {
        this.pickListsId = pickListsId;
    }

    public String getCoding() {
        return coding;
    }

    public void setCoding(String coding) {
        this.coding = coding;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Long getSourceId() {
        return sourceId;
    }

    public void setSourceId(Long sourceId) {
        this.sourceId = sourceId;
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

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public String getEnclosure() {
        return enclosure;
    }

    public void setEnclosure(String enclosure) {
        this.enclosure = enclosure;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getPickListsName() {
        return pickListsName;
    }

    public void setPickListsName(String pickListsName) {
        this.pickListsName = pickListsName;
    }

    @Override
    public String toString() {
        return "ProductionPickLists{" +
        "pickListsId=" + pickListsId +
        ", userId=" + userId +
        ", source=" + source +
        ", sourceId=" + sourceId +
        ", createUser=" + createUser +
        ", updateUser=" + updateUser +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        ", display=" + display +
        ", deptId=" + deptId +
        ", status=" + status +
        "}";
    }
}
