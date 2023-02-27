package cn.atsoft.dasheng.inStock.model.result;

import cn.atsoft.dasheng.sys.modular.system.model.result.UserResult;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class OrderResult {
    @ApiModelProperty("编码")
    private String conding;

    @ApiModelProperty("合同id")
    private Long orderId;

    @ApiModelProperty("创建人id")
    private Long creteUser;

    @ApiModelProperty("创建人返回对象")
    private UserResult creteUserResult;

    @ApiModelProperty("主题")
    private String theme;
}
