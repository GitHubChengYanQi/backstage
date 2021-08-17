package cn.atsoft.dasheng.uc.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "pay")
public class PayProperties {

    private UcConfig UcConfig;

    private Alipay alipay;

    private WeiXinPay weiXinPay;

    private WeiXinMiNiPay weiXinMiNiPay;

    private WeiXinAppPay weiXinAppPay;

    private String notifyUrl;

    @Data
    public static class Alipay {

        private String appId;

        private String appPublicKey;

        private String appPrivateKey;

        private String alipayPublicKey;
    }

    @Data
    public static class WeiXinPay {

        private String appId;

        private String mchId;

        private String mchKey;

    }

    @Data
    public static class WeiXinMiNiPay {

        private String appId;

        private String mchId;

        private String mchKey;

    }
    @Data
    public static class WeiXinAppPay{
        private String appId;

        private String mchId;
    }

    @Data
    public static class UcConfig {

        private String privateKeyStr;

        private String publicKeyStr;
    }
}
