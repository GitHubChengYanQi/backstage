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
 * 质检任务详情
 * </p>
 *
 * @author
 * @since 2021-11-16
 */
@TableName("daoxin_erp_quality_task_detail")
public class QualityTaskDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "quality_task_detail_id", type = IdType.ID_WORKER)
    private Long qualityTaskDetailId;

    /**
     * 物料id
     */
    @TableField("sku_id")
    private Long skuId;

    /**
     * 质检项
     */
    @TableField("quality_plan_id")
    private Long qualityPlanId;

    /**
     * 物料id
     */
    @TableField("brand_id")
    private Long brandId;

    /**
     * 数量
     */
    @TableField("number")
    private Integer number;

    /**
     * 负责人s
     */
    @TableField("userIds")
    private String userIds;

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
     * 对接人
     */
    @TableField("person")
    private Long person;

    @TableField("remaining")
    private Integer remaining;

    public Integer getRemaining() {
        return remaining;
    }

    public void setRemaining(Integer remaining) {
        this.remaining = remaining;
    }

    @TableField("batch")
    private Integer batch;

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getUserIds() {
        return userIds;
    }

    public void setUserIds(String userIds) {
        this.userIds = userIds;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getPerson() {
        return person;
    }

    public void setPerson(Long person) {
        this.person = person;
    }

    /**
     * 主表id
     */
    @TableField("quality_task_id")
    private Long qualityTaskId;

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


    public Long getQualityTaskDetailId() {
        return qualityTaskDetailId;
    }

    public void setQualityTaskDetailId(Long qualityTaskDetailId) {
        this.qualityTaskDetailId = qualityTaskDetailId;
    }

    public Long getSkuId() {
        return skuId;
    }

    public Long getQualityPlanId() {
        return qualityPlanId;
    }

    public void setQualityPlanId(Long qualityPlanId) {
        this.qualityPlanId = qualityPlanId;
    }

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public Long getQualityTaskId() {
        return qualityTaskId;
    }

    public void setQualityTaskId(Long qualityTaskId) {
        this.qualityTaskId = qualityTaskId;
    }

    public Long getCreateUser() {
        return createUser;
    }

    public Integer getBatch() {
        return batch;
    }

    public void setBatch(Integer batch) {
        this.batch = batch;
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

    @Override
    public String toString() {
        return "QualityTaskDetail{" +
                "qualityTaskDetailId=" + qualityTaskDetailId +
                ", skuId=" + skuId +
                ", qualityTaskId=" + qualityTaskId +
                ", createUser=" + createUser +
                ", updateUser=" + updateUser +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", display=" + display +
                ", deptId=" + deptId +
                "}";
    }
}
