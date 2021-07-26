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
 * 订单表
 * </p>
 *
 * @author ta
 * @since 2021-07-20
 */
@TableName("daoxin_erp_order")
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
      @TableId(value = "id", type = IdType.ID_WORKER)
    private Long id;

    /**
     * 订单编号
     */
    @TableField("order_id")
    private Long orderId;

    /**
     * 订单人姓名
     */
    @TableField("name")
    private String name;

    /**
     * 订单地址
     */
    @TableField("adress_id")
    private String adressId;

    /**
     * 订单数量
     */
    @TableField("numbers")
    private Long numbers;

    /**
     * 订单状态
     */
    @TableField("state")
    private String state;

    /**
     * 联系电话
     */
    @TableField("phone")
    private Long phone;

    /**
     * 订单时间
     */
    @TableField("order_time")
    private Date orderTime;

    /**
     * 付款时间
     */
    @TableField("pay_time")
    private Date payTime;

    /**
     * 发货时间
     */
    @TableField("delivery_time")
    private Date deliveryTime;

    /**
     * 订单总金额
     */
    @TableField("total")
    private Integer total;

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


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAdressId() {
        return adressId;
    }

    public void setAdressId(String adressId) {
        this.adressId = adressId;
    }

    public Long getNumbers() {
        return numbers;
    }

    public void setNumbers(Long numbers) {
        this.numbers = numbers;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Long getPhone() {
        return phone;
    }

    public void setPhone(Long phone) {
        this.phone = phone;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public Date getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(Date deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
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

    @Override
    public String toString() {
        return "Order{" +
        "id=" + id +
        ", orderId=" + orderId +
        ", name=" + name +
        ", adressId=" + adressId +
        ", numbers=" + numbers +
        ", state=" + state +
        ", phone=" + phone +
        ", orderTime=" + orderTime +
        ", payTime=" + payTime +
        ", deliveryTime=" + deliveryTime +
        ", total=" + total +
        ", createUser=" + createUser +
        ", updateUser=" + updateUser +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        ", display=" + display +
        "}";
    }
}
