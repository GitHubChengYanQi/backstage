package cn.atsoft.dasheng.sys.modular.rest.model.params;

import cn.atsoft.dasheng.model.validator.BaseValidatingParam;
import lombok.Data;

import java.io.Serializable;

@Data
public class RoleParam implements Serializable, BaseValidatingParam {

    private Long roleId;

}
