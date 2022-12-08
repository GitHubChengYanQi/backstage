package cn.atsoft.dasheng.sys.modular.system.model.params;

import cn.atsoft.dasheng.model.validator.BaseValidatingParam;
import lombok.Data;

import java.io.Serializable;
@Data
public class UserParam implements Serializable, BaseValidatingParam {

    private String userName;

    private Long userId;

}
