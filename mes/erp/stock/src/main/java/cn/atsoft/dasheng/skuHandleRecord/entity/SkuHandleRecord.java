package cn.atsoft.dasheng.skuHandleRecord.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * sku 任务操作记录
 * </p>
 *
 * @author
 * @since 2022-10-25
 */
@TableName("sku_handle_record")
public class SkuHandleRecord implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 任务id
     */
    @TableField("task_id")
    private Long taskId;

    /**
     * 单据Id
     */
    @TableField("receipt_id")
    private Long receiptId;
    /**
     * 客户id
     */
    @TableField("customer_id")
    private Long customerId;
    /**
     * 主键
     */
    @TableId(value = "record_id", type = IdType.ID_WORKER)
    private Long recordId;

    /**
     * 来源
     */
    @TableField("source")
    private String source;

    /**
     * 来源的任务单据
     */
    @TableField("source_id")
    private Long sourceId;

    /**
     * 当时库存数
     */
    @TableField("now_stock_number")
    private Long nowStockNumber;

    /**
     * 操作数量
     */
    @TableField("operation_number")
    private Long operationNumber;

    /**
     * 结余数量
     */
    @TableField("balance_number")
    private Long balanceNumber;

    @TableField("sku_id")
    private Long skuId;

    /**
     * 品牌
     */
    @TableField("brand_id")
    private Long brandId;

    /**
     * 库位id
     */
    @TableField("position_id")
    private Long positionId;

    /**
     * 主题
     */
    @TableField("theme")
    private String theme;

    /**
     * 处理时间
     */
    @TableField("operation_time")
    private Date operationTime;

    /**
     * 操作人
     */
    @TableField("operation_user_id")
    private Long operationUserId;

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


    /**
     * 部门id
     */
    @TableField(value = "deptId", fill = FieldFill.INSERT)
    private Long deptId;


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

    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
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

    public Long getNowStockNumber() {
        return nowStockNumber;
    }

    public void setNowStockNumber(Long nowStockNumber) {
        this.nowStockNumber = nowStockNumber;
    }

    public Long getOperationNumber() {
        return operationNumber;
    }

    public void setOperationNumber(Long operationNumber) {
        this.operationNumber = operationNumber;
    }

    public Long getBalanceNumber() {
        return balanceNumber;
    }

    public void setBalanceNumber(Long balanceNumber) {
        this.balanceNumber = balanceNumber;
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

    public Long getPositionId() {
        return positionId;
    }

    public void setPositionId(Long positionId) {
        this.positionId = positionId;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public Date getOperationTime() {
        return operationTime;
    }

    public void setOperationTime(Date operationTime) {
        this.operationTime = operationTime;
    }

    public Long getOperationUserId() {
        return operationUserId;
    }

    public void setOperationUserId(Long operationUserId) {
        this.operationUserId = operationUserId;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Long getReceiptId() {
        return receiptId;
    }

    public void setReceiptId(Long receiptId) {
        this.receiptId = receiptId;
    }

    @Override
    public String toString() {
        return "SkuHandleRecord{" +
                "recordId=" + recordId +
                ", source=" + source +
                ", sourceId=" + sourceId +
                ", nowStockNumber=" + nowStockNumber +
                ", operationNumber=" + operationNumber +
                ", balanceNumber=" + balanceNumber +
                ", skuId=" + skuId +
                ", brandId=" + brandId +
                ", positionId=" + positionId +
                ", theme=" + theme +
                ", operationTime=" + operationTime +
                ", operationUserId=" + operationUserId +
                "}";
    }
}
