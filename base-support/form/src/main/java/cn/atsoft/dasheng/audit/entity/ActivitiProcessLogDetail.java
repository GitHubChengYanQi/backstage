package cn.atsoft.dasheng.audit.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 流程日志人员表
 * </p>
 *
 * @author Captain_Jazz
 * @since 2023-02-16
 */
@TableName("activiti_process_log_detail")
public class ActivitiProcessLogDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
      @TableId(value = "log_detail_id", type = IdType.ID_WORKER)
    private Long logDetailId;

    /**
     * 主表id
     */
    @TableField("log_id")
    private Long logId;

    /**
     * 流程Id
     */
    @TableField("peocess_id")
    private Long peocessId;

    /**
     * 步骤Id
     */
    @TableField("setps_id")
    private Long setpsId;

    /**
     * 任务id
     */
    @TableField("task_id")
    private Long taskId;

    /**
     * 审批人id	
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 0（拒绝），1（通过） -1（创建)
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


    public Long getLogDetailId() {
        return logDetailId;
    }

    public void setLogDetailId(Long logDetailId) {
        this.logDetailId = logDetailId;
    }

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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    @Override
    public String toString() {
        return "ActivitiProcessLogDetail{" +
        "logDetailId=" + logDetailId +
        ", logId=" + logId +
        ", peocessId=" + peocessId +
        ", setpsId=" + setpsId +
        ", taskId=" + taskId +
        ", userId=" + userId +
        ", status=" + status +
        ", display=" + display +
        ", createUser=" + createUser +
        ", updateUser=" + updateUser +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        "}";
    }
}
