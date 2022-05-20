package cn.atsoft.dasheng.view.model.result;

import lombok.Data;
import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
/**
 * <p>
 * VIEW
 * </p>
 *
 * @author 
 * @since 2022-05-20
 */
@Data
@ApiModel
public class SkuBasisViewResult implements Serializable {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty("")
    private Long skuId;

    /**
     * sku名字
     */
    @ApiModelProperty("sku名字")
    private String skuName;

    /**
     * 物品Id
     */
    @ApiModelProperty("物品Id")
    private Long spuId;

    /**
     * 物品名字
     */
    @ApiModelProperty("物品名字")
    private String spuName;

    /**
     * 名称
     */
    @ApiModelProperty("名称")
    private String spuClassName;

    /**
     * spu分类id
     */
    @ApiModelProperty("spu分类id")
    private Long spuClassificationId;
    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
}
