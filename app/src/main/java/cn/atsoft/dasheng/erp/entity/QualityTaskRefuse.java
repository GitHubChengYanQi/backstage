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
 * 质检任务拒检
 * </p>
 *
 * @author song
 * @since 2021-12-14
 */
@TableName("daoxin_erp_quality_task_refuse")
public class QualityTaskRefuse implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "refuse_id", type = IdType.ID_WORKER)
    private Long refuseId;

    /**
     * 质检任务id
     */
    @TableField("quality_task_id")
    private Long qualityTaskId;
    /**
     * 备注
     */
    @TableField("note")
    private String note;

    /**
     * 质检任务详情id
     */
    @TableField("quality_task_detail_id")
    private Long qualityTaskDetailId;

    /**
     * 物料id
     */
    @TableField("sku_id")
    private Long skuId;

    /**
     * 品牌
     */
    @TableField("brand_id")
    private Long brandId;

    /**
     * 拒绝数量
     */
    @TableField("number")
    private Long number;

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
    public Long getRefuseId() {
        return refuseId;
    }

    public void setRefuseId(Long refuseId) {
        this.refuseId = refuseId;
    }

    public Long getQualityTaskId() {
        return qualityTaskId;
    }

    public void setQualityTaskId(Long qualityTaskId) {
        this.qualityTaskId = qualityTaskId;
    }

    public Long getQualityTaskDetailId() {
        return qualityTaskDetailId;
    }

    public void setQualityTaskDetailId(Long qualityTaskDetailId) {
        this.qualityTaskDetailId = qualityTaskDetailId;
    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return "QualityTaskRefuse{" +
                "refuseId=" + refuseId +
                ", qualityTaskId=" + qualityTaskId +
                ", qualityTaskDetailId=" + qualityTaskDetailId +
                ", skuId=" + skuId +
                ", brandId=" + brandId +
                ", number=" + number +
                ", createUser=" + createUser +
                ", updateUser=" + updateUser +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", display=" + display +
                "}";
    }
}
