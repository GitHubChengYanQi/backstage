package cn.atsoft.dasheng.audit.service;

import cn.atsoft.dasheng.audit.config.RestWxCpProperties;
import cn.atsoft.dasheng.audit.config.RestWxAppProperties;
import cn.atsoft.dasheng.audit.handler.MsgHandler;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.config.impl.WxMaDefaultConfigImpl;
import lombok.val;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.cp.api.WxCpService;
import me.chanjar.weixin.cp.api.impl.WxCpServiceImpl;
import me.chanjar.weixin.cp.config.impl.WxCpDefaultConfigImpl;
import me.chanjar.weixin.cp.message.WxCpMessageRouter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

@Service
@EnableConfigurationProperties(RestWxCpProperties.class)
public class RestWxCpService {

    @Autowired
    private MsgHandler msgHandler;

    private RestWxCpProperties config;

    private RestWxAppProperties miniAppProperties;

    public RestWxCpService(RestWxCpProperties restWxCpProperties){
        this.config = restWxCpProperties;
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
//    public WxMaServiceImpl getWxMiniAppClient(){
//        WxMaDefaultConfigImpl config = new WxMaDefaultConfigImpl();
//        config.setAppid(this.miniAppProperties.getAppid());
//        config.setSecret(this.miniAppProperties.getSecret());
//        config.setAesKey(this.miniAppProperties.getAesKey());
//        config.setMsgDataFormat(this.miniAppProperties.getMsgDataFormat());
//        config.setToken(this.miniAppProperties.getToken());
//        config.setAesKey(this.miniAppProperties.getAesKey());
//        WxMaServiceImpl wxCpService = new WxMaServiceImpl();
//        wxCpService.setWxMaConfig(config);
//        return  wxCpService;
//    }




    public  WxCpMessageRouter newRouter(WxCpService wxCpService) {
        final val newRouter = new WxCpMessageRouter(wxCpService);
        // 默认
        newRouter.rule().async(false).msgType(WxConsts.XmlMsgType.EVENT).event("sys_approval_change").handler(this.msgHandler).end();
        return newRouter;
    }
}
