package cn.atsoft.dasheng.purchase.model.result;

import cn.atsoft.dasheng.purchase.pojo.ThemeAndOrigin;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import lombok.Data;

import java.util.Date;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * <p>
 * 采购计划主表
 * </p>
 *
 * @author song
 * @since 2021-12-21
 */
@Data
@ApiModel
public class ProcurementPlanResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private ProcurementPlanDetalResult detalResult;

    private List<ProcurementPlanDetalResult> detalResults;

    private List<PurchaseQuotationResult> quotationResults;

    private User founder;

    private ThemeAndOrigin themeAndOrigin;

    private User user;
    /**
     * 采购计划id
     */
    @ApiModelProperty("采购计划id")
    private Long procurementPlanId;

    /**
     * 采购计划名称
     */
    @ApiModelProperty("采购计划名称")
    private String procurementPlanName;

    /**
     * 负责人
     */
    @ApiModelProperty("负责人")
    private Long userId;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remark;

    /**
     * 要求供应商等级
     */
    @ApiModelProperty("要求供应商等级")
    private Integer needLevel;

    /**
     * 非供应商物料
     */
    @ApiModelProperty("非供应商物料")
    private Integer isSpupplier;

    /**
     * 删除状态
     */
    @ApiModelProperty("删除状态")
    private Integer display;

    /**
     * 状态
     */
    @ApiModelProperty("状态")
    private Integer status;

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
     * 交付日期
     */
    @ApiModelProperty("交付日期")
    private Date deliveryDate;

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
