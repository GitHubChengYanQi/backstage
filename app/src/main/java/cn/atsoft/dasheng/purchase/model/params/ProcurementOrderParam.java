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
    @NotNull
    private List<ProcurementOrderDetailParam> detailParams;
    /**
     * 采购单
     */
    @ApiModelProperty("采购单")
    private Long procurementOrderId;

    private Long contractId;

    private Long adressId;

    private Integer money;
    /**
     * 采购计划id
     */
    @ApiModelProperty("采购计划id")
    @NotNull
    private Long procurementPlanId;

    /**
     * 状态
     */
    @ApiModelProperty("状态")
    private Long status;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String note;

    /**
     * 主题
     */
    @ApiModelProperty("主题")
    private String theme;

    /**
     * 来源
     */
    @ApiModelProperty("来源")
    private String origin;

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
