package cn.atsoft.dasheng.bom.model.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class RestBomDetailParam {


    @ApiModelProperty("skuId")
    private Long skuId;

    @ApiModelProperty("versionBomId")
    private Long versionBomId;

    @ApiModelProperty("version")
    private String version;

    @ApiModelProperty("number")
    private Integer number;

    @ApiModelProperty("autoOutstock")
    private Integer autoOutstock;
}
