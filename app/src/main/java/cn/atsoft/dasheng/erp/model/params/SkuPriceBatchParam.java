package cn.atsoft.dasheng.erp.model.params;

import cn.atsoft.dasheng.model.validator.BaseValidatingParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
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
public class SkuPriceBatchParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;

    private List<SkuPriceParam> skuPriceParamList;



}
