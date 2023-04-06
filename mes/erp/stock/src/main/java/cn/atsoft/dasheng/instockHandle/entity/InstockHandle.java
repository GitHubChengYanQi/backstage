package cn.atsoft.dasheng.instockHandle.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 入库操作结果
 * </p>
 *
 * @author song
 * @since 2022-07-08
 */
@TableName("daoxin_erp_instock_handle")
public class InstockHandle implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("source")
    private String source;

    @TableField("source_id")
    private Long sourceId;

    /**
     * 入库处理
     */
    @TableId(value = "instock_handle_id", type = IdType.ID_WORKER)
    private Long instockHandleId;

    /**
     * 入库清单
     */
    @TableField("instock_list_id")
    private Long instockListId;

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
     * 类型
     */
    @TableField("type")
    private String type;

    /**
     * 供应商id
     */
    @TableField("customer_id")
    private Long customerId;

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
     * 仓库库位id
     */
    @TableField("storehouse_positions_id")
    private Long storehousePositionsId;

    /**
     * 数量
     */
    @TableField("number")
    private Long number;

    /**
     * 实物id
     */
    @TableField("inkind_id")
    private Long inkindId;

    /**
     * 状态
     */
    @TableField("status")
    private Long status;

    /**
     * 入库数量
     */
    @TableField("instock_number")
    private Long instockNumber;

    @TableField("real_number")
    private Long realNumber;

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
    @TableField(value = "deptId" , fill = FieldFill.INSERT)
    private Long deptId;


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getInstockHandleId() {
        return instockHandleId;
    }

    public void setInstockHandleId(Long instockHandleId) {
        this.instockHandleId = instockHandleId;
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

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public Long getInkindId() {
        return inkindId;
    }

    public void setInkindId(Long inkindId) {
        this.inkindId = inkindId;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
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

    public Long getInstockListId() {
        return instockListId;
    }

    public void setInstockListId(Long instockListId) {
        this.instockListId = instockListId;
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

    @Override
    public String toString() {
        return "InstockHandle{" +
                "instockHandleId=" + instockHandleId +
                ", skuId=" + skuId +
                ", brandId=" + brandId +
                ", customerId=" + customerId +
                ", instockOrderId=" + instockOrderId +
                ", storehouseId=" + storehouseId +
                ", storehousePositionsId=" + storehousePositionsId +
                ", number=" + number +
                ", inkindId=" + inkindId +
                ", status=" + status +
                ", instockNumber=" + instockNumber +
                ", realNumber=" + realNumber +
                ", createTime=" + createTime +
                ", createUser=" + createUser +
                ", updateTime=" + updateTime +
                ", updateUser=" + updateUser +
                ", display=" + display +
                ", deptId=" + deptId +
                "}";
    }
}
