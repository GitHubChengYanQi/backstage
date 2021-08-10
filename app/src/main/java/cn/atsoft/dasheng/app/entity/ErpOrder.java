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
 * @since 2021-07-26
 */
@TableName("daoxin_erp_order")
public class ErpOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 订单编号
     */
      @TableId(value = "order_id", type = IdType.ID_WORKER)
    private Long orderId;

    /**
     * 地址id
     */
    @TableField("adress_id")
    private String adressId;

    /**
     * 出库id
     */
    @TableField("outstock_id")
    private Long outstockId;

    /**
     * 联系id
     */
    @TableField("contacts_id")
    private Long contactsId;

    /**
     * 联系人电话
     */
    @TableField("phone")
    private String phone;

    /**
     * 物品id
     */
    @TableField("item_id")
    private Long itemId;

    /**
     * 订单状态
     */
    @TableField("state")
    private String state;

    /**
     * 订单数量
     */
    @TableField("number")
    private Long number;

    /**
     * 下单时间
     */
    @TableField("order_time")
    private Date orderTime;

    /**
     * 部门编号
     */
    @TableField("deptId")
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

  public Long getOrderId() {
    return orderId;
  }

  public void setOrderId(Long orderId) {
    this.orderId = orderId;
  }

  public String getAdressId() {
    return adressId;
  }

  public void setAdressId(String adressId) {
    this.adressId = adressId;
  }

  public Long getOutstockId() {
    return outstockId;
  }

  public void setOutstockId(Long outstockId) {
    this.outstockId = outstockId;
  }

  public Long getContactsId() {
    return contactsId;
  }

  public void setContactsId(Long contactsId) {
    this.contactsId = contactsId;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public Long getItemId() {
    return itemId;
  }

  public void setItemId(Long itemId) {
    this.itemId = itemId;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public Long getNumber() {
    return number;
  }

  public void setNumber(Long number) {
    this.number = number;
  }

  public Date getOrderTime() {
    return orderTime;
  }

  public void setOrderTime(Date orderTime) {
    this.orderTime = orderTime;
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
    return "ErpOrder{" +
      "orderId=" + orderId +
      ", adressId='" + adressId + '\'' +
      ", outstockId=" + outstockId +
      ", contactsId=" + contactsId +
      ", itemId=" + itemId +
      ", state='" + state + '\'' +
      ", number=" + number +
      ", orderTime=" + orderTime +
      ", deptId=" + deptId +
      ", createUser=" + createUser +
      ", updateUser=" + updateUser +
      ", createTime=" + createTime +
      ", updateTime=" + updateTime +
      ", display=" + display +
      '}';
  }
}
