package cn.atsoft.dasheng.erp.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;

/**
 * <p>
 * 出库清单
 * </p>
 *
 * @author cheng
 * @since 2021-09-15
 */
@TableName("daoxin_erp_outstock_listing")
public class OutstockListing implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 出库清单id
     */
    @TableId(value = "outstock_listing_id", type = IdType.ID_WORKER)
    private Long outstockListingId;
    @TableField("inkind_ids")

    private String inkindIds;

    public String getInkindIds() {
        return inkindIds;
    }

    public void setInkindIds(String inkindIds) {
        this.inkindIds = inkindIds;
    }

    /**
     * 供应商id
     */
    @TableField("customer_id")
    private Long customerId;
    /**
     * 出库数量
     */
    @TableField("delivery")
    private Long delivery;

    /**
     * skuId
     */
    @TableField("sku_id")
    private Long skuId;

    @TableField("delivery_id")
    private Long deliveryId;
    /**
     * 出库时间
     */
    @TableField("time")
    private Date time;

    /**
     * 出库数量
     */
    @TableField("number")
    private Long number;

    /**
     * 出库价格
     */
    @TableField("price")
    private Integer price;

    /**
     * 品牌id
     */
    @TableField("brand_id")
    private Long brandId;

    /**
     * 部门编号
     */
    @TableField(value = "deptId", fill = FieldFill.INSERT)
    private Long deptId;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 创建者
     */
    @TableField(value = "create_user", fill = FieldFill.INSERT)
    private Long createUser;

    /**
     * 修改时间
     */
    @TableField(value = "update_time", fill = FieldFill.UPDATE)
    private Date updateTime;

    /**
     * 修改者
     */
    @TableField(value = "update_user", fill = FieldFill.UPDATE)
    private Long updateUser;

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    /**
     * 状态
     */
    @TableField("display")
    private Integer display;

    /**
     * 产品id
     */
    @TableField("item_id")
    private Long itemId;

    /**
     * 出库状态
     */
    @TableField("state")
    private Boolean state;

    /**
     * 出库单号
     */
    @TableField("outstock_order_id")
    private Long outstockOrderId;

    /**
     * 发货申请
     */
    @TableField("outstock_apply_id")
    private Long outstockApplyId;

    public Long getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(Long deliveryId) {
        this.deliveryId = deliveryId;
    }

    public Long getOutstockListingId() {
        return outstockListingId;
    }

    public void setOutstockListingId(Long outstockListingId) {
        this.outstockListingId = outstockListingId;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
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

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public Boolean getState() {
        return state;
    }

    public void setState(Boolean state) {
        this.state = state;
    }

    public Long getOutstockOrderId() {
        return outstockOrderId;
    }

    public void setOutstockOrderId(Long outstockOrderId) {
        this.outstockOrderId = outstockOrderId;
    }

    public Long getOutstockApplyId() {
        return outstockApplyId;
    }

    public void setOutstockApplyId(Long outstockApplyId) {
        this.outstockApplyId = outstockApplyId;
    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public Long getDelivery() {
        return delivery;
    }

    public void setDelivery(Long delivery) {
        this.delivery = delivery;
    }

    @Override
    public String toString() {
        return "OutstockListing{" +
                "outstockListingId=" + outstockListingId +
                ", time=" + time +
                ", number=" + number +
                ", price=" + price +
                ", brandId=" + brandId +
                ", deptId=" + deptId +
                ", createTime=" + createTime +
                ", createUser=" + createUser +
                ", updateTime=" + updateTime +
                ", updateUser=" + updateUser +
                ", display=" + display +
                ", itemId=" + itemId +
                ", state=" + state +
                ", outstockOrderId=" + outstockOrderId +
                ", outstockApplyId=" + outstockApplyId +
                "}";
    }
}
