package cn.atsoft.dasheng.production.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.models.auth.In;

import java.io.Serializable;

/**
 * <p>
 * 生产计划子表
 * </p>
 *
 * @author
 * @since 2022-02-25
 */
@TableName("daoxin_production_plan_detail")
public class ProductionPlanDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 生产计划子表id
     */
    @TableId(value = "production_plan_detail_id", type = IdType.ID_WORKER)
    private Long productionPlanDetailId;

    /**
     * 生产计划id
     */
    @TableField("production_plan_id")
    private Long productionPlanId;

    /**
     * 产品id
     */
    @TableField("sku_id")
    private Long skuId;

    /**
     * 计划数量
     */
    @TableField("plan_number")
    private Integer planNumber;
    /**
     * 生产中的数量
     */
    @TableField("making_number")
    private Integer makingNumber;

    /**
     * 交付时间
     */
    @TableField("delivery_date")
    private Integer deliveryDate;

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

    public Long getOrderDetailId() {
        return orderDetailId;
    }

    public void setOrderDetailId(Long orderDetailId) {
        this.orderDetailId = orderDetailId;
    }

    /**
     * orderDetailId
     */
    @TableField("order_detail_id")
    private Long orderDetailId;

    /**
     * 合同编号
     */
    @TableField("contract_id")
    private  String contractId;

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    public Long getProductionPlanDetailId() {
        return productionPlanDetailId;
    }

    public void setProductionPlanDetailId(Long productionPlanDetailId) {
        this.productionPlanDetailId = productionPlanDetailId;
    }

    public Long getProductionPlanId() {
        return productionPlanId;
    }

    public void setProductionPlanId(Long productionPlanId) {
        this.productionPlanId = productionPlanId;
    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }


    public Integer getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Integer deliveryDate) {
        this.deliveryDate = deliveryDate;
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

    public Integer getPlanNumber() {
        return planNumber;
    }

    public void setPlanNumber(Integer planNumber) {
        this.planNumber = planNumber;
    }

    public Integer getMakingNumber() {
        return makingNumber;
    }

    public void setMakingNumber(Integer makingNumber) {
        this.makingNumber = makingNumber;
    }

    @Override
    public String toString() {
        return "PlanDetail{" +
                "productionPlanDetailId=" + productionPlanDetailId +
                ", productionPlanId=" + productionPlanId +
                ", skuId=" + skuId +
                ", number=" + planNumber +
                ", deliveryDate=" + deliveryDate +
                ", createUser=" + createUser +
                ", deptId=" + deptId +
                ", display=" + display +
                ", updateTime=" + updateTime +
                ", createTime=" + createTime +
                ", updateUser=" + updateUser +
                "}";
    }
}
