package cn.atsoft.dasheng.media.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "aliyun")
public class RestAliConfiguration {

    private String accessId;

    private String accessKey;

    private OSS oss;

    private OSS oss2;

    private Sms sms;

    @Data
    public static class OSS{

        private String endpoint;

        private String bucket;

        private String callbackUrl;

        private String path;
    }

    @Data
    public static class Sms{

        private String signName;

        private String templateCode;
    }
}
