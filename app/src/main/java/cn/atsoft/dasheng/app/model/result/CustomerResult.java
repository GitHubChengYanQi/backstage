package cn.atsoft.dasheng.app.model.result;

import cn.atsoft.dasheng.app.entity.Adress;
import cn.atsoft.dasheng.app.entity.Contacts;
import cn.atsoft.dasheng.app.entity.CrmCustomerLevel;
import cn.atsoft.dasheng.app.entity.Phone;
import cn.atsoft.dasheng.crm.model.result.InvoiceResult;
import cn.atsoft.dasheng.crm.model.result.SupplyResult;
import cn.atsoft.dasheng.crm.region.RegionResult;
import cn.atsoft.dasheng.erp.model.result.SkuResult;
import cn.atsoft.dasheng.sys.modular.system.model.result.UserResult;
import lombok.Data;

import java.util.Date;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

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
public class CustomerResult implements Serializable {

    private static final long serialVersionUID = 1L;
    private List<SkuResult> skuResultList; //供应物料
    private CrmCustomerLevelResult crmCustomerLevelResult;
    private OriginResult originResult;
    private UserResult userResult;
    private CrmIndustryResult crmIndustryResult;
    private List<SupplyResult> supplyResults;
    private List<ContactsResult> contactsParams;
    private List<AdressResult> adressParams;
    private String classificationName;
    private Long originId;
    private String note;
    private Long userId;
    private String avatar;
    private String emall;
    private String url;
    private Long industryId;
    private String industryName;
    private Integer agent;
    private String region;
    private String abbreviation;
    private RegionResult regionResult;
    private InvoiceResult invoiceResult;
    private List<InvoiceResult> invoiceResults;
    private Integer businessCount;
    private Integer contracrCount;
    private Integer dynamicCount;
    private Integer contactsCount;
    private Contacts contact;
    private Adress address;
    private Integer supply;
    private List<BrandResult> brandResults;
    private CrmCustomerLevel level;
    private Phone phone;
    private ContactsResult defaultContactsResult;
    private UserResult createUserResult;


    /**
     * 客户id
     */
    private Long defaultContacts;

    private Long defaultAddress;

    @ApiModelProperty("客户id")
    private Long customerId;

    /**
     * 客户名称
     */
    @ApiModelProperty("客户名称")
    private String customerName;
    @ApiModelProperty("支付信息")
    private Long invoiceId;

    /**
     * 客户地址id
     */

    private Long customerLevelId;


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

    private Integer status;
    private Integer blacklist;
    private Integer classification;

    private Long sort;

    /**
     * 成立时间
     */
    @ApiModelProperty("成立时间")
    private Date setup;

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

}
