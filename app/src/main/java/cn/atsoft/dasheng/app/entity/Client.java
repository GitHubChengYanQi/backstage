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
 * 客户表
 * </p>
 *
 * @author 
 * @since 2021-07-15
 */
@TableName("daoxin_client")
public class Client implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 客户id
     */
      @TableId(value = "client_id", type = IdType.AUTO)
    private Long clientId;

    /**
     * 姓名
     */
    @TableField("name")
    private String name;

    /**
     * 居住地址
     */
    @TableField("adress")
    private String adress;

    /**
     * 联系电话
     */
    @TableField("phone")
    private Long phone;

    /**
     * 订单号
     */
    @TableField("order_id")
    private Long orderId;

    /**
     * 下单时间
     */
    @TableField("order_time")
    private Date orderTime;

    /**
     * 价格
     */
    @TableField("price")
    private Integer price;

    /**
     * 物流id
     */
    @TableField("logistics_id")
    private Long logisticsId;

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


    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public Long getPhone() {
        return phone;
    }

    public void setPhone(Long phone) {
        this.phone = phone;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Long getLogisticsId() {
        return logisticsId;
    }

    public void setLogisticsId(Long logisticsId) {
        this.logisticsId = logisticsId;
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

    @Override
    public String toString() {
        return "Client{" +
        "clientId=" + clientId +
        ", name=" + name +
        ", adress=" + adress +
        ", phone=" + phone +
        ", orderId=" + orderId +
        ", orderTime=" + orderTime +
        ", price=" + price +
        ", logisticsId=" + logisticsId +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        ", createUser=" + createUser +
        ", updateUser=" + updateUser +
        ", display=" + display +
        "}";
    }
}
