package cn.stylefeng.guns.sys.modular.rest.model.params;

import cn.atsoft.dasheng.model.validator.BaseValidatingParam;
import lombok.Data;

import java.io.Serializable;

@Data
public class UserRoleParam implements Serializable, BaseValidatingParam {

    private Long userId;

    private String roleIds;
}
