package cn.atsoft.dasheng.app.model.result;

import lombok.Data;
import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
/**
 * <p>
 * 发货表
 * </p>
 *
 * @author  
 * @since 2021-08-20
 */
@Data
@ApiModel
public class DeliveryResult implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 发货id
     */
    @ApiModelProperty("发货id")
    private Long deliveryId;

    /**
     * 出库单id
     */
    @ApiModelProperty("出库单id")
    private Long outstockOrderId;

    /**
     * 发货时间
     */
    @ApiModelProperty("发货时间")
    private Date outTime;

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