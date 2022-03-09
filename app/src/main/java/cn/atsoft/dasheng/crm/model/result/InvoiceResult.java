package cn.atsoft.dasheng.crm.model.result;

import lombok.Data;

import java.util.Date;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * <p>
 * 供应商开票
 * </p>
 *
 * @author song
 * @since 2021-12-20
 */
@Data
@ApiModel
public class InvoiceResult implements Serializable {

    private static final long serialVersionUID = 1L;
    private BankResult bankResult;


    /**
     * 开票
     */
    @ApiModelProperty("开票")
    private Long invoiceId;

    /**
     * 银行
     */
    private Long bankId;

    /**
     * 客户id
     */
    @ApiModelProperty("客户id")
    private Long customerId;

    /**
     * 开户行名称
     */
    @ApiModelProperty("开户行名称")
    private String bank;

    /**
     * 开户行账号
     */
    @ApiModelProperty("开户行账号")
    private String bankAccount;

    /**
     * 统一社会信用代码
     */
    @ApiModelProperty("统一社会信用代码")
    private String creditCode;

    /**
     * 邮箱
     */
    @ApiModelProperty("邮箱")
    private String email;

    /**
     * 是否默认
     */
    @ApiModelProperty("是否默认")
    private Integer isDefault;


    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String invoiceNote;

    /**
     * 开户账号
     */
    @ApiModelProperty("开户账号")
    private Integer bankNo;


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
    private Long display;

    /**
     * 部门id
     */
    @ApiModelProperty("部门id")
    private Long deptId;
    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
}
