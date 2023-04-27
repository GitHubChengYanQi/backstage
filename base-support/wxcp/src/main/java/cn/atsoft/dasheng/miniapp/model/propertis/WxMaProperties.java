package cn.atsoft.dasheng.miniapp.model.propertis;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "wx")
@Data
public class WxMaProperties {

    private MiniApp miniapp;

    public MiniApp getMiniapp() {
        return miniapp;
    }

    public void setMiniapp(MiniApp miniapp) {
        this.miniapp = miniapp;
    }

    public static class MiniApp {
        private String appid;
        private String secret;
        private String aesKey;
        private String token;
        private String msgDataFormat;

        // getters and setters

        public String getAppid() {
            return appid;
        }

        public void setAppid(String appid) {
            this.appid = appid;
        }

        public String getSecret() {
            return secret;
        }

        public void setSecret(String secret) {
            this.secret = secret;
        }

        public String getAesKey() {
            return aesKey;
        }

        public void setAesKey(String aesKey) {
            this.aesKey = aesKey;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getMsgDataFormat() {
            return msgDataFormat;
        }

        public void setMsgDataFormat(String msgDataFormat) {
            this.msgDataFormat = msgDataFormat;
        }
    }
}
