package cn.atsoft.dasheng.app.model.result;

import cn.atsoft.dasheng.crm.region.RegionResult;
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

    private CrmCustomerLevelResult crmCustomerLevelResult;
    private OriginResult originResult;
    private UserResult userResult;
    private CrmIndustryResult crmIndustryResult;
    //    private  ContactsResult contactsResult;
    private List<ContactsResult> contactsParams;
    private List<AdressResult> adressParams;
    private String classificationName;
    private Long originId;
    private String note;
    private Long userId;
    private String emall;
    private String url;
    private Long industryId;
    private String industryName;
    private Integer agent;
    private String region;
    private RegionResult regionResult;
    /**
     * 客户id
     */


    @ApiModelProperty("客户id")
    private Long customerId;

    /**
     * 客户名称
     */
    @ApiModelProperty("客户名称")
    private String customerName;

    /**
     * 客户地址id
     */

    private Long customerLevelId;

    private Integer status;
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
