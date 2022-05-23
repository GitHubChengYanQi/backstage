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
public class SkuPositionViewResult implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * sku名字
     */
    @ApiModelProperty("sku名字")
    private String skuName;

    @ApiModelProperty("")
    private Long skuId;

    /**
     * 仓库库位id
     */
    @ApiModelProperty("仓库库位id")
    private Long storehousePositionsId;

    /**
     * 库位名称
     */
    @ApiModelProperty("库位名称")
    private String name;
    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
}
