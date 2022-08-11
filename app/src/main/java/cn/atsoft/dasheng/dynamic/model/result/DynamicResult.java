package cn.atsoft.dasheng.dynamic.model.result;

import lombok.Data;
import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
/**
 * <p>
 * 
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-08-10
 */
@Data
@ApiModel
public class DynamicResult implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 商机动态id
     */
    @ApiModelProperty("商机动态id")
    private Long dynamicId;

    /**
     * 用户id
     */
    @ApiModelProperty("用户id")
    private Long userId;

    /**
     * 实物id
     */
    @ApiModelProperty("实物id")
    private Long inkindId;

    /**
     * 内容
     */
    @ApiModelProperty("内容")
    private String content;

    /**
     * 更改之后数据
     */
    @ApiModelProperty("更改之后数据")
    private String after;

    @ApiModelProperty("")
    private String type;

    /**
     * 更改之前数据
     */
    @ApiModelProperty("更改之前数据")
    private String before;

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
     * 物料id
     */
    @ApiModelProperty("物料id")
    private Long skuId;

    @ApiModelProperty("")
    private Long spuId;

    /**
     * 任务id
     */
    @ApiModelProperty("任务id")
    private Long taskId;

    /**
     * 仓库id
     */
    @ApiModelProperty("仓库id")
    private Long storehouseId;

    /**
     * 库位id
     */
    @ApiModelProperty("库位id")
    private Long storehousePositionsId;

    /**
     * 创建者
     */
    @ApiModelProperty(hidden = true)
    private Long createUser;

    /**
     * 修改者
     */
    @ApiModelProperty(hidden = true)
    private Long updateUser;

    /**
     * 创建时间
     */
    @ApiModelProperty(hidden = true)
    private Date createTime;

    /**
     * 修改时间
     */
    @ApiModelProperty(hidden = true)
    private Date updateTime;

    /**
     * 状态
     */
    @ApiModelProperty("状态")
    private Integer display;

    /**
     * 部门id
     */
    @ApiModelProperty("部门id")
    private Long deptId;
    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
}
