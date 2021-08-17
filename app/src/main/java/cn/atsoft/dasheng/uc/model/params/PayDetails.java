package cn.atsoft.dasheng.uc.model.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class PayDetails {

    @ApiModelProperty("币种编码")
    private  Long currencyId;

    private Integer maximum;

    private  Integer Total;
}
