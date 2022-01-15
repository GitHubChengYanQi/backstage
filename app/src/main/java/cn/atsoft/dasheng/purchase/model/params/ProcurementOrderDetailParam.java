package cn.atsoft.dasheng.purchase.model.params;

import lombok.Data;
import cn.atsoft.dasheng.model.validator.BaseValidatingParam;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author song
 * @since 2022-01-13
 */
@Data
@ApiModel
public class ProcurementOrderDetailParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;

    private Long planDetailId;


    private Integer status;
    /**
     * 采购单详情id
     */
    @ApiModelProperty("采购单详情id")
    private Long orderDetailId;

    @ApiModelProperty("")
    private Long skuId;

    /**
     * 采购计划 详情
     */
    @ApiModelProperty("采购计划 详情")
    @NotNull
    private Long detailId;

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
    private Long number;

    /**
     * 采购单id
     */
    @ApiModelProperty("采购单id")
    private Long procurementOrderId;

    /**
     * 删除状态
     */
    @ApiModelProperty("删除状态")
    private Integer display;

    @ApiModelProperty(hidden = true)
    private Long createUser;

    @ApiModelProperty(hidden = true)
    private Long updateUser;

    @ApiModelProperty(hidden = true)
    private Date createTime;

    @ApiModelProperty(hidden = true)
    private Date updateTime;

    /**
     * 部门id
     */
    @ApiModelProperty("部门id")
    private Long deptId;

    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;

    @Override
    public String checkParam() {
        return null;
    }

}
