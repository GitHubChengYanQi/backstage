package cn.atsoft.dasheng.uc.config;

import lombok.Data;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(cn.atsoft.dasheng.uc.config.AppWxProperties.class)
@Data
public class AppWxConfiguration {

    private final cn.atsoft.dasheng.uc.config.AppWxProperties appWxProperties;

    public AppWxConfiguration(cn.atsoft.dasheng.uc.config.AppWxProperties appWxProperties) {
        this.appWxProperties = appWxProperties;
    }
}
