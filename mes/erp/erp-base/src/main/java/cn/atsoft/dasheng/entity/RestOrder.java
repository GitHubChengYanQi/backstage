package cn.atsoft.dasheng.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

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
@Data
public class RestOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 订单总金额
     */
    private Integer totalAmount;

    /**
     * 交货周期
     */
    private String leadTime;

    private Long fileId;
    /**
     * 交货地点
     */
    private Long adressId;


    /**
     * 接货人
     */
    private Long userId;

    /**
     * 订单编号
     */
    private Long orderId;

    private String coding;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 日期
     */
    private Date date;
    /**
     * 货币种类
     */
    private String currency;
    /**
     * 合同id
     */
    private Long contractId;

    /**
     * 订单金额
     */
    private Long price;

    /**
     * 买方id
     */
    private Long buyerId;

    /**
     * 卖方id
     */
    private Long sellerId;

    /**
     * 主题
     */
    private String theme;

    /**
     * 来源Json字符串
     */
    private String origin;

    /**
     * 1采购 2销售
     */
    private Integer type;

    /**
     * 是否生成合同
     */
    private Integer generateContract;

    /**
     * 交货日期
     */
    private Date deliveryDate;

    /**
     * 备注
     */
    private String remark;

    /**
     * 甲方联系人id
     */
    private Long partyAContactsId;

    /**
     * 甲方地址id
     */
    private Long partyAAdressId;

    /**
     * 甲方电话
     */
    private Long partyAPhone;

    /**
     * 甲方委托人id
     */
    private Long partyAClientId;

    /**
     * 乙方联系人id
     */
    private Long partyBContactsId;

    /**
     * 乙方地址id
     */
    private Long partyBAdressId;

    /**
     * 乙方电话
     */
    private Long partyBPhone;

    /**
     * 乙方委托人id
     */
    private Long partyBClientId;

    /**
     * 乙方 银行Id
     */
    private Long partyBBankId;

    /**
     * 乙方开户行账号
     */
    private String partyBBankAccount;

    /**
     * 乙方法人
     */
    private String partyBLegalPerson;

    /**
     * 乙方公司电话
     */
    private String partyBCompanyPhone;

    /**
     * 乙方传真
     */
    private String partyBFax;

    /**
     * 乙方邮编
     */
    private String partyBZipcode;

    /**
     * 甲方银行Id
     */
    private Long partyABankId;

    /**
     * 甲方开户行账号
     */
    private String partyABankAccount;

    /**
     * 甲方法人
     */
    private String partyALegalPerson;

    /**
     * 甲方公司电话
     */
    private String partyACompanyPhone;

    /**
     * 甲方传真
     */
    private String partyAFax;

    /**
     * 甲方
     */
    private String partyAZipcode;

}
