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
 * 质检方案详情
 * </p>
 *
 * @author Captain_Jazz
 * @since 2021-10-28
 */
@TableName("daoxin_erp_quality_plan_detail")
public class QualityPlanDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 单位id
     */
    @TableField("unit_id")
    private Long unitId;

    @TableId(value = "plan_detail_id", type = IdType.ID_WORKER)
    private Long planDetailId;

    /**
     * 关联质检方案表主键id
     */
    @TableField("plan_id")
    private Long planId;

    /**
     * 质检项id
     */
    @TableField("quality_check_id")
    private Long qualityCheckId;

    /**
     * 运算符
     */
    @TableField("operator")
    private Long operator;

    /**
     * 标准值
     */
    @TableField("standard_value")
    private String standardValue;

    /**
     * 抽检类型
     */
    @TableField("testing_type")
    private String testingType;

    /**
     * 质检数量
     */
    @TableField("quality_amount")
    private Long qualityAmount;

    /**
     * 质检数量
     */
    @TableField("is_null")
    private Long isNull;


    /**
     * 质检比例
     */
    @TableField("quality_proportion")
    private Long qualityProportion;

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
     * 排序
     */
    @TableField("sort")
    private Long sort;

    /**
     * 部门编号
     */
    @TableField(value = "deptId" , fill = FieldFill.INSERT)
    private Long deptId;


    public Long getPlanDetailId() {
        return planDetailId;
    }

    public void setPlanDetailId(Long planDetailId) {
        this.planDetailId = planDetailId;
    }

    public Long getPlanId() {
        return planId;
    }

    public void setPlanId(Long planId) {
        this.planId = planId;
    }

    public Long getQualityCheckId() {
        return qualityCheckId;
    }

    public void setQualityCheckId(Long qualityCheckId) {
        this.qualityCheckId = qualityCheckId;
    }

    public Long getOperator() {
        return operator;
    }

    public void setOperator(Long operator) {
        this.operator = operator;
    }

    public String getStandardValue() {
        return standardValue;
    }

    public void setStandardValue(String standardValue) {
        this.standardValue = standardValue;
    }

    public String getTestingType() {
        return testingType;
    }

    public void setTestingType(String testingType) {
        this.testingType = testingType;
    }

    public Long getQualityAmount() {
        return qualityAmount;
    }

    public void setQualityAmount(Long qualityAmount) {
        this.qualityAmount = qualityAmount;
    }

    public Long getQualityProportion() {
        return qualityProportion;
    }

    public void setQualityProportion(Long qualityProportion) {
        this.qualityProportion = qualityProportion;
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

    public Long getIsNull() {
        return isNull;
    }

    public void setIsNull(Long isNull) {
        this.isNull = isNull;
    }

    public Long getSort() {
        return sort;
    }

    public void setSort(Long sort) {
        this.sort = sort;
    }

    public Long getUnitId() {
        return unitId;
    }

    public void setUnitId(Long unitId) {
        this.unitId = unitId;
    }

    @Override
    public String toString() {
        return "QualityPlanDetail{" +
                "planDetailId=" + planDetailId +
                ", planId=" + planId +
                ", qualityCheckId=" + qualityCheckId +
                ", operator=" + operator +
                ", standardValue=" + standardValue +
                ", testingType=" + testingType +
                ", qualityAmount=" + qualityAmount +
                ", qualityProportion=" + qualityProportion +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", createUser=" + createUser +
                ", updateUser=" + updateUser +
                ", display=" + display +
                ", deptId=" + deptId +
                "}";
    }
}
