package cn.atsoft.dasheng.uc.jwt;

import lombok.Data;

@Data
public class OpenId {

    private String SHANYAN;  // 闪验登录

    private String mpOpenId; // 公众号登录

    private String WECHAT_OPEN; // 微信 App方式登录

    private String WxMiniOpenId; // 微信小程序登录

    private String AppleUID; // 苹果账号登录
}
