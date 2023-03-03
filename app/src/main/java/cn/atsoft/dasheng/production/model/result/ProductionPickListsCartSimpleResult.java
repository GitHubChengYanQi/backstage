package cn.atsoft.dasheng.production.model.result;

import cn.atsoft.dasheng.app.model.result.BrandResult;
import cn.atsoft.dasheng.app.model.result.StorehouseResult;
import cn.atsoft.dasheng.app.model.result.StorehouseSimpleResult;
import cn.atsoft.dasheng.erp.model.result.SkuSimpleResult;
import cn.atsoft.dasheng.erp.model.result.StorehousePositionsResult;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 领料单详情表
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-03-25
 */
@Data
@ApiModel
public class ProductionPickListsCartSimpleResult implements Serializable {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty("购物车id")
    private Long pickListsCart;
    @JSONField(serializeUsing= ToStringSerializer.class)
    private Long inkindId;
    @ApiModelProperty("品牌id")
    private Long brandId;
    private Integer status;
    private Long storehouseId;
    private StorehouseResult storehouseResult;
    /**
     * 物料id
     */
    @ApiModelProperty("物料id")
    @JSONField(serializeUsing= ToStringSerializer.class)
    private Long skuId;

    @ApiModelProperty("")
    private Integer number;


}
