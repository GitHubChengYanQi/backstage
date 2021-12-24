package cn.atsoft.dasheng.purchase.model.result;

import cn.atsoft.dasheng.app.model.result.CustomerResult;
import cn.atsoft.dasheng.erp.model.result.SkuResult;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import lombok.Data;

import java.util.Date;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * <p>
 * 采购报价表单
 * </p>
 *
 * @author Captain_Jazz
 * @since 2021-12-22
 */
@Data
@ApiModel
public class PurchaseQuotationResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private SkuResult skuResult;
    private String paymentMethod;
    private Long taxPrice;
    private CustomerResult customerResult;
    private User user;

    /**
     * 采购报价id
     */
    @ApiModelProperty("采购报价id")
    private Long purchaseQuotationId;

    /**
     * 物料id
     */
    @ApiModelProperty("物料id")
    private Long skuId;

    /**
     * 价格
     */
    @ApiModelProperty("价格")
    private Long price;

    /**
     * 供应商id
     */
    @ApiModelProperty("供应商id")
    private Long customerId;

    /**
     * 报价有效期
     */
    @ApiModelProperty("报价有效期")
    private Date periodOfValidity;

    /**
     * 数量
     */
    @ApiModelProperty("数量")
    private Integer total;

    /**
     * 是否含税
     */
    @ApiModelProperty("是否含税")
    private Integer isTax;

    /**
     * 税前单价
     */
    @ApiModelProperty("税前单价")
    private Long preTax;

    /**
     * 运费价格
     */
    @ApiModelProperty("运费价格")
    private Long freight;

    /**
     * 税后单价
     */
    @ApiModelProperty("税后单价")
    private Long afterTax;

    /**
     * 是否包含运费
     */
    @ApiModelProperty("是否包含运费")
    private Integer isFreight;

    /**
     * 来源
     */
    @ApiModelProperty("来源")
    private String source;

    /**
     * 来源id
     */
    @ApiModelProperty("来源id")
    private Long sourceId;

    /**
     * 关联表id
     */
    @ApiModelProperty("关联表id")
    private Long formId;

    /**
     * 交付日期
     */
    @ApiModelProperty("交付日期")
    private Date deliveryDate;

    /**
     * 票据类型
     */
    @ApiModelProperty("票据类型")
    private String InvoiceType;

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
     * 创建用户
     */
    @ApiModelProperty(hidden = true)
    private Long createUser;

    /**
     * 修改用户
     */
    @ApiModelProperty(hidden = true)
    private Long updateUser;

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
