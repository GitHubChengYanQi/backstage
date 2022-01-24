package cn.atsoft.dasheng.purchase.model.result;

import cn.atsoft.dasheng.app.model.result.BrandResult;
import cn.atsoft.dasheng.erp.model.result.SkuResult;
import lombok.Data;

import java.util.Date;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * <p>
 * 采购计划单子表  整合数据后的 子表
 * </p>
 *
 * @author song
 * @since 2021-12-21
 */
@Data
@ApiModel
public class ProcurementPlanDetalResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private SkuResult skuResult;

    private ProcurementPlanBindResult planBindResult;

    private BrandResult brandResult;

    @ApiModelProperty("")
    private Long detailId;
    private Integer money;


    private Integer status;

    @ApiModelProperty("")
    private Long planId;

    @ApiModelProperty("")
    private Long skuId;

    private Long brandId;

    /**
     * 数量
     */
    @ApiModelProperty("数量")
    private Integer total;

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
    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
}
