package cn.atsoft.dasheng.purchase.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 采购计划主表
 * </p>
 *
 * @author song
 * @since 2021-12-21
 */
@TableName("daoxin_procurement_plan")
public class ProcurementPlan implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 采购计划id
     */
      @TableId(value = "procurement_plan_id", type = IdType.ID_WORKER)
    private Long procurementPlanId;

    /**
     * 采购计划名称
     */
    @TableField("procurement_plan_name")
    private String procurementPlanName;

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
     * 主题
     */
    @TableField("theme")
    private String theme;

    /**
     * 来源
     */
    @TableField("origin")
    private String origin;

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

    /**
     * 要求供应商等级
     */
    @TableField("need_level")
    private Integer needLevel;

    /**
     * 非供应商物料
     */
    @TableField("is_spupplier")
    private Integer isSpupplier;

    /**
     * 删除状态
     */
    @TableField("display")
    private Integer display;

    /**
     * 状态
     */
    @TableField("status")
    private Integer status;

    /**
     * 交付日期
     */
    @TableField("delivery_date")
    private Date deliveryDate;

      @TableField(value = "create_user", fill = FieldFill.INSERT)
    private Long createUser;

      @TableField(value = "update_user", fill = FieldFill.UPDATE)
    private Long updateUser;
    @TableField(value = "deptId", fill = FieldFill.INSERT)
    private Long deptId;

    @TableField(value = "tenant_id" , fill = FieldFill.INSERT)
    private Long tenantId;

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }
      @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

      @TableField(value = "update_time", fill = FieldFill.UPDATE)
    private Date updateTime;


    public Long getProcurementPlanId() {
        return procurementPlanId;
    }

    public void setProcurementPlanId(Long procurementPlanId) {
        this.procurementPlanId = procurementPlanId;
    }

    public String getProcurementPlanName() {
        return procurementPlanName;
    }

    public void setProcurementPlanName(String procurementPlanName) {
        this.procurementPlanName = procurementPlanName;
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

    public Integer getNeedLevel() {
        return needLevel;
    }

    public void setNeedLevel(Integer needLevel) {
        this.needLevel = needLevel;
    }

    public Integer getIsSpupplier() {
        return isSpupplier;
    }

    public void setIsSpupplier(Integer isSpupplier) {
        this.isSpupplier = isSpupplier;
    }

    public Integer getDisplay() {
        return display;
    }

    public void setDisplay(Integer display) {
        this.display = display;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
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
        return "ProcurementPlan{" +
        "procurementPlanId=" + procurementPlanId +
        ", procurementPlanName=" + procurementPlanName +
        ", userId=" + userId +
        ", remark=" + remark +
        ", needLevel=" + needLevel +
        ", isSpupplier=" + isSpupplier +
        ", display=" + display +
        ", status=" + status +
        ", deliveryDate=" + deliveryDate +
        ", createUser=" + createUser +
        ", updateUser=" + updateUser +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        "}";
    }
}
