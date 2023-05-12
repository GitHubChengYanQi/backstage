package cn.atsoft.dasheng.miniapp.config;

import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.miniapp.model.propertis.WxMaProperties;
import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.bean.WxMaKefuMessage;
import cn.binarywang.wx.miniapp.bean.WxMaSubscribeMessage;
import cn.binarywang.wx.miniapp.config.impl.WxMaDefaultConfigImpl;
import cn.binarywang.wx.miniapp.message.WxMaMessageHandler;
import cn.binarywang.wx.miniapp.message.WxMaMessageRouter;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.result.WxMediaUploadResult;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.error.WxRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author <a href="https://github.com/binarywang">Binary Wang</a>
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(WxMaProperties.class)
public class WxMaConfiguration {
    private final WxMaProperties properties;

    @Autowired
    public WxMaConfiguration(WxMaProperties properties) {
        this.properties = properties;
    }

    @Bean
    public WxMaService wxMaService() {
        if (ToolUtil.isEmpty(properties) || ToolUtil.isEmpty(properties.getMiniapp())) {
            throw new WxRuntimeException("小程序配置错误");
        }
        WxMaService maService = new WxMaServiceImpl();

        WxMaDefaultConfigImpl config = new WxMaDefaultConfigImpl();
        config.setAppid(properties.getMiniapp().getAppid());
        config.setSecret(properties.getMiniapp().getSecret());
        config.setToken(properties.getMiniapp().getToken());
        config.setAesKey(properties.getMiniapp().getAesKey());
        config.setMsgDataFormat(properties.getMiniapp().getMsgDataFormat());
        maService.setWxMaConfig(config);
        return maService;
    }

    @Bean
    public WxMaMessageRouter wxMaMessageRouter(WxMaService wxMaService) {
        final WxMaMessageRouter router = new WxMaMessageRouter(wxMaService);
        router
                .rule().handler(logHandler).next()
                .rule().async(false).event("subscribe_msg_popup_event").handler(subscribeMsgHandler).end()
                .rule().async(false).content("文本").handler(textHandler).end()
                .rule().async(false).content("图片").handler(picHandler).end()
                .rule().async(false).content("二维码").handler(qrcodeHandler).end();
        return router;
    }

    private final WxMaMessageHandler subscribeMsgHandler = (wxMessage, context, service, sessionManager) -> {
        service.getMsgService().sendSubscribeMsg(WxMaSubscribeMessage.builder()
                .templateId("jMpkYtYnxBlAYBpQcUIBRB7Arwi5jvOKkrR2ICoLYTM")
                .data(Lists.newArrayList(
                        new WxMaSubscribeMessage.MsgData("keyword1", "339208499")))
                .toUser(wxMessage.getFromUser())
                .build());
        return null;
    };

    private final WxMaMessageHandler logHandler = (wxMessage, context, service, sessionManager) -> {
        log.info("收到消息：" + wxMessage.toString());
        service.getMsgService().sendKefuMsg(WxMaKefuMessage.newTextBuilder().content("收到信息为：" + wxMessage.toJson())
                .toUser(wxMessage.getFromUser()).build());
        return null;
    };

    private final WxMaMessageHandler textHandler = (wxMessage, context, service, sessionManager) -> {
        service.getMsgService().sendKefuMsg(WxMaKefuMessage.newTextBuilder().content("回复文本消息")
                .toUser(wxMessage.getFromUser()).build());
        return null;
    };

    private final WxMaMessageHandler picHandler = (wxMessage, context, service, sessionManager) -> {
        try {
            WxMediaUploadResult uploadResult = service.getMediaService()
                    .uploadMedia("image", "png",
                            ClassLoader.getSystemResourceAsStream("tmp.png"));
            service.getMsgService().sendKefuMsg(
                    WxMaKefuMessage
                            .newImageBuilder()
                            .mediaId(uploadResult.getMediaId())
                            .toUser(wxMessage.getFromUser())
                            .build());
        } catch (WxErrorException e) {
            e.printStackTrace();
        }

        return null;
    };

    private final WxMaMessageHandler qrcodeHandler = (wxMessage, context, service, sessionManager) -> {
        try {
            final File file = service.getQrcodeService().createQrcode("123", 430);
            WxMediaUploadResult uploadResult = service.getMediaService().uploadMedia("image", file);
            service.getMsgService().sendKefuMsg(
                    WxMaKefuMessage
                            .newImageBuilder()
                            .mediaId(uploadResult.getMediaId())
                            .toUser(wxMessage.getFromUser())
                            .build());
        } catch (WxErrorException e) {
            e.printStackTrace();
        }

        return null;
    };

}
