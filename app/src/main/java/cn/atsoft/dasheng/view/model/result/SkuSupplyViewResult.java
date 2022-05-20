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
public class SkuSupplyViewResult implements Serializable {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty("")
    private Long skuId;

    /**
     * 客户id
     */
    @ApiModelProperty("客户id")
    private Long customerId;

    /**
     * 客户名称
     */
    @ApiModelProperty("客户名称")
    private String customerName;

    /**
     * 品牌id
     */
    @ApiModelProperty("品牌id")
    private Long brandId;

    /**
     * 品牌名称
     */
    @ApiModelProperty("品牌名称")
    private String brandName;
    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
}
