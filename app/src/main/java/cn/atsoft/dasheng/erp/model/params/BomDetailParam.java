package cn.atsoft.dasheng.erp.model.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class BomDetailParam {


    @ApiModelProperty("skuId")
    private Long skuId;

    @ApiModelProperty("versionBomId")
    private Long versionBomId;

    @ApiModelProperty("number")
    private Integer number;
}
