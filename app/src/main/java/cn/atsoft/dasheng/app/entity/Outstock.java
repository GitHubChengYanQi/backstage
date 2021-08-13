package cn.atsoft.dasheng.app.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;

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
     * 库存id
     */
    @TableField("stock_id")
    private Long stockId;

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
     * 出库品牌
     */
    @TableField("brand")
    private Long brand;

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

    public Long getStockId() {
        return stockId;
    }

    public void setStockId(Long stockId) {
        this.stockId = stockId;
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

    public Long getBrand() {
        return brand;
    }

    public void setBrand(Long brand) {
        this.brand = brand;
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
        return "Delivery{" +
        "deliveryId=" + outstockId +
        ", stockId=" + stockId +
        ", delivery time=" + deliveryTime +
        ", number=" + number +
        ", price=" + price +
        ", brand=" + brand +
        ", createTime=" + createTime +
        ", createUser=" + createUser +
        ", updateTime=" + updateTime +
        ", updateUser=" + updateUser +
        ", display=" + display +
        "}";
    }
}
