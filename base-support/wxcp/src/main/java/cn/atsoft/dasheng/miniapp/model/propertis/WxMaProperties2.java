package cn.atsoft.dasheng.miniapp.model.propertis;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "wx.miniapp2")
@Data
public class WxMaProperties2 {
    private String appid;
    private String secret;
    private String token;
//    private String msgDataFormat;
    private String aesKey;


//    public WxMaProperties2(String appid, String secret, String token, String msgDataFormat, String aesKey) {
//        this.appid = appid;
//        this.secret = secret;
//        this.token = token;
//        this.msgDataFormat = msgDataFormat;
//        this.aesKey = aesKey;
//    }
}
