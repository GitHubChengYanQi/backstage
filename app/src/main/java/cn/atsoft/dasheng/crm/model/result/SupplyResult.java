package cn.atsoft.dasheng.crm.model.result;

import cn.atsoft.dasheng.app.model.result.BrandResult;
import cn.atsoft.dasheng.app.model.result.CustomerResult;
import cn.atsoft.dasheng.erp.model.result.SkuResult;
import lombok.Data;

import java.util.Date;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * <p>
 * 供应商供应物料
 * </p>
 *
 * @author song
 * @since 2021-12-20
 */
@Data
@ApiModel
public class SupplyResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private CustomerResult customerResult;

    private Long stockNumber;

    private Long applyNumber;

    private Long brandId;

    private SkuResult skuResult;
    @ApiModelProperty("")
    private Long supplyId;


    private BrandResult brandResult;

    private List<BrandResult> brandResults;

    /**
     * sku
     */
    @ApiModelProperty("sku")
    private Long skuId;

    /**
     * 供应商id
     */
    @ApiModelProperty("供应商id")
    private Long customerId;

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
