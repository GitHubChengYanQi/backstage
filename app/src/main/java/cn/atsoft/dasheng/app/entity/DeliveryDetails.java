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
 * 
 * </p>
 *
 * @author  
 * @since 2021-08-20
 */
@TableName("daoxin_erp_delivery_details")
public class DeliveryDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 发货详情id
     */
      @TableId(value = "delivery_details_id", type = IdType.ID_WORKER)
    private Long deliveryDetailsId;

    /**
     * 产品id
     */
    @TableField("item_id")
    private Long itemId;

    /**
     * 客户id
     */
    @TableField("customer_id")
    private Long customerId;

    /**
     * 地址id
     */
    @TableField("adress_id")
    private Long adressId;

    /**
     * 联系人id
     */
    @TableField("contacts_id")
    private Long contactsId;

    /**
     * 电话id
     */
    @TableField("phone_id")
    private Long phoneId;

    /**
     * 出库明细id
     */
    @TableField("stock_item_id")
    private Long stockItemId;

    /**
     * 出库单
     */
    @TableField("delivery_id")
    private Long deliveryId;

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


    public Long getDeliveryDetailsId() {
        return deliveryDetailsId;
    }

    public void setDeliveryDetailsId(Long deliveryDetailsId) {
        this.deliveryDetailsId = deliveryDetailsId;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getAdressId() {
        return adressId;
    }

    public void setAdressId(Long adressId) {
        this.adressId = adressId;
    }

    public Long getContactsId() {
        return contactsId;
    }

    public void setContactsId(Long contactsId) {
        this.contactsId = contactsId;
    }

    public Long getPhoneId() {
        return phoneId;
    }

    public void setPhoneId(Long phoneId) {
        this.phoneId = phoneId;
    }

    public Long getStockItemId() {
        return stockItemId;
    }

    public void setStockItemId(Long stockItemId) {
        this.stockItemId = stockItemId;
    }

    public Long getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(Long deliveryId) {
        this.deliveryId = deliveryId;
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
        return "DeliveryDetails{" +
        "deliveryDetailsId=" + deliveryDetailsId +
        ", itemId=" + itemId +
        ", customerId=" + customerId +
        ", adressId=" + adressId +
        ", contactsId=" + contactsId +
        ", phoneId=" + phoneId +
        ", stockItemId=" + stockItemId +
        ", deliveryId=" + deliveryId +
        ", createUser=" + createUser +
        ", updateUser=" + updateUser +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        ", display=" + display +
        "}";
    }
}