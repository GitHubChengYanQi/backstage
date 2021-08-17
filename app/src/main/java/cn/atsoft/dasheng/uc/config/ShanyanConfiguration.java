package cn.atsoft.dasheng.uc.config;

import lombok.Data;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(Shanyanproperties.class)
@Data
public class ShanyanConfiguration {
    private final Shanyanproperties shanyanproperties;

    public ShanyanConfiguration(Shanyanproperties shanyanproperties) {
        this.shanyanproperties = shanyanproperties;
    }
}
