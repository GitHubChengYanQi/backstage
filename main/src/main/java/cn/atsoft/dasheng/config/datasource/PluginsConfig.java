package cn.atsoft.dasheng.config.datasource;

import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.core.metadata.CustomMetaObjectHandler;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * mp的插件拓展和资源扫描
 */
@Configuration
@MapperScan(basePackages = {"cn.atsoft.**.mapper"})
public class PluginsConfig {

    /**
     * 拓展核心包中的字段包装器
     */
    @Bean
    public CustomMetaObjectHandler gunsMpFieldHandler() {
        return new CustomMetaObjectHandler() {

            @Override
            protected Long getUserUniqueId() {
                try {
                    return LoginContextHolder.getContext().getUser().getId();
                } catch (Exception e) {

                    //如果获取不到当前用户就存空id
                    return -100L;
                }
            }
        };
    }

}
