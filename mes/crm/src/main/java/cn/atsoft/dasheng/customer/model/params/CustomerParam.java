package cn.atsoft.dasheng.customer.model.params;

import cn.atsoft.dasheng.model.validator.BaseValidatingParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 客户管理表
 * </p>
 *
 * @author
 * @since 2021-07-23
 */
@Data
@ApiModel
public class CustomerParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;

//    private List<ContactsParam> contactsParams;
//    private List<PurchaseQuotationParam> quotationParams;
//    private List<AdressParam> adressParams;
//    private List<SupplyParam> supplyParams;
//    private List<InvoiceParam> invoiceParams;
    private Long customerLevelId;
    private Integer status;
    private Long originId;
    private Integer agent;
    private String note;
    private String avatar;
    private Integer classification;
    private String classificationName;
    private String region;
    private Long sort;
    private Long userId;
    private String emall;
    private String url;
    private Long rank;
    private String originName;
    private String abbreviation;
    private String levelName;
    private Long industryId;
    private String industryName;
    private Integer supply;
    private String userName;
    private Long defaultContacts;
    private Long defaultAddress;
    private Long invoiceId;


    /**
     * 客户id
     */
    @ApiModelProperty("客户id")
    private Long customerId;

    @ApiModelProperty("黑名单")
    private Integer blacklist;

    /**
     * 客户名称
     */
    @ApiModelProperty("客户名称")
    private String customerName;

    /**
     * 客户地址id
     */

    /**
     * 联系人id
     */


    /**
     * 成立时间
     */
    @ApiModelProperty("成立时间")
    private Date setup;


    /**
     * 注册资本
     */
    @ApiModelProperty("注册资本")
    private String registeredCapital;

    /**
     * 传真
     */
    @ApiModelProperty("传真")
    private String fax;

    /**
     * 企业电话
     */
    @ApiModelProperty("企业电话")
    private String telephone;

    /**
     * 邮编
     */
    @ApiModelProperty("邮编")
    private String zipCode;

    /**
     * 法定代表人
     */
    @ApiModelProperty("法定代表人")
    private String legal;

    /**
     * 统一社会信用代码
     */
    @ApiModelProperty("统一社会信用代码")
    private String utscc;

    /**
     * 公司类型
     */
    @ApiModelProperty("公司类型")
    private String companyType;

    /**
     * 营业期限
     */
    @ApiModelProperty("营业期限")
    private Date businessTerm;

    /**
     * 注册地址
     */
    @ApiModelProperty("注册地址")
    private String signIn;

    /**
     * 简介
     */
    @ApiModelProperty("简介")
    private String introduction;

    /**
     * 创建者
     */
    @ApiModelProperty(hidden = true)
    private Long createUser;

    /**
     * 修改者
     */
    @ApiModelProperty(hidden = true)
    private Long updateUser;

    /**
     * 创建时间
     */
    @ApiModelProperty(hidden = true)
    private Date createTime;

    /**
     * 修改时间
     */
    @ApiModelProperty(hidden = true)
    private Date updateTime;

    /**
     * 状态
     */
    @ApiModelProperty("状态")
    private Integer display;

    /**
     * 部门编号
     */
    @ApiModelProperty("部门编号")
    private Long deptId;

    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;

    @Override
    public String checkParam() {
        return null;
    }

}
