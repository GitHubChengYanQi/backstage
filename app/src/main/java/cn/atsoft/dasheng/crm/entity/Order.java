package cn.atsoft.dasheng.crm.entity;

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
 * @author song
 * @since 2022-02-23
 */
@TableName("daoxin_crm_order")
public class Order implements Serializable {

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
     * 订单金额
     */
    @TableField("price")
    private Long price;

    /**
     * 买方id
     */
    @TableField("buyer _id")
    private Long buyerId;

    /**
     * 卖方id
     */
    @TableField("seller_id")
    private Long sellerId;

    /**
     * 主题
     */
    @TableField("theme")
    private String theme;

    /**
     * 来源Json字符串
     */
    @TableField("origin")
    private String origin;

    /**
     * 1采购 2销售
     */
    @TableField("type")
    private Integer type;

    /**
     * 是否生成合同
     */
    @TableField("generate_contract")
    private Integer generateContract;

    /**
     * 交货日期
     */
    @TableField("delivery_date")
    private Date deliveryDate;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

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

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }


    public Long getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(Long buyerId) {
        this.buyerId = buyerId;
    }

    public Long getSellerId() {
        return sellerId;
    }

    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getGenerateContract() {
        return generateContract;
    }

    public void setGenerateContract(Integer generateContract) {
        this.generateContract = generateContract;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", contractId=" + contractId +
                ", price=" + price +
                ", sellerId=" + sellerId +
                ", theme=" + theme +
                ", origin=" + origin +
                ", type=" + type +
                ", generateContract=" + generateContract +
                ", deliveryDate=" + deliveryDate +
                ", remark=" + remark +
                ", createUser=" + createUser +
                ", deptId=" + deptId +
                ", display=" + display +
                ", updateTime=" + updateTime +
                ", createTime=" + createTime +
                ", updateUser=" + updateUser +
                "}";
    }
}
