package cn.atsoft.dasheng.outStock.model.result;

//import cn.atsoft.dasheng.app.model.result.StorehouseResult;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

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
public class RestOutStockCartSimpleResult implements Serializable {

    private static final long serialVersionUID = 1L;


    @JSONField(serializeUsing= ToStringSerializer.class)
    private Long inkindId;

    private Long storehouseId;
//    private StorehouseResult storehouseResult;
    /**
     * 物料id
     */
    @ApiModelProperty("物料id")
    @JSONField(serializeUsing= ToStringSerializer.class)
    private Long skuId;

    @ApiModelProperty("")
    private Integer number;


}
