package cn.atsoft.dasheng.app.model.result;

import lombok.Data;
import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
/**
 * <p>
 * 入库表
 * </p>
 *
 * @author song
 * @since 2021-07-17
 */
@Data
@ApiModel
public class InstockResult implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 物品编号
     */
    @ApiModelProperty("物品编号")
    private Long instockId;

    /**
     * 物品名称
     */
    @ApiModelProperty("物品名称")
    private Long itemId;

    /**
     * 登记时间
     */
    @ApiModelProperty("登记时间")
    private Date registerTime;

    /**
     * 入库数量
     */
    @ApiModelProperty("入库数量")
    private Long number;

    /**
     * 价格
     */
    @ApiModelProperty("价格")
    private Integer price;

    /**
     * 品牌
     */
    @ApiModelProperty("品牌")
    private String brandId;

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
}
