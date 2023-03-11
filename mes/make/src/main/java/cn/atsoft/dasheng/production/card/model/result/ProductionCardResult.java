package cn.atsoft.dasheng.production.card.model.result;

import lombok.Data;
import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
/**
 * <p>
 * 生产卡片
 * </p>
 *
 * @author Captain_Jazz
 * @since 2023-03-10
 */
@Data
@ApiModel
public class ProductionCardResult implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 生产卡片id
     */
    @ApiModelProperty("生产卡片id")
    private Long productionCardId;

    /**
     * 工单id
     */
    @ApiModelProperty("工单id")
    private Long workOrderId;

    /**
     * 物料id
     */
    @ApiModelProperty("物料id")
    private Long skuId;

    /**
     * 生产计划id
     */
    @ApiModelProperty("生产计划id")
    private Long productionPlanId;

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
     * 主题
     */
    @ApiModelProperty("主题")
    private String theme;

    /**
     * 来源Json字符串
     */
    @ApiModelProperty("来源Json字符串")
    private String origin;

    /**
     * 卡片编码
     */
    @ApiModelProperty("卡片编码")
    private String cardCoding;

    /**
     * 机床编号
     */
    @ApiModelProperty("机床编号")
    private String machineCoding;
    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
}
