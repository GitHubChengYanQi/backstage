package cn.atsoft.dasheng.production.card.model.result;

import lombok.Data;
import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
/**
 * <p>
 * 生产卡片绑定
 * </p>
 *
 * @author Captain_Jazz
 * @since 2023-03-10
 */
@Data
@ApiModel
public class ProductionCardBindResult implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 工单id
     */
    @ApiModelProperty("工单id")
    private Long productionCardBindId;

    /**
     * 生产卡片id
     */
    @ApiModelProperty("生产卡片id")
    private Long productionCardId;

    /**
     * 来源类型
     */
    @ApiModelProperty("来源类型")
    private String source;

    /**
     * 来源id
     */
    @ApiModelProperty("来源id")
    private Long sourseId;

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
     * 创建者
     */
    @ApiModelProperty(hidden = true)
    private Long createUser;

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
     * 修改时间
     */
    @ApiModelProperty(hidden = true)
    private Date updateTime;
    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
}
