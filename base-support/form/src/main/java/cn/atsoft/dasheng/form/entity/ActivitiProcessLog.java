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
 * 流程日志表
 * </p>
 *
 * @author Sing
 * @since 2021-11-10
 */
@TableName("activiti_process_log")
public class ActivitiProcessLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "log_id", type = IdType.ID_WORKER)
    private Long logId;

    /**
     * 流程Id
     */
    @TableField("peocess_id")
    private Long peocessId;

    /**
     * 动作状态
     */
    @TableField("action_status")
    private String actionStatus;

    /**
     * 步骤Id
     */
    @TableField("setps_id")
    private Long setpsId;
    /**
     * 步骤Id
     */
    @TableField("task_id")
    private Long taskId;
    /**
     * 步骤Id
     */
    @TableField("route_log_id")
    private Long routeLogId;

    /**
     * 步骤Id
     */
    @TableField("audit_user_id")
    private Long auditUserId;

    /**
     * 0（拒绝），1（通过）
     */
    @TableField("status")
    private Integer status;

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


    public Long getLogId() {
        return logId;
    }

    public void setLogId(Long logId) {
        this.logId = logId;
    }

    public Long getPeocessId() {
        return peocessId;
    }

    public void setPeocessId(Long peocessId) {
        this.peocessId = peocessId;
    }

    public Long getSetpsId() {
        return setpsId;
    }

    public void setSetpsId(Long setpsId) {
        this.setpsId = setpsId;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    public String getActionStatus() {
        return actionStatus;
    }

    public void setActionStatus(String actionStatus) {
        this.actionStatus = actionStatus;
    }

    public Long getAuditUserId() {
        return auditUserId;
    }

    public void setAuditUserId(Long auditUserId) {
        this.auditUserId = auditUserId;
    }

    public Long getRouteLogId() {
        return routeLogId;
    }

    public void setRouteLogId(Long routeLogId) {
        this.routeLogId = routeLogId;
    }

    @Override
    public String toString() {
        return "ActivitiProcessLog{" +
                "logId=" + logId +
                ", peocessId=" + peocessId +
                ", actionStatus='" + actionStatus + '\'' +
                ", setpsId=" + setpsId +
                ", taskId=" + taskId +
                ", routeLogId=" + routeLogId +
                ", auditUserId=" + auditUserId +
                ", status=" + status +
                ", display=" + display +
                ", createUser=" + createUser +
                ", updateUser=" + updateUser +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
