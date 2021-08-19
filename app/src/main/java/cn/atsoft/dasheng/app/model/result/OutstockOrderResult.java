package cn.atsoft.dasheng.app.model.result;

import lombok.Data;
import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
/**
 * <p>
 * 出库单
 * </p>
 *
 * @author cheng
 * @since 2021-08-16
 */
@Data
@ApiModel
public class OutstockOrderResult implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 出库详细id
     */
    @ApiModelProperty("出库详细id")
    private Long outstockOrderId;

    /**
     * 出库状态
     */
    @ApiModelProperty("出库状态")
    private Integer state;

    /**
     * 计划出库时间
     */
    @ApiModelProperty("计划出库时间")
    private Date time;

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
    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
}