package cn.atsoft.dasheng.purchase.model.params;

import lombok.Data;
import cn.atsoft.dasheng.model.validator.BaseValidatingParam;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 采购单
 * </p>
 *
 * @author song
 * @since 2022-01-13
 */
@Data
@ApiModel
public class ProcurementOrderParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;

    private List<ProcurementOrderDetailParam> detailParams;
    /**
     * 采购单
     */
    @ApiModelProperty("采购单")
    private Long procurementOrderId;

    /**
     * 采购计划id
     */
    @ApiModelProperty("采购计划id")
    private Long procurementPlanId;

    /**
     * 状态
     */
    @ApiModelProperty("状态")
    private Integer status;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String note;

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

    /**
     * 采购计划 详情
     */
    @ApiModelProperty("采购计划 详情")
    private Long detailId;

    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;

    @Override
    public String checkParam() {
        return null;
    }

}
