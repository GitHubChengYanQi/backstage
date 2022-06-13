package cn.atsoft.dasheng.app.entity;

import cn.atsoft.dasheng.crm.pojo.Payment;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 合同表
 * </p>
 *
 * @author
 * @since 2021-07-21
 */

@TableName(value = "daoxin_crm_contract", autoResultMap = true)
@Accessors(chain = true)
public class Contract implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 来源
     */
    @TableField("source")
    private String source;

    @TableField("contract_class_id")
    private Long contractClassId;
    /**
     * 来源id
     */
    @TableField("source_id")
    private Long sourceId;
    /**
     * 合同id
     */
    @TableId(value = "contract_id", type = IdType.ID_WORKER)
    private Long contractId;

    /**
     * 合同编号
     */
    @TableField("coding")
    private String coding;
    /**
     * 合同名称
     */
    @TableField("name")
    private String name;


    @TableField("template_id")
    private Long templateId;


    @TableField("audit")
    private Integer audit;

    @TableField("allMoney")
    private Integer allMoney;
    /**
     * 付款信息
     */
    @TableField(typeHandler = FastjsonTypeHandler.class)
    private Payment payment;

    @TableField("party_a")
    private Long partyA;
    @TableField("party_a_adress_id")
    private Long partyAAdressId;
    @TableField("party_b_adress_id")
    private Long partyBAdressId;
    @TableField("party_a_contacts_id")
    private Long partyAContactsId;
    @TableField("party_a_phone")
    private Long partyAPhone;
    @TableField("party_b_phone")
    private Long partyBPhone;

    @TableField("party_b_contacts_id")
    private Long partyBContactsId;


    @TableField("party_b")
    private Long partyB;

    /**
     * 负责人id
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 备注
     */
    @TableField("note")
    private String note;

    /**
     * 创建时间
     */
    @TableField("time")
    private Date time;

    /**
     * 内容
     */
    @TableField("content")
    private String content;


    @TableField("deptId")
    private Long deptId;


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
     * 主题
     */
    @TableField("theme")
    private String theme;

    /**
     * 来源
     */
    @TableField("origin")
    private String origin;

    public String getCoding() {
        return coding;
    }

    public void setCoding(String coding) {
        this.coding = coding;
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


    public Integer getAllMoney() {
        return allMoney;
    }

    public Long getContractClassId() {
        return contractClassId;
    }

    public void setContractClassId(Long contractClassId) {
        this.contractClassId = contractClassId;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public void setAllMoney(Integer allMoney) {
        this.allMoney = allMoney;
    }

    public Long getPartyA() {
        return partyA;
    }

    public void setPartyA(Long partyA) {
        this.partyA = partyA;
    }

    public Long getPartyB() {
        return partyB;
    }

    public void setPartyB(Long partyB) {
        this.partyB = partyB;
    }

    public Long getPartyAAdressId() {
        return partyAAdressId;
    }

    public void setPartyAAdressId(Long partyAAdressId) {
        this.partyAAdressId = partyAAdressId;
    }

    public Long getPartyBAdressId() {
        return partyBAdressId;
    }

    public void setPartyBAdressId(Long partyBAdressId) {
        this.partyBAdressId = partyBAdressId;
    }

    public Long getPartyAContactsId() {
        return partyAContactsId;
    }

    public void setPartyAContactsId(Long partyAContactsId) {
        this.partyAContactsId = partyAContactsId;
    }

    public Long getPartyBContactsId() {
        return partyBContactsId;
    }

    public void setPartyBContactsId(Long partyBContactsId) {
        this.partyBContactsId = partyBContactsId;
    }

    public Long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }

    public Integer getAudit() {
        return audit;
    }

    public void setAudit(Integer audit) {
        this.audit = audit;
    }

    public Long getPartyAPhone() {
        return partyAPhone;
    }

    public void setPartyAPhone(Long partyAPhone) {
        this.partyAPhone = partyAPhone;
    }

    public Long getPartyBPhone() {
        return partyBPhone;
    }

    public void setPartyBPhone(Long partyBPhone) {
        this.partyBPhone = partyBPhone;
    }

    public Long getContractId() {
        return contractId;
    }

    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
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

    @Override
    public String toString() {
        return "Contract{" +
                "contractId=" + contractId +
                ", name=" + name +
                ", userId=" + userId +
                ", note=" + note +
                ", time=" + time +
                ", content=" + content +
                ", createUser=" + createUser +
                ", updateUser=" + updateUser +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", display=" + display +
                "}";
    }
}
