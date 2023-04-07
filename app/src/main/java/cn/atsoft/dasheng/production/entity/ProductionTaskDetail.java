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
 * 
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-03-22
 */
@TableName("daoxin_production_task_detail")
public class ProductionTaskDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 子表id
     */
      @TableId(value = "production_task_detail_id", type = IdType.ID_WORKER)
    private Long productionTaskDetailId;

    /**
     * 主表id
     */
    @TableField("production_task_id")
    private Long productionTaskId;

    /**
     * 创建者
     */
      @TableField(value = "create_user", fill = FieldFill.INSERT)
    private Long createUser;

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
     * 实物
     */
    @TableField("inkind_id")
    private Long inkindId;
    /**
     * 实物
     */
    @TableField("out_sku_id")
    private Long outSkuId;

    /**
     * 实物
     */
    @TableField("quality_id")
    private Long qualityId;

    /**
     * 实物
     */
    @TableField("my_quality_id")
    private Long myQualityId;

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    /**
     * 实物
     */
    @TableField("number")
    private Integer number;

    public Long getOutSkuId() {
        return outSkuId;
    }

    public void setOutSkuId(Long outSkuId) {
        this.outSkuId = outSkuId;
    }

    public Long getQualityId() {
        return qualityId;
    }

    public void setQualityId(Long qualityId) {
        this.qualityId = qualityId;
    }

    public Long getMyQualityId() {
        return myQualityId;
    }

    public void setMyQualityId(Long myQualityId) {
        this.myQualityId = myQualityId;
    }

    public Long getProductionTaskDetailId() {
        return productionTaskDetailId;
    }

    public void setProductionTaskDetailId(Long productionTaskDetailId) {
        this.productionTaskDetailId = productionTaskDetailId;
    }

    public Long getProductionTaskId() {
        return productionTaskId;
    }

    public void setProductionTaskId(Long productionTaskId) {
        this.productionTaskId = productionTaskId;
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

    public Long getInkindId() {
        return inkindId;
    }

    public void setInkindId(Long inkindId) {
        this.inkindId = inkindId;
    }

    @Override
    public String toString() {
        return "ProductionTaskDetail{" +
        "productionTaskDetailId=" + productionTaskDetailId +
        ", productionTaskId=" + productionTaskId +
        ", createUser=" + createUser +
        ", deptId=" + deptId +
        ", display=" + display +
        ", updateTime=" + updateTime +
        ", createTime=" + createTime +
        ", updateUser=" + updateUser +
        ", inkindId=" + inkindId +
        "}";
    }
}
