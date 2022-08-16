package cn.atsoft.dasheng.core.config.api.version;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.validation.constraints.NotNull;

@Configuration
@RequiredArgsConstructor
//public class CustomWebMvcConfiguration extends WebMvcConfigurationSupport {
public class CustomWebMvcConfiguration implements WebMvcConfigurer, WebMvcRegistrations {

    @Override
    @NotNull
    public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
        return new ApiVersionRequestMappingHandlerMapping();
    }
}