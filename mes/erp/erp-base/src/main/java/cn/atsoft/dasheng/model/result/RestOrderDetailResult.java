package cn.atsoft.dasheng.model.result;

import cn.atsoft.dasheng.goods.sku.model.result.RestSkuResult;
import cn.atsoft.dasheng.goods.sku.model.result.SkuListResult;
import cn.atsoft.dasheng.sys.modular.rest.service.RestPositionService;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 订单明细表
 * </p>
 *
 * @author song
 * @since 2022-02-23
 */
@Data
@ApiModel
public class RestOrderDetailResult implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("物料库存绑定库位")
    List<Map<String,Object>> bindPositions;

    private SkuListResult skuResult;
    /**
     * 订单id
     */
    @ApiModelProperty("订单id")
    private Long orderId;
    @ApiModelProperty("订单id")
    private Long detailId;

    @ApiModelProperty("")
    private Long skuId;

    /**
     * 采购数量
     */
    @ApiModelProperty("采购数量")
    private Integer purchaseNumber;
    /**
     * 到货数量
     */
    @ApiModelProperty("到货数量")
    private Integer arrivalNumber;

    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
}
