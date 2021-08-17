package cn.atsoft.dasheng.uc.model.params;

import lombok.Data;

@Data
public class AppleIdentity {

    private String iss;

    private String sub;

    private String aud;

    private String email;
}
