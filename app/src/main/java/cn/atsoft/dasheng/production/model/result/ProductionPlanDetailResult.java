package cn.atsoft.dasheng.production.model.result;

import lombok.Data;
import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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


    /**
     * 生产计划子表id
     */
    @ApiModelProperty("生产计划子表id")
    private Long productionPlanDetailId;

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
     * 数量
     */
    @ApiModelProperty("数量")
    private Integer number;

    /**
     * 交付时间
     */
    @ApiModelProperty("交付时间")
    private Date deliveryDate;

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
    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
}
