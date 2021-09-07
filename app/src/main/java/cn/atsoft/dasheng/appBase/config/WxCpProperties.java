package cn.atsoft.dasheng.appBase.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "wxcp")
@Data
public class WxCpProperties {

    private String corpId;

    private String secret;

    private Integer agentId;

    private String token;

    private String aesKey;
}
