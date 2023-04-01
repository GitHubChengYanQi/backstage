package cn.atsoft.dasheng.crm.model.result;

import cn.atsoft.dasheng.app.entity.*;
import cn.atsoft.dasheng.app.model.result.ContractResult;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.crm.entity.Bank;
import cn.atsoft.dasheng.crm.entity.OrderDetail;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import cn.atsoft.dasheng.sys.modular.system.model.result.UserResult;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * <p>
 * 订单表
 * </p>
 *
 * @author song
 * @since 2022-02-23
 */
@Data
@ApiModel
public class OrderResult implements Serializable {

    @ApiModelProperty("入库进度百分比")
    private Integer inStockRate;

    @ApiModelProperty("付款进度百分比")
    private Integer paymentRate;

    @ApiModelProperty("发票进度百分比")
    private Integer invoiceBillRate;

    private int totalNumber;

    private Double totalAmount;

    private Double paymentPrice;

    private Double deficientPrice;

    private String fileId;

    private String leadTime;

    private static final long serialVersionUID = 1L;

    private PaymentResult paymentResult;

    private List<PaymentRecordResult> paymentRecordResults;

    private List<OrderDetailResult> detailResults;

    private Date date;

    private Long userId;

    private Contacts deliverer;  //交货人

    private String AContactsPhone;

    private String BContactsPhone;

    private Double allMoney;

    private Adress adress;

    private Long adressId;

    private Customer Acustomer = new Customer();
    private Customer Bcustomer = new Customer();

    private Adress Aadress = new Adress();
    private Adress Badress = new Adress();

    private Contacts Acontacts = new Contacts();
    private Contacts Bcontacts = new Contacts();

    private Bank Abank = new Bank();
    private Bank Bbank = new Bank();

    private Phone Aphone = new Phone();
    private Phone Bphone = new Phone();

    private Integer status;
    private String coding;
    private UserResult user;

    private String ACompanyPhone;
    private String BCompanyPhone;

    private Contract contract;

    /**
     * 货币种类
     */
    private String currency;
    /**
     * 订单编号
     */
    @ApiModelProperty("订单编号")
    private Long orderId;

    /**
     * 合同id
     */
    @ApiModelProperty("合同id")
    private Long contractId;

    /**
     * 订单金额
     */
    @ApiModelProperty("订单金额")
    private Long price;

    /**
     * 买方id
     */
    @ApiModelProperty("买方id")
    private Long buyerId;

    /**
     * 卖方id
     */
    @ApiModelProperty("卖方id")
    private Long sellerId;

    /**
     * 主题
     */
    @ApiModelProperty("主题")
    private String theme;

    /**
     * 来源Json字符串
     */
    @ApiModelProperty("来源Json字符串")
    @JSONField(serialize = false)
    private String origin;

    /**
     * 1采购 2销售
     */
    @ApiModelProperty("1采购 2销售")
    private Integer type;

    /**
     * 是否生成合同
     */
    @ApiModelProperty("是否生成合同")
    private Integer generateContract;

    /**
     * 交货日期
     */
    @ApiModelProperty("交货日期")
    private Date deliveryDate;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remark;

    /**
     * 创建者
     */
    @ApiModelProperty(hidden = true)
    @JSONField(serialize = false)
    private Long createUser;

    /**
     * 部门编号
     */
    @ApiModelProperty("部门编号")
    @JSONField(serialize = false)
    private Long deptId;

    /**
     * 状态
     */
    @ApiModelProperty("状态")
    @JSONField(serialize = false)
    private Integer display;

    /**
     * 修改时间
     */
    @ApiModelProperty(hidden = true)
    @JSONField(serialize = false)
    private Date updateTime;

    /**
     * 创建时间
     */
    @ApiModelProperty(hidden = true)
    private Date createTime;

    /**
     * 修改者
     */
    @ApiModelProperty(hidden = true)
    @JSONField(serialize = false)
    private Long updateUser;

    /**
     * 甲方联系人id
     */
    @ApiModelProperty("甲方联系人id")
    private Long partyAContactsId;

    /**
     * 甲方地址id
     */
    @ApiModelProperty("甲方地址id")
    private Long partyAAdressId;

    /**
     * 甲方电话
     */
    @ApiModelProperty("甲方电话")
    private String partyAPhone;

    /**
     * 甲方委托人id
     */
    @ApiModelProperty("甲方委托人id")
    private Long partyAClientId;

    /**
     * 乙方联系人id
     */
    @ApiModelProperty("乙方联系人id")
    private Long partyBContactsId;

    /**
     * 乙方地址id
     */
    @ApiModelProperty("乙方地址id")
    private Long partyBAdressId;

    /**
     * 乙方电话
     */
    @ApiModelProperty("乙方电话")
    private String partyBPhone;

    /**
     * 乙方委托人id
     */
    @ApiModelProperty("乙方委托人id")
    private Long partyBClientId;

    /**
     * 乙方 银行Id
     */
    @ApiModelProperty("乙方 银行Id")
    private Long partyBBankId;

    /**
     * 乙方开户行账号
     */
    @ApiModelProperty("乙方开户行账号")
    private String partyBBankAccount;

    /**
     * 乙方法人
     */
    @ApiModelProperty("乙方法人")
    private String partyBLegalPerson;

    /**
     * 乙方公司电话
     */
    @ApiModelProperty("乙方公司电话")
    private String partyBCompanyPhone;

    /**
     * 乙方传真
     */
    @ApiModelProperty("乙方传真")
    private String partyBFax;

    /**
     * 乙方邮编
     */
    @ApiModelProperty("乙方邮编")
    private String partyBZipcode;

    /**
     * 甲方银行Id
     */
    @ApiModelProperty("甲方银行Id")
    private Long partyABankId;

    /**
     * 甲方开户行账号
     */
    @ApiModelProperty("甲方开户行账号")
    private String partyABankAccount;

    /**
     * 甲方开户行号
     */
    private Long partyABankNo;
    /**
     * 乙方方开户行号
     */
    private Long partyBBankNo;

    /**
     * 甲方法人
     */
    @ApiModelProperty("甲方法人")
    private String partyALegalPerson;

    /**
     * 甲方公司电话
     */
    @ApiModelProperty("甲方公司电话")
    private String partyACompanyPhone;

    /**
     * 甲方传真
     */
    @ApiModelProperty("甲方传真")
    private String partyAFax;

    /**
     * 甲方
     */
    @ApiModelProperty("甲方")
    private String partyAZipcode;
    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;

    public Double getAllMoney() {
        if(ToolUtil.isNotEmpty(allMoney)){
            return BigDecimal.valueOf(allMoney).divide(BigDecimal.valueOf(100),2, RoundingMode.DOWN).doubleValue();
        }
        return null;
    }
    public Double getTotalAmount() {
        if(ToolUtil.isNotEmpty(totalAmount)){
            return BigDecimal.valueOf(totalAmount).divide(BigDecimal.valueOf(100),2, RoundingMode.DOWN).doubleValue();
        }
        return null;
    }
    public Double getDeficientPrice() {
        if(ToolUtil.isNotEmpty(deficientPrice)){
            return BigDecimal.valueOf(deficientPrice).divide(BigDecimal.valueOf(100),2, RoundingMode.DOWN).doubleValue();
        }
        return null;
    }
    public Double getPaymentPrice() {
        if(ToolUtil.isNotEmpty(paymentPrice)){
            return BigDecimal.valueOf(paymentPrice).divide(BigDecimal.valueOf(100),2, RoundingMode.DOWN).doubleValue();
        }
        return null;
    }
}
