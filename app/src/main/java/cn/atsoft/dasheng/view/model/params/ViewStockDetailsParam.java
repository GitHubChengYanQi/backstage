package cn.atsoft.dasheng.view.model.params;

import lombok.Data;
import cn.atsoft.dasheng.model.validator.BaseValidatingParam;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * VIEW
 * </p>
 *
 * @author
 * @since 2022-01-27
 */
@Data
@ApiModel
public class ViewStockDetailsParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty("")
    private Long skuId;

    private String type;

    private Long partId;

    private List<Long> skuIds;

    private Date createTime;

    /**
     * 物品Id
     */
    @ApiModelProperty("物品Id")
    private Long spuId;

    /**
     * spu分类id
     */
    @ApiModelProperty("spu分类id")
    private Long spuClassificationId;

    /**
     * 名称
     */
    @ApiModelProperty("名称")
    private String className;

    /**
     * 数量
     */
    @ApiModelProperty("数量")
    private Long number;

    /**
     * 仓库库位id
     */
    @ApiModelProperty("仓库库位id")
    private Long storehousePositionsId;

    /**
     * 物品名字
     */
    @ApiModelProperty("物品名字")
    private String spuName;

    /**
     * 地点id
     */
    @ApiModelProperty("地点id")
    private Long storehouseId;

    /**
     * 仓库id
     */
    @ApiModelProperty("仓库id")
    private Long stockId;

    /**
     * 品牌id
     */
    @ApiModelProperty("品牌id")
    private Long brandId;

    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;

    @Override
    public String checkParam() {
        return null;
    }

}
