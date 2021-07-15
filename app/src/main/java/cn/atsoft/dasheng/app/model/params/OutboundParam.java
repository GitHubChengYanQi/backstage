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
 * 出库表
 * </p>
 *
 * @author 
 * @since 2021-07-15
 */
@Data
@ApiModel
public class OutboundParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;


    /**
     * 出库id
     */
    @ApiModelProperty("出库id")
    private Long outboundId;

    /**
     * 仓库id
     */
    @ApiModelProperty("仓库id")
    private Long stockId;

    /**
     * 出库物品
     */
    @ApiModelProperty("出库物品")
    private Long itemId;

    /**
     * 出库数量
     */
    @ApiModelProperty("出库数量")
    private Long number;

    /**
     * 出库时间
     */
    @ApiModelProperty("出库时间")
    private Date outtime;

    /**
     * 出库地点
     */
    @ApiModelProperty("出库地点")
    private Long placeId;

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
