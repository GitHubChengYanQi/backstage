package cn.atsoft.dasheng.crm.entity.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 客户管理表
 * </p>
 *
 * @author
 * @since 2021-07-23
 */
@TableName("daoxin_crm_customer")
public class CustomerExcelItem implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 客户id
     */
    private Long customerId;


    @TableField("agent")
    private Integer agent;


    @TableField("classification")
    private Integer classification;

    @TableField("sort")
    private Long sort;

    @TableField("region")
    private String region;

    /**
     * 客户名称
     */
    @TableField("customer_name")
    @Excel(name = "客户名称")
    private String customerName;


    /**
     * 客户地址id
     */
    @TableField("customer_level_id")
    private Long customerLevelId;
    @TableField("status")
    private Integer status;
    @TableField("origin_id")
    private Long originId;
    @TableField("note")
    private String note;
    @TableField("user_id")
    private Long userId;
    @TableField("emall")
    private String emall;
    @TableField("url")
    private String url;
    @TableField("industry_id")
    private Long industryId;
    /**
     * 法定代表人
     */
    @TableField("legal")
    @Excel(name = "法定代表人")

    private String legal;

    /**
     * 统一社会信用代码
     */
    @TableField("utscc")
    @Excel(name = "统一社会信用代码")

    private String utscc;

    /**
     * 公司类型
     */
    @TableField("company_type")
    private String companyType;

    /**
     * 营业期限
     */
    @TableField("business_term")
    @Excel(name = "营业期限")

    private Date businessTerm;

    /**
     * 注册地址
     */
    @TableField("sign_in")
    @Excel(name = "注册地址")

    private String signIn;

    /**
     * 简介
     */
    @TableField("introduction")
    @Excel(name = "简介")

    private String introduction;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @TableField("avatar")
    private String avatar;

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

//    private String classificationName;
//
//    public String getClassificationName() {
//        return classificationName;
//    }
//
//    public void setClassificationName(String classificationName) {
//        this.classificationName = classificationName;
//    }

    /**
     * 状态
     */
    @TableField("display")
    private Integer display;

    @Override
    public String toString() {
        return "Customer{" +
                "customerId=" + customerId +

                ", agent=" + agent +
                ", classification=" + classification +
                ", sort=" + sort +
                ", customerName='" + customerName + '\'' +
                ", customerLevelId=" + customerLevelId +
                ", status=" + status +
                ", originId=" + originId +
                ", note='" + note + '\'' +
                ", userId=" + userId +
                ", emall='" + emall + '\'' +
                ", url='" + url + '\'' +
                ", industryId=" + industryId +
                ", legal='" + legal + '\'' +
                ", utscc='" + utscc + '\'' +
                ", companyType='" + companyType + '\'' +
                ", businessTerm=" + businessTerm +
                ", signIn='" + signIn + '\'' +
                ", introduction='" + introduction + '\'' +
                ", createUser=" + createUser +
                ", updateUser=" + updateUser +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", display=" + display +
                ", deptId=" + deptId +
                ", setup=" + setup +
                '}';
    }

    /**
     * 部门编号
     */
    @TableField("deptId")
    private Long deptId;


    /**
     * 成立时间
     */
    @TableField("setup")
    private Date setup;

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getOriginId() {
        return originId;
    }

    public void setOriginId(Long originId) {
        this.originId = originId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getIndustryId() {
        return industryId;
    }

    public void setIndustryId(Long industryId) {
        this.industryId = industryId;
    }

    public String getEmall() {
        return emall;
    }

    public void setEmall(String emall) {
        this.emall = emall;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getAgent() {
        return agent;
    }

    public void setAgent(Integer agent) {
        this.agent = agent;
    }

    public Integer getClassification() {
        return classification;
    }

    public void setClassification(Integer classification) {
        this.classification = classification;
    }

    public Long getSort() {
        return sort;
    }

    public void setSort(Long sort) {
        this.sort = sort;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Long getCustomerLevelId() {
        return customerLevelId;
    }

    public void setCustomerLevelId(Long customerLevelId) {
        this.customerLevelId = customerLevelId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }


    public Date getSetup() {
        return setup;
    }

    public void setSetup(Date setup) {
        this.setup = setup;
    }

    public String getLegal() {
        return legal;
    }

    public void setLegal(String legal) {
        this.legal = legal;
    }

    public String getUtscc() {
        return utscc;
    }

    public void setUtscc(String utscc) {
        this.utscc = utscc;
    }

    public String getCompanyType() {
        return companyType;
    }

    public void setCompanyType(String companyType) {
        this.companyType = companyType;
    }

    public Date getBusinessTerm() {
        return businessTerm;
    }

    public void setBusinessTerm(Date businessTerm) {
        this.businessTerm = businessTerm;
    }

    public String getSignIn() {
        return signIn;
    }

    public void setSignIn(String signIn) {
        this.signIn = signIn;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
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

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }
}
