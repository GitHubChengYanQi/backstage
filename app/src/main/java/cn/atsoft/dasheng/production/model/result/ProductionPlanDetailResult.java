package cn.atsoft.dasheng.production.model.result;

import cn.atsoft.dasheng.app.model.result.PartsResult;
import cn.atsoft.dasheng.erp.model.result.SkuResult;
import lombok.Data;

import java.util.Date;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.servlet.http.Part;
import java.util.List;

/**
 * <p>
 * 生产计划子表
 * </p>
 *
 * @author
 * @since 2022-02-25
 */
@Data
@ApiModel
public class ProductionPlanDetailResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private PartsResult partsResult;

    private SkuResult skuResult;

    private ProcessRouteResult processRouteResult;
    /**
     * 生产计划子表id
     */
    @ApiModelProperty("生产计划子表id")
    private Long productionPlanDetailId;

    private Long stockNumber;



    /**
     * orderDetailId
     */
    @ApiModelProperty("orderDetailId")
    private Long orderDetailId;


    /**
     * 生产计划id
     */
    @ApiModelProperty("生产计划id")
    private Long productionPlanId;

    /**
     * 产品id
     */
    @ApiModelProperty("产品id")
    private Long skuId;

    /**
     * 计划数量
     */
    private Integer planNumber;
    /**
     * 生产中的数量
     */
    private Integer makingNumber;

    /**
     * 交付时间
     */
    @ApiModelProperty("交付时间")
    private Integer deliveryDate;

    /**
     * 创建者
     */
    @ApiModelProperty(hidden = true)
    private Long createUser;

    /**
     * 部门编号
     */
    @ApiModelProperty("部门编号")
    private Long deptId;

    /**
     * 状态
     */
    @ApiModelProperty("状态")
    private Integer display;

    /**
     * 修改时间
     */
    @ApiModelProperty(hidden = true)
    private Date updateTime;

    /**
     * 创建时间
     */
    @ApiModelProperty(hidden = true)
    private Date createTime;

    /**
     * 修改者
     */
    @ApiModelProperty(hidden = true)
    private Long updateUser;

    /**
     * 合同编号
     */
    @ApiModelProperty("合同编号")
    private Long contractId;

    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
}
