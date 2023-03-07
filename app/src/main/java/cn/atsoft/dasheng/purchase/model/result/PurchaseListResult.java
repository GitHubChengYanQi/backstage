package cn.atsoft.dasheng.purchase.model.result;

import cn.atsoft.dasheng.app.model.result.BrandResult;
import cn.atsoft.dasheng.app.model.result.CustomerResult;
import cn.atsoft.dasheng.erp.model.result.SkuListResult;
import lombok.Data;
import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
/**
 * <p>
 * 预购管理
 * </p>
 *
 * @author Captain_Jazz
 * @since 2023-03-04
 */
@Data
@ApiModel
public class PurchaseListResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private CustomerResult customerResult;
    private List<CustomerResult> bindCustomerResultList;

    private BrandResult brandResult;

    private SkuListResult skuResult;

    @ApiModelProperty("")
    private Long purchaseListId;

    @ApiModelProperty("")
    private Long skuId;

    /**
     * 品牌id
     */
    @ApiModelProperty("品牌id")
    private Long brandId;

    /**
     * 供应商id
     */
    @ApiModelProperty("供应商id")
    private Long customerId;

    /**
     * 数量
     */
    @ApiModelProperty("数量")
    private Integer number;

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

    /**
     * 状态 创建采购计划 98  采购计划完成99
     */
    @ApiModelProperty("状态 创建采购计划 98  采购计划完成99")
    private Integer status;
    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
}
