package cn.atsoft.dasheng.core.config.api.version;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.validation.constraints.NotNull;

/**
 * 应该是没用了
 * 统一放在了 main 包内启用
 */
@Configuration
@RequiredArgsConstructor
//public class CustomWebMvcConfiguration extends WebMvcConfigurationSupport {
public class CustomWebMvcConfiguration implements WebMvcConfigurer, WebMvcRegistrations {


}