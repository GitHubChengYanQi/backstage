package cn.atsoft.dasheng.production.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 生产任务
 * </p>
 *
 * @author 
 * @since 2022-02-28
 */
@TableName("daoxin_production_task")
public class ProductionTask implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 生产任务id
     */
      @TableId(value = "production_task_id", type = IdType.ID_WORKER)
    private Long productionTaskId;

    /**
     * 编码
     */
    @TableField("coding")
    private String coding;

    /**
     * 生产任务名称
     */
    @TableField("production_task_name")
    private String productionTaskName;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

    /**
     * 负责人
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 工单id
     */
    @TableField("work_order_id")
    private Long workOrderId;

    /**
     * 工序id
     */
    @TableField("ship_setp_id")
    private Long shipSetpId;

    public Integer getSingleProductionCycle() {
        return singleProductionCycle;
    }

    public void setSingleProductionCycle(Integer singleProductionCycle) {
        this.singleProductionCycle = singleProductionCycle;
    }

    /**
     * 单台生产周期（天）
     */
    @TableField("single_production_cycle")
    private Integer singleProductionCycle;

    /**
     * 生产时间
     */
    @TableField("production_time")
    private Date productionTime;

    /**
     * 创建者
     */
      @TableField(value = "create_user", fill = FieldFill.INSERT)
    private Long createUser;

    /**
     * 部门编号
     */
    @TableField("deptId")
    private Long deptId;

    /**
     * 状态
     */
    @TableField("display")
    private Integer display;

    /**
     * 修改时间
     */
      @TableField(value = "update_time", fill = FieldFill.UPDATE)
    private Date updateTime;

    /**
     * 创建时间
     */
      @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 修改者
     */
      @TableField(value = "update_user", fill = FieldFill.UPDATE)
    private Long updateUser;

    /**
     * 主题
     */
    @TableField("theme")
    private String theme;

    /**
     * 来源Json字符串
     */
    @TableField("origin")
    private String origin;


    public Long getProductionTaskId() {
        return productionTaskId;
    }

    public void setProductionTaskId(Long productionTaskId) {
        this.productionTaskId = productionTaskId;
    }

    public String getCoding() {
        return coding;
    }

    public void setCoding(String coding) {
        this.coding = coding;
    }

    public String getProductionTaskName() {
        return productionTaskName;
    }

    public void setProductionTaskName(String productionTaskName) {
        this.productionTaskName = productionTaskName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getWorkOrderId() {
        return workOrderId;
    }

    public void setWorkOrderId(Long workOrderId) {
        this.workOrderId = workOrderId;
    }

    public Long getShipSetpId() {
        return shipSetpId;
    }

    public void setShipSetpId(Long shipSetpId) {
        this.shipSetpId = shipSetpId;
    }

    public Date getProductionTime() {
        return productionTime;
    }

    public void setProductionTime(Date productionTime) {
        this.productionTime = productionTime;
    }

    public Long getCreateUser() {
        return createUser;
    }

    public void setCreateUser(Long createUser) {
        this.createUser = createUser;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public Integer getDisplay() {
        return display;
    }

    public void setDisplay(Integer display) {
        this.display = display;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(Long updateUser) {
        this.updateUser = updateUser;
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

    @Override
    public String toString() {
        return "ProductionTask{" +
        "productionTaskId=" + productionTaskId +
        ", coding=" + coding +
        ", productionTaskName=" + productionTaskName +
        ", remark=" + remark +
        ", userId=" + userId +
        ", workOrderId=" + workOrderId +
        ", shipSetpId=" + shipSetpId +
        ", productionTime=" + productionTime +
        ", createUser=" + createUser +
        ", deptId=" + deptId +
        ", display=" + display +
        ", updateTime=" + updateTime +
        ", createTime=" + createTime +
        ", updateUser=" + updateUser +
        ", theme=" + theme +
        ", origin=" + origin +
        "}";
    }
}
