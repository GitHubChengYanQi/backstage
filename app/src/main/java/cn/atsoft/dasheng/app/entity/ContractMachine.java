package cn.atsoft.dasheng.app.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 机床合同表
 * </p>
 *
 * @author 
 * @since 2021-07-20
 */
@TableName("daoxin_contract_machine")
public class ContractMachine implements Serializable {
    public String getSigningSite() {
        return signingSite;
    }

    public void setSigningSite(String signingSite) {
        this.signingSite = signingSite;
    }

    private static final long serialVersionUID = 1L;

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
    @TableField("contract_name")
    private  String contractName;
    @TableField("creation_time")
     private Date creationTime;
    @TableField("note")
     private  String note;
    @TableField("signing_site")
    private  String signingSite;
    /**
     * 合同
     */
      @TableId(value = "contract_id", type = IdType.ID_WORKER)
    private Long contractId;

    /**
     * 购货单位
     */
    @TableField("purchase_unit_id")
    private Long purchaseUnitId;

    /**
     * 供货单位
     */
    @TableField("supply_unit_id")
    private Long supplyUnitId;

    /**
     * 出售设备
     */
    @TableField("equipment")
    private String equipment;

    /**
     * 项目名称
     */
    @TableField("project_name")
    private String projectName;

    /**
     * 合同总金额
     */
    @TableField("contract_amount")
    private Long contractAmount;

    /**
     * 付款方式
     */
    @TableField("payment")
    private String payment;

    /**
     * 合同生效日期天数
     */
    @TableField("effective_date")
    private Long effectiveDate;

    /**
     * 合同总额
     */
    @TableField("amount_one")
    private Long amountOne;

    /**
     * 安装一周内付款总额
     */
    @TableField("amount_two")
    private Long amountTwo;

    /**
     * 运行日期
     */
    @TableField("run_date")
    private Long runDate;

    /**
     * 运行一周内付款总额
     */
    @TableField("amount_three")
    private Long amountThree;

    /**
     * 交货地点
     */
    @TableField("pace_delivery")
    private String paceDelivery;

    /**
     * 收货人名称
     */
    @TableField("deliverer_name")
    private String delivererName;

    /**
     * 收货人地址
     */
    @TableField("delivery_address")
    private String deliveryAddress;

    /**
     * 运输方式
     */
    @TableField("transportation_mode")
    private String transportationMode;

    /**
     * 安装调试地点
     */
    @TableField("installation_location")
    private String installationLocation;

    /**
     * 验收时间
     */
    @TableField("acceptance_time_one")
    private Integer acceptanceTimeOne;

    @TableField("acceptance_time_two")
    private Integer acceptanceTimeTwo;

    @TableField("acceptance_time_three")
    private Integer acceptanceTimeThree;

    /**
     * 收地点
     */
    @TableField("receive_location")
    private String receiveLocation;

    /**
     * 收费标准
     */
    @TableField("charge_standard")
    private String chargeStandard;

    /**
     * 保修期限
     */
    @TableField("warranty_period_one")
    private Integer warrantyPeriodOne;

    @TableField("warranty_period_two")
    private Integer warrantyPeriodTwo;

    @TableField("warranty_period_three")
    private Integer warrantyPeriodThree;

    @TableField("warranty_period_four")
    private Integer warrantyPeriodFour;

    @TableField("warranty_period_five")
    private Integer warrantyPeriodFive;

    /**
     * 甲方代表人
     */
    @TableField("party_a_name")
    private String partyAName;

    /**
     * 甲方日期
     */
    @TableField("Party_a_time")
    private Date partyATime;

    /**
     * 乙方代表人
     */
    @TableField("Party_b_name")
    private String partyBName;

    /**
     * 乙方日期
     */
    @TableField("Party_b_time")
    private Date partyBTime;


    public Long getContractId() {
        return contractId;
    }

    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }

    public Long getPurchaseUnitId() {
        return purchaseUnitId;
    }

    public void setPurchaseUnitId(Long purchaseUnitId) {
        this.purchaseUnitId = purchaseUnitId;
    }

    public Long getSupplyUnitId() {
        return supplyUnitId;
    }

    public void setSupplyUnitId(Long supplyUnitId) {
        this.supplyUnitId = supplyUnitId;
    }

    public String getEquipment() {
        return equipment;
    }

    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Long getContractAmount() {
        return contractAmount;
    }

    public void setContractAmount(Long contractAmount) {
        this.contractAmount = contractAmount;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public Long getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Long effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public Long getAmountOne() {
        return amountOne;
    }

    public void setAmountOne(Long amountOne) {
        this.amountOne = amountOne;
    }

    public Long getAmountTwo() {
        return amountTwo;
    }

    public void setAmountTwo(Long amountTwo) {
        this.amountTwo = amountTwo;
    }

    public Long getRunDate() {
        return runDate;
    }

    public void setRunDate(Long runDate) {
        this.runDate = runDate;
    }

    public Long getAmountThree() {
        return amountThree;
    }

    public void setAmountThree(Long amountThree) {
        this.amountThree = amountThree;
    }

    public String getPaceDelivery() {
        return paceDelivery;
    }

    public void setPaceDelivery(String paceDelivery) {
        this.paceDelivery = paceDelivery;
    }

    public String getDelivererName() {
        return delivererName;
    }

    public void setDelivererName(String delivererName) {
        this.delivererName = delivererName;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getTransportationMode() {
        return transportationMode;
    }

    public void setTransportationMode(String transportationMode) {
        this.transportationMode = transportationMode;
    }

    public String getInstallationLocation() {
        return installationLocation;
    }

    public void setInstallationLocation(String installationLocation) {
        this.installationLocation = installationLocation;
    }

    public Integer getAcceptanceTimeOne() {
        return acceptanceTimeOne;
    }

    public void setAcceptanceTimeOne(Integer acceptanceTimeOne) {
        this.acceptanceTimeOne = acceptanceTimeOne;
    }

    public Integer getAcceptanceTimeTwo() {
        return acceptanceTimeTwo;
    }

    public void setAcceptanceTimeTwo(Integer acceptanceTimeTwo) {
        this.acceptanceTimeTwo = acceptanceTimeTwo;
    }

    public Integer getAcceptanceTimeThree() {
        return acceptanceTimeThree;
    }

    public void setAcceptanceTimeThree(Integer acceptanceTimeThree) {
        this.acceptanceTimeThree = acceptanceTimeThree;
    }

    public String getReceiveLocation() {
        return receiveLocation;
    }

    public void setReceiveLocation(String receiveLocation) {
        this.receiveLocation = receiveLocation;
    }

    public String getChargeStandard() {
        return chargeStandard;
    }

    public void setChargeStandard(String chargeStandard) {
        this.chargeStandard = chargeStandard;
    }

    public Integer getWarrantyPeriodOne() {
        return warrantyPeriodOne;
    }

    public void setWarrantyPeriodOne(Integer warrantyPeriodOne) {
        this.warrantyPeriodOne = warrantyPeriodOne;
    }

    public Integer getWarrantyPeriodTwo() {
        return warrantyPeriodTwo;
    }

    public void setWarrantyPeriodTwo(Integer warrantyPeriodTwo) {
        this.warrantyPeriodTwo = warrantyPeriodTwo;
    }

    public Integer getWarrantyPeriodThree() {
        return warrantyPeriodThree;
    }

    @Override
    public String toString() {
        return "ContractMachine{" +
                "contractName='" + contractName + '\'' +
                ", creationTime='" + creationTime + '\'' +
                ", note='" + note + '\'' +
                ", signingSite='" + signingSite + '\'' +
                ", contractId=" + contractId +
                ", purchaseUnitId=" + purchaseUnitId +
                ", supplyUnitId=" + supplyUnitId +
                ", equipment='" + equipment + '\'' +
                ", projectName='" + projectName + '\'' +
                ", contractAmount=" + contractAmount +
                ", payment='" + payment + '\'' +
                ", effectiveDate=" + effectiveDate +
                ", amountOne=" + amountOne +
                ", amountTwo=" + amountTwo +
                ", runDate=" + runDate +
                ", amountThree=" + amountThree +
                ", paceDelivery='" + paceDelivery + '\'' +
                ", delivererName='" + delivererName + '\'' +
                ", deliveryAddress='" + deliveryAddress + '\'' +
                ", transportationMode='" + transportationMode + '\'' +
                ", installationLocation='" + installationLocation + '\'' +
                ", acceptanceTimeOne=" + acceptanceTimeOne +
                ", acceptanceTimeTwo=" + acceptanceTimeTwo +
                ", acceptanceTimeThree=" + acceptanceTimeThree +
                ", receiveLocation='" + receiveLocation + '\'' +
                ", chargeStandard='" + chargeStandard + '\'' +
                ", warrantyPeriodOne=" + warrantyPeriodOne +
                ", warrantyPeriodTwo=" + warrantyPeriodTwo +
                ", warrantyPeriodThree=" + warrantyPeriodThree +
                ", warrantyPeriodFour=" + warrantyPeriodFour +
                ", warrantyPeriodFive=" + warrantyPeriodFive +
                ", partyAName='" + partyAName + '\'' +
                ", partyATime=" + partyATime +
                ", partyBName='" + partyBName + '\'' +
                ", partyBTime=" + partyBTime +
                '}';
    }

    public void setWarrantyPeriodThree(Integer warrantyPeriodThree) {
        this.warrantyPeriodThree = warrantyPeriodThree;
    }

    public Integer getWarrantyPeriodFour() {
        return warrantyPeriodFour;
    }

    public void setWarrantyPeriodFour(Integer warrantyPeriodFour) {
        this.warrantyPeriodFour = warrantyPeriodFour;
    }

    public Integer getWarrantyPeriodFive() {
        return warrantyPeriodFive;
    }

    public void setWarrantyPeriodFive(Integer warrantyPeriodFive) {
        this.warrantyPeriodFive = warrantyPeriodFive;
    }

    public String getPartyAName() {
        return partyAName;
    }

    public void setPartyAName(String partyAName) {
        this.partyAName = partyAName;
    }

    public Date getPartyATime() {
        return partyATime;
    }

    public void setPartyATime(Date partyATime) {
        this.partyATime = partyATime;
    }

    public String getPartyBName() {
        return partyBName;
    }

    public void setPartyBName(String partyBName) {
        this.partyBName = partyBName;
    }

    public Date getPartyBTime() {
        return partyBTime;
    }

    public void setPartyBTime(Date partyBTime) {
        this.partyBTime = partyBTime;
    }


}
