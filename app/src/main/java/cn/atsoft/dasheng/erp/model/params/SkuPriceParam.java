package cn.atsoft.dasheng.erp.model.params;

import lombok.Data;
import cn.atsoft.dasheng.model.validator.BaseValidatingParam;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 商品价格设置表
 * </p>
 *
 * @author sjl
 * @since 2023-01-09
 */
@Data
@ApiModel
public class SkuPriceParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;



    /**
     * sku
     */
    @ApiModelProperty("sku")
    private Long skuId;

    /**
     * 价格
     */
    @ApiModelProperty("价格")
    private double price;


    /**
     * 状态
     */
    @ApiModelProperty("状态")
    private Integer display;

    @Override
    public String checkParam() {
        return null;
    }

}
