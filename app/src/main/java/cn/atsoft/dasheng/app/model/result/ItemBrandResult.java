package cn.atsoft.dasheng.app.model.result;

import lombok.Data;
import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
/**
 * <p>
 * 商品品牌绑定表
 * </p>
 *
 * @author 
 * @since 2021-09-23
 */
@Data
@ApiModel
public class ItemBrandResult implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 商品id
     */
    @ApiModelProperty("商品id")
    private Long itemId;

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
