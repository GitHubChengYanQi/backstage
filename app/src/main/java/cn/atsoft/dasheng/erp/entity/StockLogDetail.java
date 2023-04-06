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
 * 库存操作记录子表
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-12-22
 */
@TableName("daoxin_erp_stock_log_detail")
public class StockLogDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 库存操作记录详情
     */
      @TableId(value = "stock_log_detail_id", type = IdType.ID_WORKER)
    private Long stockLogDetailId;

    /**
     * 库存操作记录
     */
    @TableField("stock_log_id")
    private Long stockLogId;

    /**
     * 实物id
     */
    @TableField("inkind_id")
    private Long inkindId;

    /**
     * 实物id
     */
    @TableField("sku_id")
    private Long skuId;

    /**
     * 数量(实物角度)
     */
    @TableField("number")
    private Integer number;

    /**
     * 操作之前的库存数量(实物角度)
     */
    @TableField("before_number")
    private Integer beforeNumber;

    /**
     * 操作之后数量(实物角度)
     */
    @TableField("after_number")
    private Integer afterNumber;

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
     * 类型(increase增加/dwindle减少)
     */
    @TableField("type")
    private String type;

    @TableField("source")
    private String source;

    @TableField("source_id")
    private Long sourceId;

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


    public Long getStockLogDetailId() {
        return stockLogDetailId;
    }

    public void setStockLogDetailId(Long stockLogDetailId) {
        this.stockLogDetailId = stockLogDetailId;
    }

    public Long getStockLogId() {
        return stockLogId;
    }

    public void setStockLogId(Long stockLogId) {
        this.stockLogId = stockLogId;
    }

    public Long getInkindId() {
        return inkindId;
    }

    public void setInkindId(Long inkindId) {
        this.inkindId = inkindId;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getBeforeNumber() {
        return beforeNumber;
    }

    public void setBeforeNumber(Integer beforeNumber) {
        this.beforeNumber = beforeNumber;
    }

    public Integer getAfterNumber() {
        return afterNumber;
    }

    public void setAfterNumber(Integer afterNumber) {
        this.afterNumber = afterNumber;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    @Override
    public String toString() {
        return "StockLogDetail{" +
                "stockLogDetailId=" + stockLogDetailId +
                ", stockLogId=" + stockLogId +
                ", inkindId=" + inkindId +
                ", skuId=" + skuId +
                ", number=" + number +
                ", beforeNumber=" + beforeNumber +
                ", afterNumber=" + afterNumber +
                ", storehouseId=" + storehouseId +
                ", storehousePositionsId=" + storehousePositionsId +
                ", type='" + type + '\'' +
                ", source='" + source + '\'' +
                ", sourceId=" + sourceId +
                ", createTime=" + createTime +
                ", createUser=" + createUser +
                ", updateTime=" + updateTime +
                ", updateUser=" + updateUser +
                ", display=" + display +
                ", deptId=" + deptId +
                '}';
    }
}
