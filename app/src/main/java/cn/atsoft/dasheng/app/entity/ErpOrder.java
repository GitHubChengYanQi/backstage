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
     * 合同id
     */
    @TableField("contract_id")
    private Long contractId;
    /**
     * 合同名称
     */
    @TableField("contract_name")
    private String contractName;

    /**
     * 甲方id
     */
    @TableField("party_a")
    private Long partyA;

    /**
     * 乙方id
     */
    @TableField("party_b")
    private Long partyB;

    /**
     * 甲方联系人id
     */
    @TableField("party_a_contacts_id")
    private Long partyAContactsId;

    /**
     * 乙方联系人id
     */
    @TableField("party_b_contacts_id")
    private Long partyBContactsId;

    /**
     * 甲方联系地址
     */
    @TableField("party_a_adress_id")
    private Long partyAAdressId;

    /**
     * 乙方联系地址
     */
    @TableField("party_b_adress_id")
    private Long partyBAdressId;


    /**
     * 甲方联系人电话
     */
    @TableField("party_a_phone")
    private Long partyAPhone;

    /**
     * 乙方联系人电话
     */
    @TableField("party_b_phone")
    private Long partyBPhone;

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
     * 订单金额
     */
    @TableField("price")
    private Long price;

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

    public Long getContractId() {
        return contractId;
    }

    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    public Long getPartyA() {
        return partyA;
    }

    public void setPartyA(Long partyA) {
        this.partyA = partyA;
    }

    public Long getPartyB() {
        return partyB;
    }

    public void setPartyB(Long partyB) {
        this.partyB = partyB;
    }

    public Long getPartyAContactsId() {
        return partyAContactsId;
    }

    public void setPartyAContactsId(Long partyAContactsId) {
        this.partyAContactsId = partyAContactsId;
    }

    public Long getPartyBContactsId() {
        return partyBContactsId;
    }

    public void setPartyBContactsId(Long partyBContactsId) {
        this.partyBContactsId = partyBContactsId;
    }

    public Long getPartyAAdressId() {
        return partyAAdressId;
    }

    public void setPartyAAdressId(Long partyAAdressId) {
        this.partyAAdressId = partyAAdressId;
    }

    public Long getPartyBAdressId() {
        return partyBAdressId;
    }

    public void setPartyBAdressId(Long partyBAdressId) {
        this.partyBAdressId = partyBAdressId;
    }

    public Long getPartyAPhone() {
        return partyAPhone;
    }

    public void setPartyAPhone(Long partyAPhone) {
        this.partyAPhone = partyAPhone;
    }

    public Long getPartyBPhone() {
        return partyBPhone;
    }

    public void setPartyBPhone(Long partyBPhone) {
        this.partyBPhone = partyBPhone;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
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
                ", contractId='" + contractId + '\'' +
                ", contractName=" + contractName +
                ", partyA=" + partyA +
                ", partyB=" + partyB +
                ", partyAContactsId=" + partyAContactsId +
                ", partyBContactsId=" + partyBContactsId +
                ", partyAAdressId=" + partyAAdressId +
                ", partyBAdressId=" + partyBAdressId +
                ", partyAPhone=" + partyAPhone +
                ", partyBPhone=" + partyBPhone +
                ", price=" + price +
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
