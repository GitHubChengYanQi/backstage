package cn.atsoft.dasheng.app.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.models.auth.In;

import java.io.Serializable;

/**
 * <p>
 * 仓库物品明细表
 * </p>
 *
 * @author
 * @since 2021-07-15
 */
@TableName("daoxin_erp_stock_details")
public class StockDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField(value = "deptId", fill = FieldFill.INSERT)
    private Long deptId;

    @TableField(exist = false)
    private Integer num; //用来分组查询求相同sku总库存数量

    /**
     * 供应商
     */
    @TableField("customer_id")
    private Long customerId;

    /**
     * 实物id
     */
    @TableField("inkind_id")
    private Long inkindId;

    @TableField("number")
    private Long number;

    @TableField("qr_code_id")
    private Long qrCodeId;

    @TableField("outstock_order_id")
    private Long outStockOrderId;

    @TableField("stage")
    private Integer stage;
    /**
     * 库位id
     */
    @TableField("storehouse_positions_id")
    private Long storehousePositionsId;

    @TableField("storehouse_id")
    private Long storehouseId;


    /**
     * skuId
     */
    @TableField("sku_id")
    private Long skuId;

    /**
     * 明细id
     */
    @TableId(value = "stock_item_id", type = IdType.AUTO)
    private Long stockItemId;

    /**
     * 仓库id
     */
    @TableField("stock_id")
    private Long stockId;

    /**
     * 价格
     */
    @TableField("price")
    private Integer price;

    /**
     * 品牌id
     */
    @TableField("item_id")
    private Long itemId;

    /**
     * 入库时间
     */
    @TableField("storage_time")
    private Date storageTime;

    /**
     * 品牌id
     */
    @TableField("brand_id")
    private Long brandId;

    /**
     * 条形码
     */
    @TableField("barcode")
    private Long barcode;

    /**
     * 创建者
     */
    @TableField(value = "create_user", fill = FieldFill.INSERT)
    private Long createUser;

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

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public Long getQrCodeId() {
        return qrCodeId;
    }

    public void setQrCodeId(Long qrCodeId) {
        this.qrCodeId = qrCodeId;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getStorehouseId() {
        return storehouseId;
    }

    public void setStorehouseId(Long storehouseId) {
        this.storehouseId = storehouseId;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemsId) {
        this.itemId = itemsId;
    }

    public Long getStockItemId() {
        return stockItemId;
    }

    public void setStockItemId(Long stockItemId) {
        this.stockItemId = stockItemId;
    }

    public Long getStockId() {
        return stockId;
    }

    public void setStockId(Long stockId) {
        this.stockId = stockId;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Date getStorageTime() {
        return storageTime;
    }

    public void setStorageTime(Date storageTime) {
        this.storageTime = storageTime;
    }

    public Long getCreateUser() {
        return createUser;
    }

    public void setCreateUser(Long createUser) {
        this.createUser = createUser;
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

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public Long getBarcode() {
        return barcode;
    }

    public void setBarcode(Long barcode) {
        this.barcode = barcode;
    }

    public Integer getStage() {
        return stage;
    }

    public void setStage(Integer stage) {
        this.stage = stage;
    }

    public Long getOutStockOrderId() {
        return outStockOrderId;
    }

    public void setOutStockOrderId(Long outStockOrderId) {
        this.outStockOrderId = outStockOrderId;
    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public Long getStorehousePositionsId() {
        return storehousePositionsId;
    }

    public void setStorehousePositionsId(Long storehousePositionsId) {
        this.storehousePositionsId = storehousePositionsId;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public Long getInkindId() {
        return inkindId;
    }

    public void setInkindId(Long inkindId) {
        this.inkindId = inkindId;
    }


    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    @Override
    public String toString() {
        return "StockDetails{" +
                "stockItemId=" + stockItemId +
                ", stockId=" + stockId +
                ", price=" + price +
                ", storageTime=" + storageTime +
                ", createUser=" + createUser +
                ", createTime=" + createTime +
                ", updateUser=" + updateUser +
                ", updateTime=" + updateTime +
                ", display=" + display +
                ", itemId=" + itemId +
                ", placeId=" + storehouseId +
                ", brandId=" + brandId +
                ", barcode=" + barcode +
                "}";
    }
}
