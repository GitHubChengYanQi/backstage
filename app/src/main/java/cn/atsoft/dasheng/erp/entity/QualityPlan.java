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
 * 质检方案
 * </p>
 *
 * @author Captain_Jazz
 * @since 2021-10-28
 */
@TableName("daoxin_erp_quality_plan")
public class QualityPlan implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 质检方案id
     */
      @TableId(value = "quality_plan_id", type = IdType.ID_WORKER)
    private Long qualityPlanId;

    /**
     * 抽检类型
     */
    @TableField("testing_type")
    private String testingType;

    /**
     * 方案名称
     */
    @TableField("plan_name")
    private String planName;

    /**
     * 编号
     */
    @TableField("plan_coding")
    private String planCoding;

    /**
     * 状态
     */
    @TableField("plan_status")
    private Integer planStatus;

    /**
     * 质检类型
     */
    @TableField("plan_type")
    private String planType;

    /**
     * 特别提醒
     */
    @TableField("attention_please")
    private String attentionPlease;

    /**
     * 附件
     */
    @TableField("plan_adjunct")
    private String planAdjunct;

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
     * 状态
     */
    @TableField("display")
    private Integer display;

    /**
     * 部门编号
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


    public Long getQualityPlanId() {
        return qualityPlanId;
    }

    public void setQualityPlanId(Long qualityPlanId) {
        this.qualityPlanId = qualityPlanId;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public Integer getPlanStatus() {
        return planStatus;
    }

    public void setPlanStatus(Integer planStatus) {
        this.planStatus = planStatus;
    }

    public String getPlanType() {
        return planType;
    }

    public void setPlanType(String planType) {
        this.planType = planType;
    }

    public String getAttentionPlease() {
        return attentionPlease;
    }

    public void setAttentionPlease(String attentionPlease) {
        this.attentionPlease = attentionPlease;
    }

    public String getPlanAdjunct() {
        return planAdjunct;
    }

    public void setPlanAdjunct(String planAdjunct) {
        this.planAdjunct = planAdjunct;
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
    public String getTestingType() {
        return testingType;
    }

    public void setTestingType(String testingType) {
        this.testingType = testingType;
    }
    public String getPlanCoding() {
        return planCoding;
    }

    public void setPlanCoding(String planCoding) {
        this.planCoding = planCoding;
    }
    @Override
    public String toString() {
        return "QualityPlan{" +
        "qualityPlanId=" + qualityPlanId +
        ", planName=" + planName +
        ", planStatus=" + planStatus +
        ", planType=" + planType +
        ", attentionPlease=" + attentionPlease +
        ", planAdjunct=" + planAdjunct +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        ", createUser=" + createUser +
        ", updateUser=" + updateUser +
        ", display=" + display +
        ", deptId=" + deptId +
        "}";
    }
}
