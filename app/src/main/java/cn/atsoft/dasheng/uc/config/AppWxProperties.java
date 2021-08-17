package cn.atsoft.dasheng.uc.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "wx.app")
public class AppWxProperties {

    private String appId;

    private String secret;

}
