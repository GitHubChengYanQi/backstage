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
 * 发货表
 * </p>
 *
 * @author
 * @since 2021-07-15
 */
@TableName("daoxin_order")
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 发货编号
     */
    @TableId(value = "order_id", type = IdType.ID_WORKER)
    private Long orderId;

    /**
     * 出库编号
     */
    @TableField("outbound_id")
    private Long outboundId;

    /**
     * 发货人
     */
    @TableField("consignor")
    private String consignor;

    /**
     * 收货人
     */


    /**
     * 收货地址
     */
    @TableField("shipping")
    private String shipping;


    /**
     * 发货价格
     */
    @TableField("price")
    private Integer price;

    /**
     * 物品重量
     */
    @TableField("weight")
    private Long weight;

    /**
     * 物品面积
     */
    @TableField("area")
    private Long area;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(value = "create_user", fill = FieldFill.INSERT)
    private Long createUser;

    @TableField(value = "update_time", fill = FieldFill.UPDATE)
    private Date updateTime;

    @TableField(value = "update_user", fill = FieldFill.UPDATE)
    private Long updateUser;

    @TableField("display")
    private Integer display;


    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getOutboundId() {
        return outboundId;
    }

    public void setOutboundId(Long outboundId) {
        this.outboundId = outboundId;
    }

    public String getConsignor() {
        return consignor;
    }

    public void setConsignor(String consignor) {
        this.consignor = consignor;
    }


    public String getShipping() {
        return shipping;
    }

    public void setShipping(String shipping) {
        this.shipping = shipping;
    }


    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Long getWeight() {
        return weight;
    }

    public void setWeight(Long weight) {
        this.weight = weight;
    }

    public Long getArea() {
        return area;
    }

    public void setArea(Long area) {
        this.area = area;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getCreateUser() {
        return createUser;
    }

    public void setCreateUser(Long createUser) {
        this.createUser = createUser;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
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

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", outboundId=" + outboundId +
                ", consignor=" + consignor +
                ", shipping=" + shipping +
                 ", price=" + price +
                ", weight=" + weight +
                ", area=" + area +
                ", createTime=" + createTime +
                ", createUser=" + createUser +
                ", updateTime=" + updateTime +
                ", updateUser=" + updateUser +
                ", display=" + display +
                "}";
    }
}
