package cn.atsoft.dasheng.audit.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "miniapp")
public class RestWxAppProperties {
    private String appid;
    private String secret;
    private String aesKey;
    private String token;
    private String msgDataFormat;
}
