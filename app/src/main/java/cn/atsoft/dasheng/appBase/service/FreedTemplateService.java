package cn.atsoft.dasheng.appBase.service;

import cn.atsoft.dasheng.appBase.config.FreedTemplateProperties;
import lombok.Data;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

@Service
@EnableConfigurationProperties(FreedTemplateProperties.class)
@Data
public class FreedTemplateService {

    private FreedTemplateProperties config;

    public FreedTemplateService(FreedTemplateProperties config){
        this.config = config;
    }
}
