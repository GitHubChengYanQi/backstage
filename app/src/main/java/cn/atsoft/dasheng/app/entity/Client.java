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
 * @since 2021-07-16
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
    @TableField("name")
    private String name;

    /**
     * 客户地址id
     */
    @TableField("adress_id")
    private String adressId;

    /**
     * 联系电话
     */
    @TableField("phone1")
    private Long phone1;

    /**
     * 固定电话
     */
    @TableField("phone2")
    private Long phone2;

    /**
     * 订单号
     */
    @TableField("order_id")
    private Long orderId;

    /**
     * 下单时间
     */
    @TableField("order_time")
    private Date orderTime;

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
    private Long utscc;

    /**
     * 公司类型
     */
    @TableField("company_type")
    private String companyType;

    /**
     * 营业期限
     */
    @TableField("business_term")
    private Long businessTerm;

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


    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAdressId() {
        return adressId;
    }

    public void setAdressId(String adressId) {
        this.adressId = adressId;
    }

    public Long getPhone1() {
        return phone1;
    }

    public void setPhone1(Long phone1) {
        this.phone1 = phone1;
    }

    public Long getPhone2() {
        return phone2;
    }

    public void setPhone2(Long phone2) {
        this.phone2 = phone2;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
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

    public Long getUtscc() {
        return utscc;
    }

    public void setUtscc(Long utscc) {
        this.utscc = utscc;
    }

    public String getCompanyType() {
        return companyType;
    }

    public void setCompanyType(String companyType) {
        this.companyType = companyType;
    }

    public Long getBusinessTerm() {
        return businessTerm;
    }

    public void setBusinessTerm(Long businessTerm) {
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

    @Override
    public String toString() {
        return "Client{" +
        "clientId=" + clientId +
        ", name=" + name +
        ", adressId=" + adressId +
        ", phone1=" + phone1 +
        ", phone2=" + phone2 +
        ", orderId=" + orderId +
        ", orderTime=" + orderTime +
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
        "}";
    }
}
