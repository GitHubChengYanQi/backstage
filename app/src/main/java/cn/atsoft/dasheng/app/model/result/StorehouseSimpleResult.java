package cn.atsoft.dasheng.app.model.result;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class StorehouseSimpleResult {
    /**
     * 仓库id
     */

    @ApiModelProperty("仓库id")
    @JSONField(serializeUsing= ToStringSerializer.class)
    private Long storehouseId;

    /**
     * 名称
     */
    @ApiModelProperty("名称")
    private String name;
}
