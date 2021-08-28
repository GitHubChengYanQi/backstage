package cn.atsoft.dasheng.config.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

@Configuration
public class SpringSessionConfiguration {

    @Bean
    public CookieSerializer cookieSerializer() {
        DefaultCookieSerializer serializer = new DefaultCookieSerializer();
        serializer.setCookieName("JSESSIONID");
        serializer.setDomainName("localhost");
        serializer.setCookiePath("/");
        serializer.setCookieMaxAge(3600);
        serializer.setSameSite("none");  // 设置SameSite属性
        serializer.setUseHttpOnlyCookie(true);
        serializer.setUseSecureCookie(false);
        return serializer;
    }
}
