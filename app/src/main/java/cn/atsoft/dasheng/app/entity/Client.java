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
 * 客户管理表
 * </p>
 *
 * @author 
 * @since 2021-07-23
 */
@TableName("daoxin_client")
public class Client implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 客户id
     */
      @TableId(value = "client_id", type = IdType.AUTO)
    private Long clientId;

    /**
     * 客户名称
     */
    @TableField("client_name")
    private String clientName;

    /**
     * 客户地址id
     */
    @TableField("adress_id")
    private Long adressId;

    /**
     * 联系人id
     */
    @TableField("contacts_id")
    private Long contactsId;

    /**
     * 成立时间
     */
    @TableField("setup")
    private Date setup;

    /**
     * 法定代表人
     */
    @TableField("legal")
    private String legal;

    /**
     * 统一社会信用代码
     */
    @TableField("utscc")
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
    private Date businessTerm;

    /**
     * 注册地址
     */
    @TableField("sign_in")
    private String signIn;

    /**
     * 简介
     */
    @TableField("introduction")
    private String introduction;

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
     * 部门编号
     */
    @TableField("deptId")
    private Long deptId;


    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public Long getAdressId() {
        return adressId;
    }

    public void setAdressId(Long adressId) {
        this.adressId = adressId;
    }

    public Long getContactsId() {
        return contactsId;
    }

    public void setContactsId(Long contactsId) {
        this.contactsId = contactsId;
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

    @Override
    public String toString() {
        return "Client{" +
        "clientId=" + clientId +
        ", clientName=" + clientName +
        ", adressId=" + adressId +
        ", contactsId=" + contactsId +
        ", setup=" + setup +
        ", legal=" + legal +
        ", utscc=" + utscc +
        ", companyType=" + companyType +
        ", businessTerm=" + businessTerm +
        ", signIn=" + signIn +
        ", introduction=" + introduction +
        ", createUser=" + createUser +
        ", updateUser=" + updateUser +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        ", display=" + display +
        ", deptId=" + deptId +
        "}";
    }
}
