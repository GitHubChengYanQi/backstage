package cn.atsoft.dasheng.crm.entity;

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
 * 付款信息表
 * </p>
 *
 * @author song
 * @since 2022-02-23
 */
@TableName("daoxin_crm_payment")
public class Payment implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 付款信息id
     */
    @TableId(value = "payment_id", type = IdType.ID_WORKER)
    private Long paymentId;

    /**
     * 状态
     */
    @TableField("status")
    private Integer status;

    @TableField("type")
    private Integer type;

    @TableField("money")
    private Integer money;

    /**
     * 订单编号
     */
    @TableField("order_id")
    private Long orderId;

    /**
     * 结算方式
     */
    @TableField("pay_method")
    private String payMethod;

    /**
     * 是否运费
     */
    @TableField("freight")
    private Integer freight;

    /**
     * 名称
     */
    @TableField("name")
    private String name;

    /**
     * 交货方式
     */
    @TableField("delivery_way")
    private String deliveryWay;

    /**
     * 交货地址
     */
    @TableField("adress_id")
    private Long adressId;

    /**
     * 付款方式
     */
    @TableField("pay_plan")
    private Long payPlan;

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
     * 部门编号
     */
    @TableField("deptId")
    private Long deptId;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

    public Integer getFreight() {
        return freight;
    }

    public void setFreight(Integer freight) {
        this.freight = freight;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDeliveryWay() {
        return deliveryWay;
    }

    public void setDeliveryWay(String deliveryWay) {
        this.deliveryWay = deliveryWay;
    }

    public Long getAdressId() {
        return adressId;
    }

    public void setAdressId(Long adressId) {
        this.adressId = adressId;
    }

    public Long getPayPlan() {
        return payPlan;
    }

    public void setPayPlan(Long payPlan) {
        this.payPlan = payPlan;
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

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getMoney() {
        return money;
    }

    public void setMoney(Integer money) {
        this.money = money;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "paymentId=" + paymentId +
                ", orderId=" + orderId +
                ", payMethod=" + payMethod +
                ", freight=" + freight +
                ", name=" + name +
                ", deliveryWay=" + deliveryWay +
                ", adressId=" + adressId +
                ", payPlan=" + payPlan +
                ", createUser=" + createUser +
                ", updateUser=" + updateUser +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", display=" + display +
                ", deptId=" + deptId +
                ", remark=" + remark +
                "}";
    }
}
