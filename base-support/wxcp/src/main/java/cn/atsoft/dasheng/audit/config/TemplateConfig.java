package cn.atsoft.dasheng.audit.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
@Data
@Component
public class TemplateConfig {
    @Value("${templateId}")
    private String templateId;
}
