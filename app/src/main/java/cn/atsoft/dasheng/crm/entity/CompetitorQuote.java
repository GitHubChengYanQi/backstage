package cn.atsoft.dasheng.crm.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 竞争对手报价
 * </p>
 *
 * @author 
 * @since 2021-09-07
 */
@TableName("daoxin_crm_competitor_quote")
public class CompetitorQuote implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 报价id
     */
      @TableId(value = "quote_id", type = IdType.ID_WORKER)
    private Long quoteId;
    /**
     * 商机id
     */
    @TableField("business_id")
    private Long businessId;

    /**
     * 竞争对手id
     */
    @TableField("competitor_id")
    private Long competitorId;
    /**
     * 是否我的报价
     */
    @TableField("camp_type")
    private  Long campType;
    /**
     * 报价金额
     */
    @TableField("competitors_quote")
    private Integer competitorsQuote;

    /**
     * 报价状态
     */
    @TableField("quote_status")
    private String quoteStatus;

    /**
     * 报价分类
     */
    @TableField("quote_type")
    private String quoteType;

    /**
     * 报价日期
     */
    @TableField("quote_date")
    private Date quoteDate;

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
    @TableField(value = "deptId",fill =FieldFill.INSERT)
    private Long deptId;

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }
    @TableField(value = "tenant_id" , fill = FieldFill.INSERT)
    private Long tenantId;

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }



    public Long getQuoteId() {
        return quoteId;
    }

    public void setQuoteId(Long quoteId) {
        this.quoteId = quoteId;
    }

    public Long getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Long businessid) {
        this.businessId = businessid;
    }

    public Long getCompetitorId() {
        return competitorId;
    }

    public void setCompetitorId(Long competitorId) {
        this.competitorId = competitorId;
    }

    public Long getCampType() {
        return campType;
    }

    public void setCampType(Long campType) {
        this.campType = campType;
    }

    public Integer getCompetitorsQuote() {
        return competitorsQuote;
    }

    public void setCompetitorsQuote(Integer competitorsQuote) {
        this.competitorsQuote = competitorsQuote;
    }

    public String getQuoteStatus() {
        return quoteStatus;
    }

    public void setQuoteStatus(String quoteStatus) {
        this.quoteStatus = quoteStatus;
    }

    public String getQuoteType() {
        return quoteType;
    }

    public void setQuoteType(String quoteType) {
        this.quoteType = quoteType;
    }

    public Date getQuoteDate() {
        return quoteDate;
    }

    public void setQuoteDate(Date quoteDate) {
        this.quoteDate = quoteDate;
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
        return "CompetitorQuote{" +
        "quoteId=" + quoteId +
        ", businessId=" + businessId +
        ", competitorId=" + competitorId +
        ", competitorsQuote=" + competitorsQuote +
        ", quoteStatus=" + quoteStatus +
        ", quoteType=" + quoteType +
        ", quoteDate=" + quoteDate +
        ", createUser=" + createUser +
        ", updateUser=" + updateUser +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        ", display=" + display +
        "}";
    }
}
