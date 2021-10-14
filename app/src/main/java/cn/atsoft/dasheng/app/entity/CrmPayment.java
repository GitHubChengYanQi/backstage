package cn.atsoft.dasheng.app.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 付款信息表
 * </p>
 *
 * @author ta
 * @since 2021-07-26
 */
@TableName("daoxin_crm_payment")
public class CrmPayment implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 付款信息id
     */
      @TableId(value = "payment_id", type = IdType.ID_WORKER)
    private Long paymentId;

    /**
     * 订单编号
     */
    @TableField("order_id")
    private Long orderId;


    /**
     * 物品
     */
    @TableField("item_id")
    private Long itemId;
    /**
     * 出库
     */
    @TableField("outstock_id")
    private Long outstockId;

    /**
     * 付款时间
     */
    @TableField("pay_time")
    private Date payTime;

    /**
     * 支付方式
     */
    @TableField("pay_type")
    private String payType;

    /**
     * 运费
     */
    @TableField("freight")
    private Long freight;

    /**
     * 部门编号
     */
    @TableField(value = "deptId",fill =FieldFill.INSERT)
    private Long deptId;


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
     * 出库id
     */
    @TableField("delivery_id")
    private Long deliveryId;


    public static long getSerialVersionUID() {
        return serialVersionUID;
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

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public Long getOutstockId() {
        return outstockId;
    }

    public void setOutstockId(Long outstockId) {
        this.outstockId = outstockId;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public Long getFreight() {
        return freight;
    }

    public void setFreight(Long freight) {
        this.freight = freight;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
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

    public Long getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(Long deliveryId) {
        this.deliveryId = deliveryId;
    }


    @Override
    public String toString() {
        return "CrmPayment{" +
                "paymentId=" + paymentId +
                ", orderId=" + orderId +
                ", itemId=" + itemId +
                ", outstockId=" + outstockId +
                ", payTime=" + payTime +
                ", payType='" + payType + '\'' +
                ", freight=" + freight +
                ", deptId=" + deptId +
                ", createUser=" + createUser +
                ", updateUser=" + updateUser +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", display=" + display +
                ", deliveryId=" + deliveryId +
                '}';
    }
}
