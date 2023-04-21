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
 * 出库表
 * </p>
 *
 * @author song
 * @since 2021-07-17
 */
@TableName("daoxin_erp_outstock")
public class Outstock implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 出库id
     */
    @TableId(value = "outstock_id", type = IdType.ID_WORKER)
    private Long outstockId;
    /**
     * 部门Id
     */
    @TableField(value = "deptId", fill = FieldFill.INSERT)
    private Long deptId;



    @TableField(value = "tenant_id" , fill = FieldFill.INSERT)
    private Long tenantId;

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }



    /**
     * 仓库id
     */
    @TableField("storehouse_id")
    private Long storehouseId;
    /**
     * skuId
     */
    @TableField("sku_id")
    private Long skuId;

    @TableField("stock_id")
    private Long stockId;


    @TableField("outstock_order_id")
    private Long outstockOrderId;

    public Long getStockItemId() {
        return stockItemId;
    }

    public void setStockItemId(Long stockItemId) {
        this.stockItemId = stockItemId;
    }

    @TableField("stock_item_id")
    private Long stockItemId;
    /**
     * 出库时间
     */
    @TableField("delivery_time")
    private Date deliveryTime;

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
     * 产品id
     */
    @TableField("item_id")
    private Long itemId;

    /**
     * 出库状态
     */
    @TableField("state")
    private Long state;

    /**
     * 发货申请Id
     */
    @TableField("outstock_apply_id")
    private Long outstockApplyId;
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


    public Long getOutstockId() {
        return outstockId;
    }

    public void setOutstockId(Long outstockId) {
        this.outstockId = outstockId;
    }

    public Long getStorehouseId() {
        return storehouseId;
    }

    public void setStorehouseId(Long storehouseId) {
        this.storehouseId = storehouseId;
    }

    public Date getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(Date deliveryTime) {
        this.deliveryTime = deliveryTime;
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

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public Long getState() {
        return state;
    }

    public void setState(Long state) {
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

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public Long getStockId() {
        return stockId;
    }

    public void setStockId(Long stockId) {
        this.stockId = stockId;
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
                "deliveryId=" + outstockId +
                ", storehouseId=" + storehouseId +
                ", delivery time=" + deliveryTime +
                ", number=" + number +
                ", price=" + price +
                ", createTime=" + createTime +
                ", createUser=" + createUser +
                ", updateTime=" + updateTime +
                ", updateUser=" + updateUser +
                ", display=" + display +
                ", brandId=" + brandId +
                ", state=" + state +
                ", itemId=" + itemId +
                "}";
    }
}
