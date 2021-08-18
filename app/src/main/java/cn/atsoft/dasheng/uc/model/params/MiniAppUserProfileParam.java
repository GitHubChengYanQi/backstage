package cn.atsoft.dasheng.uc.model.params;

import cn.atsoft.dasheng.portal.banner.model.validator.BaseValidatingParam;
import lombok.Data;

import java.io.Serializable;

@Data
public class MiniAppUserProfileParam implements Serializable, BaseValidatingParam {

    private String encryptedData;

    private String iv;
}
