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
 * @author song
 * @since 2022-05-27
 */
@TableName("daoxin_anomaly")
public class Anomaly implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("customer_json")
    private String customerJson;

    @TableField("check_number")
    private String checkNumber;

    @TableField("position_id")
    private Long positionId;

    /**
     * 可入库数量
     */
    @TableField("instock_number")
    private Long instockNumber;

    @TableField("order_id")
    private Long orderId;
    /**
     * 供应商id
     */
    @TableField("customer_id")
    private Long customerId;
    /**
     * 品牌
     */
    @TableField("brand_id")
    private Long brandId;

    @TableId(value = "anomaly_id", type = IdType.ID_WORKER)
    private Long anomalyId;

    @TableField("type")
    private String type;

    @TableField("form_id")
    private Long formId;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

    /**
     * 附件
     */
    @TableField("enclosure")
    private String enclosure;

    /**
     * 主题
     */
    @TableField("theme")
    private String theme;

    /**
     * 来源Json字符串
     */
    @TableField("origin")
    private String origin;

    /**
     * 来源
     */
    @TableField("source")
    private String source;

    /**
     * 来源id
     */
    @TableField("source_id")
    private Long sourceId;

    /**
     * 物料
     */
    @TableField("sku_id")
    private Long skuId;

    /**
     * 需要数量
     */
    @TableField("need_number")
    private Long needNumber;

    /**
     * 实际数量
     */
    @TableField("real_number")
    private Long realNumber;

    /**
     * 原因说明
     */
    @TableField("reason")
    private String reason;

    /**
     * 可以入库
     */
    @TableField("status")
    private Integer status;

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
    /**
     * 租户编号
     */
    @TableField(value = "tenant_id" , fill = FieldFill.INSERT)
    private Long tenantId;

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Long getPositionId() {
        return positionId;
    }

    public void setPositionId(Long positionId) {
        this.positionId = positionId;
    }

    public String getCheckNumber() {
        return checkNumber;
    }

    public void setCheckNumber(String checkNumber) {
        this.checkNumber = checkNumber;
    }

    public Long getInstockNumber() {
        return instockNumber;
    }

    public void setInstockNumber(Long instockNumber) {
        this.instockNumber = instockNumber;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public Long getAnomalyId() {
        return anomalyId;
    }

    public void setAnomalyId(Long anomalyId) {
        this.anomalyId = anomalyId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getFormId() {
        return formId;
    }

    public void setFormId(Long formId) {
        this.formId = formId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getEnclosure() {
        return enclosure;
    }

    public void setEnclosure(String enclosure) {
        this.enclosure = enclosure;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
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

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public Long getNeedNumber() {
        return needNumber;
    }

    public void setNeedNumber(Long needNumber) {
        this.needNumber = needNumber;
    }

    public Long getRealNumber() {
        return realNumber;
    }

    public void setRealNumber(Long realNumber) {
        this.realNumber = realNumber;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getCustomerJson() {
        return customerJson;
    }

    public void setCustomerJson(String customerJson) {
        this.customerJson = customerJson;
    }

    @Override
    public String toString() {
        return "Anomaly{" +
                "anomalyId=" + anomalyId +
                ", type=" + type +
                ", formId=" + formId +
                ", remark=" + remark +
                ", enclosure=" + enclosure +
                ", theme=" + theme +
                ", origin=" + origin +
                ", source=" + source +
                ", sourceId=" + sourceId +
                ", skuId=" + skuId +
                ", needNumber=" + needNumber +
                ", realNumber=" + realNumber +
                ", reason=" + reason +
                ", status=" + status +
                ", createTime=" + createTime +
                ", createUser=" + createUser +
                ", updateTime=" + updateTime +
                ", updateUser=" + updateUser +
                ", display=" + display +
                ", deptId=" + deptId +
                "}";
    }
}
