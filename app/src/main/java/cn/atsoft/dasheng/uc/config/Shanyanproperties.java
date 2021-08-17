package cn.atsoft.dasheng.uc.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "chuanglan.shanyan")
public class Shanyanproperties {

    private config android;

    private config ios;

    @Data
    public static class config {

        private String appid;

        private String appKey;
    }
}
