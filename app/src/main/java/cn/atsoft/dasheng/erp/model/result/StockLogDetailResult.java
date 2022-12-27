package cn.atsoft.dasheng.erp.model.result;

import lombok.Data;
import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
/**
 * <p>
 * 库存操作记录子表
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-12-22
 */
@Data
@ApiModel
public class StockLogDetailResult implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 库存操作记录详情
     */
    @ApiModelProperty("库存操作记录详情")
    private Long stockLogDetailId;

    /**
     * 库存操作记录
     */
    @ApiModelProperty("库存操作记录")
    private Long stockLogId;

    /**
     * 实物id
     */
    @ApiModelProperty("实物id")
    private Long inkindId;

    /**
     * 数量(实物角度)
     */
    @ApiModelProperty("数量(实物角度)")
    private Integer number;

    /**
     * 操作之前的库存数量(实物角度)
     */
    @ApiModelProperty("操作之前的库存数量(实物角度)")
    private Integer beforeNumber;

    /**
     * 操作之后数量(实物角度)
     */
    @ApiModelProperty("操作之后数量(实物角度)")
    private Integer afterNumber;

    /**
     * 地点id
     */
    @ApiModelProperty("地点id")
    private Long storehouseId;

    /**
     * 仓库库位id
     */
    @ApiModelProperty("仓库库位id")
    private Long storehousePositionsId;

    /**
     * 类型(increase增加/dwindle减少)
     */
    @ApiModelProperty("类型(increase增加/dwindle减少)")
    private String type;

    @ApiModelProperty("")
    private String source;

    @ApiModelProperty("")
    private Long sourceId;

    @ApiModelProperty(hidden = true)
    private Date createTime;

    @ApiModelProperty(hidden = true)
    private Long createUser;

    @ApiModelProperty(hidden = true)
    private Date updateTime;

    @ApiModelProperty(hidden = true)
    private Long updateUser;

    @ApiModelProperty("")
    private Integer display;

    /**
     * 部门编号
     */
    @ApiModelProperty("部门编号")
    private Long deptId;
    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
}
