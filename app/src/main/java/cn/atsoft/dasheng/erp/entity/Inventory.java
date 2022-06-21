package cn.atsoft.dasheng.inventory.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;

/**
 * <p>
 * 盘点任务主表
 * </p>
 *
 * @author Captain_Jazz
 * @since 2021-12-27
 */
@TableName("daoxin_erp_inventory")
public class Inventory implements Serializable {

    /**
     * 附件
     */
    @TableField("enclosure")
    private String enclosure;

    @TableField("user_id")
    private Long userId;
    /**
     * 参与人员
     */
    @TableField("participants")
    private String participants;

    @TableField("notice")
    private String notice;

    @TableField("end_time")
    private Date endTime;

    @TableField("begin_time")
    private Date beginTime;

    @TableField("mode")
    private String mode;

    @TableField("method")
    private String method;

    private static final long serialVersionUID = 1L;

    /**
     * 盘点任务id
     */
    @TableId(value = "inventory_task_id", type = IdType.ID_WORKER)
    private Long inventoryTaskId;

    /**
     * 盘点任务名称
     */
    @TableField("inventory_task_name")
    private String inventoryTaskName;

    /**
     * 盘点任务备注
     */
    @TableField("remark")
    private String remark;

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

    /**
     * 部门id
     */
    @TableField("deptId")
    private Long deptId;


    public Long getInventoryTaskId() {
        return inventoryTaskId;
    }

    public void setInventoryTaskId(Long inventoryTaskId) {
        this.inventoryTaskId = inventoryTaskId;
    }

    public String getInventoryTaskName() {
        return inventoryTaskName;
    }

    public void setInventoryTaskName(String inventoryTaskName) {
        this.inventoryTaskName = inventoryTaskName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getEnclosure() {
        return enclosure;
    }

    public void setEnclosure(String enclosure) {
        this.enclosure = enclosure;
    }

    public String getParticipants() {
        return participants;
    }

    public void setParticipants(String participants) {
        this.participants = participants;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Inventory{" +
                "inventoryTaskId=" + inventoryTaskId +
                ", inventoryTaskName=" + inventoryTaskName +
                ", remark=" + remark +
                ", display=" + display +
                ", createUser=" + createUser +
                ", updateUser=" + updateUser +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", deptId=" + deptId +
                "}";
    }
}
