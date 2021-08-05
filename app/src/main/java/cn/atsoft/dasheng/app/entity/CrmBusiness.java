package cn.atsoft.dasheng.app.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;

/**
 * <p>
 * 商机表
 * </p>
 *
 * @author
 * @since 2021-08-03
 */
@TableName("daoxin_crm_business")
public class CrmBusiness implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableField("track_id")
    private Long trackId;
    /**
     * 商机id
     */
    @TableId(value = "business_id", type = IdType.ID_WORKER)
    private Long businessId;
    @TableField("process_id")
    private Long processId;
    /**
     * 商机名称
     */
    @TableField("business_name")
    private String businessName;

    /**
     * 负责人
     */
    @TableField("person")
    private Long person;

    /**
     * 客户id
     */
    @TableField("customer_id")
    private Long customerId;

    public Long getProcessId() {
        return processId;
    }

    public void setProcessId(Long processId) {
        this.processId = processId;
    }

    /**
     * 结单日期
     */
    @TableField("statement_time")
    private Date statementTime;

    /**
     * 阶段变更时间
     */
    @TableField("change_time")
    private Date changeTime;

    /**
     * 商机金额
     */
    @TableField("opportunity_amount")
    private Integer opportunityAmount;

    /**
     * 阶段状态
     */
    @TableField("state")
    private String state;

    /**
     * 销售流程id
     */
    @TableField("sales_id")
    private Long salesId;

    /**
     * 产品合计
     */
    @TableField("total_products")
    private Double totalProducts;

    /**
     * 机会来源
     */
    @TableField("origin_id")
    private Long originId;

    /**
     * 立项日期
     */
    @TableField("time")
    private Date time;

    /**
     * 商机阶段
     */
    @TableField("stage")
    private String stage;

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

    public Long getTrackId() {
        return trackId;
    }

    public void setTrackId(Long trackId) {
        this.trackId = trackId;
    }

    public Long getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Long businessId) {
        this.businessId = businessId;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public Long getPerson() {
        return person;
    }

    public void setPerson(Long person) {
        this.person = person;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Date getStatementTime() {
        return statementTime;
    }

    public void setStatementTime(Date statementTime) {
        this.statementTime = statementTime;
    }

    public Date getChangeTime() {
        return changeTime;
    }

    public void setChangeTime(Date changeTime) {
        this.changeTime = changeTime;
    }

    public Integer getOpportunityAmount() {
        return opportunityAmount;
    }

    public void setOpportunityAmount(Integer opportunityAmount) {
        this.opportunityAmount = opportunityAmount;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Long getSalesId() {
        return salesId;
    }

    public void setSalesId(Long salesId) {
        this.salesId = salesId;
    }

    public Double getTotalProducts() {
        return totalProducts;
    }

    public void setTotalProducts(Double totalProducts) {
        this.totalProducts = totalProducts;
    }

    public Long getOriginId() {
        return originId;
    }

    public void setOriginId(Long originId) {
        this.originId = originId;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
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

    public Integer getDisplay() {
        return display;
    }

    public void setDisplay(Integer display) {
        this.display = display;
    }

    @Override
    public String toString() {
        return "CrmBusiness{" +
                "businessId=" + businessId +
                ", businessName=" + businessName +
                ", person=" + person +
                ", customerId=" + customerId +
                ", statementTime=" + statementTime +
                ", changeTime=" + changeTime +
                ", opportunityAmount=" + opportunityAmount +
                ", state=" + state +
                ", salesId=" + salesId +
                ", totalProducts=" + totalProducts +
                ", originId=" + originId +
                ", time=" + time +
                ", stage=" + stage +
                ", createUser=" + createUser +
                ", updateUser=" + updateUser +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", display=" + display +
                "}";
    }
}