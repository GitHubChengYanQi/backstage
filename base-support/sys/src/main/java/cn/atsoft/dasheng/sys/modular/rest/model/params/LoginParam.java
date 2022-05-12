package cn.atsoft.dasheng.sys.modular.rest.model.params;

import cn.atsoft.dasheng.model.validator.BaseValidatingParam;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Data
public class LoginParam implements Serializable, BaseValidatingParam {

    @NotBlank
    private String UserName;

    @NotEmpty
    private String Password;

    private String kaptcha;
}
