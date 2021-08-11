package cn.atsoft.dasheng.appBase.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "freed-template")
public class FreedTemplateProperties {
}
