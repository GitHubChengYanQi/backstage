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
 *
 * </p>
 *
 * @author
 * @since 2021-09-07
 */
@TableName("daoxin_crm_competitor")
public class Competitor implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 竞争对手id
     */
    @TableId(value = "competitor_id", type = IdType.ID_WORKER)
    private Long competitorId;
    @TableField("adress")
    private String adress;


    /**
     * 报价id
     */
    @TableField("competitors_quote_id")
    private Long competitorsQuoteId;

    /**
     * 竞争对手企业名称
     */
    @TableField("name")
    private String name;

    /**
     * 联系电话
     */
    @TableField("phone")
    private Long phone;

    /**
     * 网址
     */
    @TableField("url")
    private String url;

    /**
     * 创立日期
     */
    @TableField("creation_date")
    private Date creationDate;

    /**
     * 邮箱
     */
    @TableField("email")
    private String email;

    /**
     * 员工规模
     */
    @TableField("staff_size")
    private Long staffSize;

    /**
     * 公司所有制
     */
    @TableField("ownership")
    private Integer ownership;

    /**
     * 地区
     */
    @TableField("region")
    private String region;

    /**
     * 竞争级别
     */
    @TableField("competition_level")
    private Integer competitionLevel;

    /**
     * 年销售
     */
    @TableField("annual_sales")
    private Integer annualSales;

    /**
     * 公司简介
     */
    @TableField("company_profile")
    private String companyProfile;

    /**
     * 对手优势
     */
    @TableField("rival_advantage")
    private String rivalAdvantage;

    /**
     * 对手劣势
     */
    @TableField("opponents_weaknesses")
    private String opponentsWeaknesses;

    /**
     * 采取对策
     */
    @TableField("take_countermeasures")
    private String takeCountermeasures;

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
     * 部门编号
     */
    @TableField(value = "deptId",fill =FieldFill.INSERT)
    private Long deptId;

    @TableField(value = "tenant_id" , fill = FieldFill.INSERT)
    private Long tenantId;

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }


    /**
     * 状态
     */
    @TableField("display")
    private Integer display;

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public Long getCompetitorId() {
        return competitorId;
    }

    public void setCompetitorId(Long competitorId) {
        this.competitorId = competitorId;
    }

    public Long getCompetitorsQuoteId() {
        return competitorsQuoteId;
    }

    public void setCompetitorsQuoteId(Long competitorsQuoteId) {
        this.competitorsQuoteId = competitorsQuoteId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPhone() {
        return phone;
    }

    public void setPhone(Long phone) {
        this.phone = phone;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getStaffSize() {
        return staffSize;
    }

    public void setStaffSize(Long staffSize) {
        this.staffSize = staffSize;
    }

    public Integer getOwnership() {
        return ownership;
    }

    public void setOwnership(Integer ownership) {
        this.ownership = ownership;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public Integer getCompetitionLevel() {
        return competitionLevel;
    }

    public void setCompetitionLevel(Integer competitionLevel) {
        this.competitionLevel = competitionLevel;
    }

    public Integer getAnnualSales() {
        return annualSales;
    }

    public void setAnnualSales(Integer annualSales) {
        this.annualSales = annualSales;
    }

    public String getCompanyProfile() {
        return companyProfile;
    }

    public void setCompanyProfile(String companyProfile) {
        this.companyProfile = companyProfile;
    }

    public String getRivalAdvantage() {
        return rivalAdvantage;
    }

    public void setRivalAdvantage(String rivalAdvantage) {
        this.rivalAdvantage = rivalAdvantage;
    }

    public String getOpponentsWeaknesses() {
        return opponentsWeaknesses;
    }

    public void setOpponentsWeaknesses(String opponentsWeaknesses) {
        this.opponentsWeaknesses = opponentsWeaknesses;
    }

    public String getTakeCountermeasures() {
        return takeCountermeasures;
    }

    public void setTakeCountermeasures(String takeCountermeasures) {
        this.takeCountermeasures = takeCountermeasures;
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

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }


    @Override
    public String toString() {
        return "Competitor{" +
                "competitorId=" + competitorId +
                ", competitorsQuoteId=" + competitorsQuoteId +
                ", name=" + name +
                ", phone=" + phone +
                ", url=" + url +
                ", creationDate=" + creationDate +
                ", email=" + email +
                ", staffSize=" + staffSize +
                ", ownership=" + ownership +
                ", region=" + region +
                ", competitionLevel=" + competitionLevel +
                ", annualSales=" + annualSales +
                ", companyProfile=" + companyProfile +
                ", rivalAdvantage=" + rivalAdvantage +
                ", opponentsWeaknesses=" + opponentsWeaknesses +
                ", takeCountermeasures=" + takeCountermeasures +
                ", createUser=" + createUser +
                ", updateUser=" + updateUser +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", display=" + display +
                "}";
    }
}
