package cn.atsoft.dasheng.uc.model.params;

import cn.atsoft.dasheng.model.validator.BaseValidatingParam;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

@Data
public class SmsCodeParam implements Serializable, BaseValidatingParam {

    @ApiModelProperty("手机号码")
    @ApiParam(required = true)
    @Pattern(regexp = "^1\\d{10}", message = "手机号码格式错误")
    @NotBlank(message = "请填写手机号")
    private String phone;

    @ApiModelProperty("短信验证码")
    @NotBlank(message = "验证码不能为空")
    private String code;
}
