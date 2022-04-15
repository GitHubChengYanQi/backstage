package cn.atsoft.dasheng.sys.modular.rest.model.params;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class MobileUrl {

    @Value("${mobile.url}")
    public String url;

    public static String prefix;

    @Bean
    private void getUrl() {
        prefix = url;
    }

}
