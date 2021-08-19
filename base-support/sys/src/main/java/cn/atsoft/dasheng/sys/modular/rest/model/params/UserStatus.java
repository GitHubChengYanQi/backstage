package cn.atsoft.dasheng.sys.modular.rest.model.params;

import cn.atsoft.dasheng.model.validator.BaseValidatingParam;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Data
public class UserStatus implements Serializable, BaseValidatingParam {
    @NotEmpty
    private Long userId;
}
