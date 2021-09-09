package cn.atsoft.dasheng.appBase.service;

import cn.atsoft.dasheng.appBase.config.WxCpProperties;
import me.chanjar.weixin.common.service.WxOAuth2Service;
import me.chanjar.weixin.cp.api.impl.WxCpServiceImpl;
import me.chanjar.weixin.cp.config.impl.WxCpDefaultConfigImpl;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

@Service
@EnableConfigurationProperties(WxCpProperties.class)
public class WxCpService  {

    private WxCpProperties config;

    public WxCpService(WxCpProperties wxCpProperties){
        this.config = wxCpProperties;
    }

    public WxCpServiceImpl getWxCpClient(){

        WxCpDefaultConfigImpl config = new WxCpDefaultConfigImpl();
        config.setCorpId(this.config.getCorpId());      // 设置微信企业号的appid
        config.setCorpSecret(this.config.getSecret());  // 设置微信企业号的app corpSecret
        config.setAgentId(this.config.getAgentId());     // 设置微信企业号应用ID
        config.setToken(this.config.getToken());       // 设置微信企业号应用的token
        config.setAesKey(this.config.getAesKey());      // 设置微信企业号应用的EncodingAESKey

        WxCpServiceImpl wxCpService = new WxCpServiceImpl();
        wxCpService.setWxCpConfigStorage(config);
        return  wxCpService;
    }
}
