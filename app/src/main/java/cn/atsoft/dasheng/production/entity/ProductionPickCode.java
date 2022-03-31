package cn.atsoft.dasheng.production.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;
import java.io.Serializable;

/**
 * <p>
 * 领取物料码
 * </p>
 *
 * @author cheng
 * @since 2022-03-29
 */
@TableName("daoxin_production_pick_code")
public class ProductionPickCode implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 取件码id
     */
    @TableId(value = "pick_code_id", type = IdType.ID_WORKER)
    private Long pickCodeId;

    /**
     * 取件码
     */
    @TableField("code")
    private Long code;
    /**
     * 任务id
     */
    @TableField("production_task_id")
    private Long productionTaskId;
    
    /**
     * 取件码
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 关联领料单id
     */
    @TableField("pick_lists_id")
    private Long pickListsId;

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


    public Long getPickCodeId() {
        return pickCodeId;
    }

    public void setPickCodeId(Long pickCodeId) {
        this.pickCodeId = pickCodeId;
    }

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    public Long getPickListsId() {
        return pickListsId;
    }

    public void setPickListsId(Long pickListsId) {
        this.pickListsId = pickListsId;
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

    public Long getProductionTaskId() {
        return productionTaskId;
    }

    public void setProductionTaskId(Long productionTaskId) {
        this.productionTaskId = productionTaskId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    @Override
    public String toString() {
        return "ProductionPickCode{" +
        "pickCodeId=" + pickCodeId +
        ", code=" + code +
        ", pickListsId=" + pickListsId +
        ", createUser=" + createUser +
        ", updateUser=" + updateUser +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        ", display=" + display +
        ", deptId=" + deptId +
        "}";
    }
}
