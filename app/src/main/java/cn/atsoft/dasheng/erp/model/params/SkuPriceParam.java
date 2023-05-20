package cn.atsoft.dasheng.erp.model.params;

import cn.atsoft.dasheng.core.util.ToolUtil;
import lombok.Data;
import cn.atsoft.dasheng.model.validator.BaseValidatingParam;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
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
    private Double price;


    /**
     * 状态
     */
    @ApiModelProperty("状态")
    private Integer display;

    /**
     * 分类
     */
    @ApiModelProperty("分类")
    private Integer type;

    public Long getPrice() {
        return ToolUtil.isEmpty(price) ? 0L : BigDecimal.valueOf(price).multiply(BigDecimal.valueOf(100)).longValue();
    }

    @Override
    public String checkParam() {
        return null;
    }

}
