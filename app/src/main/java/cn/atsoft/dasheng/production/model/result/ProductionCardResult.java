package cn.atsoft.dasheng.production.model.result;

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
 * @author 
 * @since 2022-02-28
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
