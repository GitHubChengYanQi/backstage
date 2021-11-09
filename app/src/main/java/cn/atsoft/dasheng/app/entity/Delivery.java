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
 * @since 2021-08-20
 */
@TableName("daoxin_erp_delivery")
public class Delivery implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * skuId
     */
    @TableField("sku_id")
    private Long skuId;
    /**
     * 车牌号
     */
    @TableField("license_plate")
    private String licensePlate;
    /**
     * 司机电话
     */
    @TableField("driver_phone")
    private Long driverPhone;
    /**
     * 司机姓名
     */
    @TableField("driver_name")
    private String driverName;
    /**
     * 物流单号
     */
    @TableField("logistics_number")
    private String logisticsNumber;
    /**
     * 物流公司
     */
    @TableField("logistics_company")
    private String logisticsCompany;
    /**
     * 发货方式
     */
    @TableField("delivery_way")
    private Integer deliveryWay;

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
     * 发货id
     */
    @TableId(value = "delivery_id", type = IdType.ID_WORKER)
    private Long deliveryId;

    /**
     * 出库单id
     */
    @TableField("outstock_order_id")
    private Long outstockOrderId;

    /**
     * 发货时间
     */
    @TableField("out_time")
    private Date outTime;

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

    @TableField(value = "deptId", fill = FieldFill.INSERT_UPDATE)
    private Long deptId;

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public Long getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(Long deliveryId) {
        this.deliveryId = deliveryId;
    }

    public Long getOutstockOrderId() {
        return outstockOrderId;
    }

    public void setOutstockOrderId(Long outstockOrderId) {
        this.outstockOrderId = outstockOrderId;
    }

    public Date getOutTime() {
        return outTime;
    }

    public void setOutTime(Date outTime) {
        this.outTime = outTime;
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

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public Long getDriverPhone() {
        return driverPhone;
    }

    public void setDriverPhone(Long driverPhone) {
        this.driverPhone = driverPhone;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getLogisticsNumber() {
        return logisticsNumber;
    }

    public void setLogisticsNumber(String logisticsNumber) {
        this.logisticsNumber = logisticsNumber;
    }

    public String getLogisticsCompany() {
        return logisticsCompany;
    }

    public void setLogisticsCompany(String logisticsCompany) {
        this.logisticsCompany = logisticsCompany;
    }

    public Integer getDeliveryWay() {
        return deliveryWay;
    }

    public void setDeliveryWay(Integer deliveryWay) {
        this.deliveryWay = deliveryWay;
    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    @Override
    public String toString() {
        return "Delivery{" +
                "deliveryId=" + deliveryId +
                ", outstockOrderId=" + outstockOrderId +
                ", outTime=" + outTime +
                ", createUser=" + createUser +
                ", updateUser=" + updateUser +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", display=" + display +
                "}";
    }
}
