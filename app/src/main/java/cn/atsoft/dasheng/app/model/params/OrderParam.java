package cn.atsoft.dasheng.app.model.params;

import lombok.Data;
import cn.atsoft.dasheng.model.validator.BaseValidatingParam;
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
 * @since 2021-07-15
 */
@Data
@ApiModel
public class OrderParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;


    /**
     * 发货编号
     */
    @ApiModelProperty("发货编号")
    private Long orderId;

    /**
     * 出库编号
     */
    @ApiModelProperty("出库编号")
    private Long outboundId;

    /**
     * 发货人
     */
    @ApiModelProperty("发货人")
    private String consignor;



    /**
     * 收货地址
     */
    @ApiModelProperty("收货地址")
    private String shipping;


    /**
     * 发货价格
     */
    @ApiModelProperty("发货价格")
    private Integer price;

    /**
     * 物品重量
     */
    @ApiModelProperty("物品重量")
    private Long weight;

    /**
     * 物品面积
     */
    @ApiModelProperty("物品面积")
    private Long area;

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

    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;

    @Override
    public String checkParam() {
        return null;
    }

}
