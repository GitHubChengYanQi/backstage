package cn.stylefeng.guns.sys.modular.rest.model.params;

import cn.atsoft.dasheng.model.validator.BaseValidatingParam;
import lombok.Data;

import java.io.Serializable;

@Data
public class RoleSetParam implements Serializable, BaseValidatingParam {
    private Long roleId;

    private String ids;
}
