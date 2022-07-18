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
 * 库存盘点处理
 * </p>
 *
 * @author song
 * @since 2022-07-15
 */
@TableName("daoxin_erp_inventory_stock")
public class InventoryStock implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long InkindId;
      @TableId(value = "inventory_stock_id", type = IdType.ID_WORKER)
    private Long inventoryStockId;

    /**
     * 盘点主表id
     */
    @TableField("inventory_id")
    private Long inventoryId;

    /**
     * 盘点任务详情id
     */
    @TableField("detail_id")
    private Long detailId;

    @TableField("sku_id")
    private Long skuId;

    @TableField("brand_id")
    private Long brandId;

    @TableField("customer_id")
    private Long customerId;

    /**
     * 库位id
     */
    @TableField("position_id")
    private Long positionId;

    @TableField("anomaly_id")
    private Long anomalyId;

    /**
     * 数量
     */
    @TableField("number")
    private Long number;

    /**
     * 锁
     */
    @TableField("lock_status")
    private Integer lockStatus;

    /**
     * 实际数量
     */
    @TableField("real_number")
    private Long realNumber;

    /**
     * 默认 0
     */
    @TableField("status")
    private Integer status;

      @TableField(value = "create_user", fill = FieldFill.INSERT)
    private Long createUser;

      @TableField(value = "update_user", fill = FieldFill.UPDATE)
    private Long updateUser;

      @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

      @TableField(value = "update_time", fill = FieldFill.UPDATE)
    private Date updateTime;

    /**
     * 部门id
     */
    @TableField("deptId")
    private Long deptId;

    @TableField("display")
    private Integer display;


    public Long getInventoryStockId() {
        return inventoryStockId;
    }

    public void setInventoryStockId(Long inventoryStockId) {
        this.inventoryStockId = inventoryStockId;
    }

    public Long getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(Long inventoryId) {
        this.inventoryId = inventoryId;
    }

    public Long getDetailId() {
        return detailId;
    }

    public void setDetailId(Long detailId) {
        this.detailId = detailId;
    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getPositionId() {
        return positionId;
    }

    public void setPositionId(Long positionId) {
        this.positionId = positionId;
    }

    public Long getAnomalyId() {
        return anomalyId;
    }

    public void setAnomalyId(Long anomalyId) {
        this.anomalyId = anomalyId;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public Integer getLockStatus() {
        return lockStatus;
    }

    public void setLockStatus(Integer lockStatus) {
        this.lockStatus = lockStatus;
    }

    public Long getRealNumber() {
        return realNumber;
    }

    public void setRealNumber(Long realNumber) {
        this.realNumber = realNumber;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    public Long getInkindId() {
        return InkindId;
    }

    public void setInkindId(Long inkindId) {
        InkindId = inkindId;
    }

    @Override
    public String toString() {
        return "InventoryStock{" +
        "inventoryStockId=" + inventoryStockId +
        ", inventoryId=" + inventoryId +
        ", detailId=" + detailId +
        ", skuId=" + skuId +
        ", brandId=" + brandId +
        ", customerId=" + customerId +
        ", positionId=" + positionId +
        ", anomalyId=" + anomalyId +
        ", number=" + number +
        ", lockStatus=" + lockStatus +
        ", realNumber=" + realNumber +
        ", status=" + status +
        ", createUser=" + createUser +
        ", updateUser=" + updateUser +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        ", deptId=" + deptId +
        ", display=" + display +
        "}";
    }
}
