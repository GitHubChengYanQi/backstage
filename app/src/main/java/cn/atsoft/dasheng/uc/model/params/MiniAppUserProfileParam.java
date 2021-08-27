package cn.atsoft.dasheng.uc.model.params;

import cn.atsoft.dasheng.model.validator.BaseValidatingParam;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
public class MiniAppUserProfileParam implements Serializable, BaseValidatingParam {


    private String encryptedData;

    private String iv;
}
