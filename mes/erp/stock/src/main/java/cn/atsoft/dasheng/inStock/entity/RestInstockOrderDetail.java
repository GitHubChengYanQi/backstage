package cn.atsoft.dasheng.inStock.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 入库清单
 * </p>
 *
 * @author song
 * @since 2021-10-06
 */
@TableName("daoxin_erp_instock_list")
public class RestInstockOrderDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 异常处理方式
     */
    @TableField("anomaly_handle")
    private String anomalyHandle;
    /**
     * 供应商
     */
    @TableField("customer_id")
    private Long customerId;
    /**
     * skuId
     */
    @TableField("sku_id")
    private Long skuId;
    /**
     * 实际数量
     */
    @TableField("real_number")
    private Long realNumber;

    /**
     * 入库数量
     */
    @TableField("instock_number")
    private Long instockNumber;


    /**
     * 到货时间
     */
    @TableField("received_date")
    private Date receivedDate;

    /**
     * 有效日期
     */
    @TableField("effective_date")
    private Date effectiveDate;

    /**
     * 生产日期
     */
    @TableField("manufacture_date")
    private Date manufactureDate;

    /**
     * 批号
     */
    @TableField("lot_number")
    private String lotNumber;

    /**
     * 流水号
     */
    @TableField("serial_number")
    private String serialNumber;


    /**
     * 库位id
     */
    @TableField("storehouse_positions_id")
    private Long storehousePositionsId;

    /**
     * 入库清单
     */
    @TableId(value = "instock_list_id", type = IdType.ID_WORKER)
    private Long instockListId;

    /**
     * 价格
     */
    @TableField("cost_price")
    private Integer costPrice;

    @TableField("selling_price")
    private Integer sellingPrice;


    /**
     * 仓库id
     */
    @TableField("storehouse_id")
    private Long storeHouseId;

    @TableField("inkind_id")
    private Long inkindId;

    public Long getInkindId() {
        return inkindId;
    }

    public void setInkindId(Long inkindId) {
        this.inkindId = inkindId;
    }

    /**
     * 品牌id
     */
    @TableField("brand_id")
    private Long brandId;


    @TableField("status")
    private Long status;

    /**
     * 数量
     */
    @TableField("number")
    private Long number;

    /**
     * 入库单id
     */
    @TableField("instock_order_id")
    private Long instockOrderId;

    /**
     * 订单id
     */
    @TableField("detail_id")
    private Long detailId;

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

    /**
     * 部门编号
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
    public Long getDetailId() {
        return detailId;
    }

    public void setDetailId(Long detailId) {
        this.detailId = detailId;
    }

    public String getAnomalyHandle() {
        return anomalyHandle;
    }

    public void setAnomalyHandle(String anomalyHandle) {
        this.anomalyHandle = anomalyHandle;
    }

    public Integer getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(Integer costPrice) {
        this.costPrice = costPrice;
    }

    public Integer getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(Integer sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public Long getInstockListId() {
        return instockListId;
    }

    public void setInstockListId(Long instockListId) {
        this.instockListId = instockListId;
    }

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public Long getInstockOrderId() {
        return instockOrderId;
    }

    public void setInstockOrderId(Long instockOrderId) {
        this.instockOrderId = instockOrderId;
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

    public Long getStoreHouseId() {
        return storeHouseId;
    }

    public void setStoreHouseId(Long storeHouseId) {
        this.storeHouseId = storeHouseId;
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

    public Date getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(Date receivedDate) {
        this.receivedDate = receivedDate;
    }

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public Date getManufactureDate() {
        return manufactureDate;
    }

    public void setManufactureDate(Date manufactureDate) {
        this.manufactureDate = manufactureDate;
    }

    public String getLotNumber() {
        return lotNumber;
    }

    public void setLotNumber(String lotNumber) {
        this.lotNumber = lotNumber;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }


    public Long getInstockNumber() {
        return instockNumber;
    }

    public void setInstockNumber(Long instockNumber) {
        this.instockNumber = instockNumber;
    }

    public Long getRealNumber() {
        return realNumber;
    }

    public void setRealNumber(Long realNumber) {
        this.realNumber = realNumber;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    @Override
    public String toString() {
        return "InstockList{" +
                "instockListId=" + instockListId +
                ", brandId=" + brandId +
                ", number=" + number +
                ", instockOrderId=" + instockOrderId +
                ", createTime=" + createTime +
                ", createUser=" + createUser +
                ", updateTime=" + updateTime +
                ", updateUser=" + updateUser +
                ", display=" + display +
                ", deptId=" + deptId +
                "}";
    }
}
