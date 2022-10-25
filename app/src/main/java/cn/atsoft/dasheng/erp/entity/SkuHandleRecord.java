package cn.atsoft.dasheng.erp.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

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
