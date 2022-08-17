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
 *
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-04-14
 */
@TableName("daoxin_erp_instock_log_detail")
public class InstockLogDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 实际数量
     */
    @TableField("real_number")
    private Long realNumber;

    /**
     * 异常id
     */
    @TableField("anomaly_id")
    private Long anomalyId;
    /**
     * 单据id
     */
    @TableField("receipt_id")
    private Long receiptId;

    @TableId(value = "instock_log_detail_id", type = IdType.ID_WORKER)
    private Long instockLogDetailId;

    @TableField("instock_log_id")
    private Long instockLogId;

    @TableField("type")
    private String type;

    @TableField("customer_id")
    private Long customerId;
    /**
     * 实物id
     */
    @TableField("inkind_id")
    private Long inkindId;

    /**
     * sku_id
     */
    @TableField("sku_id")
    private Long skuId;

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
     * 数量
     */
    @TableField("number")
    private Long number;

    /**
     * 入库数量
     */
    @TableField("instock_number")
    private Long instockNumber;

    /**
     * 入库单id
     */
    @TableField("instock_order_id")
    private Long instockOrderId;

    /**
     * 地点id
     */
    @TableField("storehouse_id")
    private Long storehouseId;

    /**
     * 出售价格
     */
    @TableField("source")
    private String source;

    /**
     * 成本价格
     */
    @TableField("source_id")
    private Long sourceId;

    /**
     * 仓库库位id
     */
    @TableField("storehouse_positions_id")
    private Long storehousePositionsId;

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


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    /**
     * 部门编号
     */
    @TableField("deptId")
    private Long deptId;

    public Long getRealNumber() {
        return realNumber;
    }

    public void setRealNumber(Long realNumber) {
        this.realNumber = realNumber;
    }

    public Long getReceiptId() {
        return receiptId;
    }

    public void setReceiptId(Long receiptId) {
        this.receiptId = receiptId;
    }

    public Long getInstockLogDetailId() {
        return instockLogDetailId;
    }

    public void setInstockLogDetailId(Long instockLogDetailId) {
        this.instockLogDetailId = instockLogDetailId;
    }

    public Long getInstockLogId() {
        return instockLogId;
    }

    public void setInstockLogId(Long instockLogId) {
        this.instockLogId = instockLogId;
    }

    public Long getInkindId() {
        return inkindId;
    }

    public void setInkindId(Long inkindId) {
        this.inkindId = inkindId;
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

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public Long getInstockNumber() {
        return instockNumber;
    }

    public void setInstockNumber(Long instockNumber) {
        this.instockNumber = instockNumber;
    }

    public Long getInstockOrderId() {
        return instockOrderId;
    }

    public void setInstockOrderId(Long instockOrderId) {
        this.instockOrderId = instockOrderId;
    }


    public Long getStorehouseId() {
        return storehouseId;
    }

    public void setStorehouseId(Long storehouseId) {
        this.storehouseId = storehouseId;
    }

    public Long getStorehousePositionsId() {
        return storehousePositionsId;
    }

    public void setStorehousePositionsId(Long storehousePositionsId) {
        this.storehousePositionsId = storehousePositionsId;
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

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getAnomalyId() {
        return anomalyId;
    }

    public void setAnomalyId(Long anomalyId) {
        this.anomalyId = anomalyId;
    }

    @Override
    public String toString() {
        return "InstockLogDetail{" +
                "instockLogDetailId=" + instockLogDetailId +
                ", instockLogId=" + instockLogId +
                ", inkindId=" + inkindId +
                ", skuId=" + skuId +
                ", brandId=" + brandId +
                ", itemId=" + itemId +
                ", number=" + number +
                ", instockNumber=" + instockNumber +
                ", instockOrderId=" + instockOrderId +
                ", storehousePositionsId=" + storehousePositionsId +
                ", createTime=" + createTime +
                ", createUser=" + createUser +
                ", updateTime=" + updateTime +
                ", updateUser=" + updateUser +
                ", display=" + display +
                ", deptId=" + deptId +
                "}";
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Long getSourceId() {
        return sourceId;
    }

    public void setSourceId(Long sourceId) {
        this.sourceId = sourceId;
    }
}
