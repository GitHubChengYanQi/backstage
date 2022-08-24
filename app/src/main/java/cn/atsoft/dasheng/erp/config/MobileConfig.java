package cn.atsoft.dasheng.erp.config;

import cn.atsoft.dasheng.appBase.config.WxCpProperties;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;


@ConfigurationProperties(prefix = "mobile")
@Data
public class MobileConfig {

    private String url;
    private String apiUrl;

}
