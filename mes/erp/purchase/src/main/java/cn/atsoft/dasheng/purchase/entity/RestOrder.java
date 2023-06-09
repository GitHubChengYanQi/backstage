package cn.atsoft.dasheng.purchase.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 订单表
 * </p>
 *
 * @author song
 * @since 2022-02-23
 */
@TableName("daoxin_crm_order")
public class RestOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 订单总金额
     */
    @TableField("total_amount")
    private Integer totalAmount;

    /**
     * 交货周期
     */
    @TableField("lead_time")
    private String leadTime;

    @TableField("file_id")
    private Long fileId;
    /**
     * 交货地点
     */
    @TableField("adress_id")
    private Long adressId;


    /**
     * 接货人
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 订单编号
     */
    @TableId(value = "order_id", type = IdType.ID_WORKER)
    private Long orderId;

    @TableField("coding")
    private String coding;
    /**
     * 状态
     */
    @TableField("status")
    private Integer status;
    /**
     * 日期
     */
    @TableField("date")
    private Date date;
    /**
     * 货币种类
     */
    @TableField("currency")
    private String currency;
    /**
     * 合同id
     */
    @TableField("contract_id")
    private Long contractId;

    /**
     * 订单金额
     */
    @TableField("price")
    private Long price;

    /**
     * 买方id
     */
    @TableField("buyer_id")
    private Long buyerId;

    /**
     * 卖方id
     */
    @TableField("seller_id")
    private Long sellerId;

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
     * 1采购 2销售
     */
    @TableField("type")
    private Integer type;

    /**
     * 是否生成合同
     */
    @TableField("generate_contract")
    private Integer generateContract;

    /**
     * 交货日期
     */
    @TableField("delivery_date")
    private Date deliveryDate;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

    /**
     * 甲方联系人id
     */
    @TableField("party_a_contacts_id")
    private Long partyAContactsId;

    /**
     * 甲方地址id
     */
    @TableField("party_a_adress_id")
    private Long partyAAdressId;

    /**
     * 甲方电话
     */
    @TableField("party_a_phone")
    private Long partyAPhone;

    /**
     * 甲方委托人id
     */
    @TableField("party_a_client_id")
    private Long partyAClientId;

    /**
     * 乙方联系人id
     */
    @TableField("party_b_contacts_id")
    private Long partyBContactsId;

    /**
     * 乙方地址id
     */
    @TableField("party_b_adress_id")
    private Long partyBAdressId;

    /**
     * 乙方电话
     */
    @TableField("party_b_phone")
    private Long partyBPhone;

    /**
     * 乙方委托人id
     */
    @TableField("party_b_client_id")
    private Long partyBClientId;

    /**
     * 乙方 银行Id
     */
    @TableField("party_b_bank_id")
    private Long partyBBankId;

    /**
     * 乙方开户行账号
     */
    @TableField("party_b_bank_account")
    private String partyBBankAccount;

    /**
     * 乙方法人
     */
    @TableField("party_b_legal_person")
    private String partyBLegalPerson;

    /**
     * 乙方公司电话
     */
    @TableField("party_b_company_phone")
    private String partyBCompanyPhone;

    /**
     * 乙方传真
     */
    @TableField("party_b_fax")
    private String partyBFax;

    /**
     * 乙方邮编
     */
    @TableField("party_b_zipCode")
    private String partyBZipcode;

    /**
     * 甲方银行Id
     */
    @TableField("party_a_bank_id")
    private Long partyABankId;

    /**
     * 甲方开户行账号
     */
    @TableField("party_a_bank_account")
    private String partyABankAccount;

    /**
     * 甲方法人
     */
    @TableField("party_a_legal_person")
    private String partyALegalPerson;

    /**
     * 甲方公司电话
     */
    @TableField("party_a_company_phone")
    private String partyACompanyPhone;

    /**
     * 甲方传真
     */
    @TableField("party_a_fax")
    private String partyAFax;

    /**
     * 甲方
     */
    @TableField("party_a_zipCode")
    private String partyAZipcode;


    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public String getCoding() {
        return coding;
    }

    public void setCoding(String coding) {
        this.coding = coding;
    }

    public Long getPartyAContactsId() {
        return partyAContactsId;
    }

    public void setPartyAContactsId(Long partyAContactsId) {
        this.partyAContactsId = partyAContactsId;
    }

    public Long getPartyAAdressId() {
        return partyAAdressId;
    }

    public void setPartyAAdressId(Long partyAAdressId) {
        this.partyAAdressId = partyAAdressId;
    }

    public Long getPartyAPhone() {
        return partyAPhone;
    }

    public void setPartyAPhone(Long partyAPhone) {
        this.partyAPhone = partyAPhone;
    }

    public Long getPartyAClientId() {
        return partyAClientId;
    }

    public void setPartyAClientId(Long partyAClientId) {
        this.partyAClientId = partyAClientId;
    }

    public Long getPartyBContactsId() {
        return partyBContactsId;
    }

    public void setPartyBContactsId(Long partyBContactsId) {
        this.partyBContactsId = partyBContactsId;
    }

    public Long getPartyBAdressId() {
        return partyBAdressId;
    }

    public void setPartyBAdressId(Long partyBAdressId) {
        this.partyBAdressId = partyBAdressId;
    }

    public Long getPartyBPhone() {
        return partyBPhone;
    }

    public void setPartyBPhone(Long partyBPhone) {
        this.partyBPhone = partyBPhone;
    }

    public Long getPartyBClientId() {
        return partyBClientId;
    }

    public void setPartyBClientId(Long partyBClientId) {
        this.partyBClientId = partyBClientId;
    }

    public Long getPartyBBankId() {
        return partyBBankId;
    }

    public void setPartyBBankId(Long partyBBankId) {
        this.partyBBankId = partyBBankId;
    }

    public String getPartyBBankAccount() {
        return partyBBankAccount;
    }

    public void setPartyBBankAccount(String partyBBankAccount) {
        this.partyBBankAccount = partyBBankAccount;
    }

    public String getPartyBLegalPerson() {
        return partyBLegalPerson;
    }

    public void setPartyBLegalPerson(String partyBLegalPerson) {
        this.partyBLegalPerson = partyBLegalPerson;
    }

    public String getPartyBCompanyPhone() {
        return partyBCompanyPhone;
    }

    public void setPartyBCompanyPhone(String partyBCompanyPhone) {
        this.partyBCompanyPhone = partyBCompanyPhone;
    }

    public String getPartyBFax() {
        return partyBFax;
    }

    public void setPartyBFax(String partyBFax) {
        this.partyBFax = partyBFax;
    }

    public String getPartyBZipcode() {
        return partyBZipcode;
    }

    public void setPartyBZipcode(String partyBZipcode) {
        this.partyBZipcode = partyBZipcode;
    }

    public Long getPartyABankId() {
        return partyABankId;
    }

    public void setPartyABankId(Long partyABankId) {
        this.partyABankId = partyABankId;
    }

    public String getPartyABankAccount() {
        return partyABankAccount;
    }

    public void setPartyABankAccount(String partyABankAccount) {
        this.partyABankAccount = partyABankAccount;
    }

    public String getPartyALegalPerson() {
        return partyALegalPerson;
    }

    public void setPartyALegalPerson(String partyALegalPerson) {
        this.partyALegalPerson = partyALegalPerson;
    }

    public String getPartyACompanyPhone() {
        return partyACompanyPhone;
    }

    public void setPartyACompanyPhone(String partyACompanyPhone) {
        this.partyACompanyPhone = partyACompanyPhone;
    }

    public String getPartyAFax() {
        return partyAFax;
    }

    public void setPartyAFax(String partyAFax) {
        this.partyAFax = partyAFax;
    }

    public String getPartyAZipcode() {
        return partyAZipcode;
    }

    public void setPartyAZipcode(String partyAZipcode) {
        this.partyAZipcode = partyAZipcode;
    }

    /**
     * 创建者
     */
    @TableField(value = "create_user", fill = FieldFill.INSERT)
    private Long createUser;

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

    public Long getPartyABankNo() {
        return partyABankNo;
    }

    public void setPartyABankNo(Long partyABankNo) {
        this.partyABankNo = partyABankNo;
    }

    public Long getPartyBBankNo() {
        return partyBBankNo;
    }

    public void setPartyBBankNo(Long partyBBankNo) {
        this.partyBBankNo = partyBBankNo;
    }

    /**
     * 甲方开户行号
     */
    @TableField("party_a_bank_no")
    private Long partyABankNo;
    /**
     * 乙方方开户行号
     */
    @TableField("party_b_bank_no")
    private Long partyBBankNo;

    /**
     * 状态
     */
    @TableField("display")
    private Integer display;

    /**
     * 修改时间
     */
    @TableField(value = "update_time", fill = FieldFill.UPDATE)
    private Date updateTime;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 修改者
     */
    @TableField(value = "update_user", fill = FieldFill.UPDATE)
    private Long updateUser;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getContractId() {
        return contractId;
    }

    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }


    public Long getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(Long buyerId) {
        this.buyerId = buyerId;
    }

    public Long getSellerId() {
        return sellerId;
    }

    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getGenerateContract() {
        return generateContract;
    }

    public void setGenerateContract(Integer generateContract) {
        this.generateContract = generateContract;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getCreateUser() {
        return createUser;
    }

    public void setCreateUser(Long createUser) {
        this.createUser = createUser;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public Integer getDisplay() {
        return display;
    }

    public void setDisplay(Integer display) {
        this.display = display;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(Long updateUser) {
        this.updateUser = updateUser;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getAdressId() {
        return adressId;
    }

    public void setAdressId(Long adressId) {
        this.adressId = adressId;
    }

    public String getLeadTime() {
        return leadTime;
    }

    public void setLeadTime(String leadTime) {
        this.leadTime = leadTime;
    }

    public Integer getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Integer totalAmount) {
        this.totalAmount = totalAmount;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", contractId=" + contractId +
                ", price=" + price +
                ", sellerId=" + sellerId +
                ", theme=" + theme +
                ", origin=" + origin +
                ", type=" + type +
                ", generateContract=" + generateContract +
                ", deliveryDate=" + deliveryDate +
                ", remark=" + remark +
                ", createUser=" + createUser +
                ", deptId=" + deptId +
                ", display=" + display +
                ", updateTime=" + updateTime +
                ", createTime=" + createTime +
                ", updateUser=" + updateUser +
                "}";
    }
}
