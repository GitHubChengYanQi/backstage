package cn.atsoft.dasheng.sys.modular.system.model.params;

import cn.atsoft.dasheng.model.validator.BaseValidatingParam;
import lombok.Data;

import java.io.Serializable;

@Data
public class ChangePwdParam implements Serializable, BaseValidatingParam {

    private String oldPassword;

    private String newPassword;
}
