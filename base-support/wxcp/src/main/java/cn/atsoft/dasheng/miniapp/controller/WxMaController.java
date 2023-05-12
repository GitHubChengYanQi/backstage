package cn.atsoft.dasheng.miniapp.controller;

import cn.atsoft.dasheng.miniapp.model.propertis.WxMaProperties;
import cn.atsoft.dasheng.miniapp.service.WxMaMessageService;
import cn.atsoft.dasheng.miniapp.service.WxMiniAppService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.bean.WxMaMessage;
import cn.binarywang.wx.miniapp.bean.WxMaSubscribeMsgEvent;
import cn.binarywang.wx.miniapp.constant.WxMaConstants;
import cn.binarywang.wx.miniapp.message.WxMaMessageRouter;
import cn.binarywang.wx.miniapp.message.WxMaXmlOutMessage;
import cn.binarywang.wx.miniapp.util.WxMaConfigHolder;
import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/wxma/messageCallBack/{appid}")
public class WxMaController {
    protected static final Logger logger = LoggerFactory.getLogger(WxMaProperties.class);

    @Autowired
    private WxMaMessageRouter wxMaMessageRouter;

    @Autowired
    private WxMiniAppService wxMaService;

    @Autowired
    WxMaMessageService wxMaMessageService;

    @PostMapping(produces = "application/xml; charset=UTF-8")
    public String post(
                    @PathVariable String appid,
                       @RequestBody String requestBody,
                       @RequestParam(name = "msg_signature", required = false) String msgSignature,
                       @RequestParam(name = "encrypt_type", required = false) String encryptType,
                       @RequestParam(name = "signature", required = false) String signature,
                       @RequestParam("timestamp") String timestamp,
                       @RequestParam("nonce") String nonce) {
        logger.info("\n接收微信请求：[msg_signature=[{}], encrypt_type=[{}], signature=[{}]," +
                        " timestamp=[{}], nonce=[{}], requestBody=[\n{}\n] ",
                msgSignature, encryptType, signature, timestamp, nonce, requestBody);

        WxMaServiceImpl wxCpClient = wxMaService.getWxCpClient(appid);
        final boolean isJson = Objects.equals(wxCpClient.getWxMaConfig().getMsgDataFormat(),
                WxMaConstants.MsgDataFormat.JSON);
        if (StringUtils.isBlank(encryptType)) {
            // 明文传输的消息
            WxMaMessage inMessage;
            if (isJson) {
                inMessage = WxMaMessage.fromJson(requestBody);
            } else {//xml
                inMessage = WxMaMessage.fromXml(requestBody);
            }

            this.route(inMessage);
            WxMaConfigHolder.remove();//清理ThreadLocal
            return "success";
        }
        String out = null;
        if ("aes".equals(encryptType)) {
            // 是aes加密的消息
            WxMaMessage inMessage;
            if (isJson) {
                inMessage = WxMaMessage.fromEncryptedJson(requestBody, wxCpClient.getWxMaConfig());
            } else {//xml
                inMessage = WxMaMessage.fromEncryptedXml(requestBody, wxCpClient.getWxMaConfig(),
                        timestamp, nonce, msgSignature);
            }
            String event = inMessage.getEvent();
            String toUser = inMessage.getToUser();
            String fromUser = inMessage.getFromUser();
            String msgType = inMessage.getMsgType();
            String content = inMessage.getContent();
            String msgId = inMessage.getMsgType();
//            WxMaSubscribeMsgEvent.WxMaSubscribeMsgEventJson uselessMsg = inMessage.getUselessMsg();
            Map<String, Object> allFieldsMap = inMessage.getAllFieldsMap();
            List<WxMaSubscribeMsgEvent.WxMaSubscribeMsgEventJson> list = new ArrayList<>();
            Object object = allFieldsMap.get("List");
            List<cn.atsoft.dasheng.miniapp.entity.WxMaMessage> messageList = new ArrayList<>();
            if (object instanceof List) {
                list.addAll(JSON.parseArray(JSON.toJSONString(object), WxMaSubscribeMsgEvent.WxMaSubscribeMsgEventJson.class));
            }else {
                list.add((WxMaSubscribeMsgEvent.WxMaSubscribeMsgEventJson) object);
            }
            for (WxMaSubscribeMsgEvent.WxMaSubscribeMsgEventJson wxMaSubscribeMsgEventJson : list) {
                cn.atsoft.dasheng.miniapp.entity.WxMaMessage entity = BeanUtil.toBean(wxMaSubscribeMsgEventJson, cn.atsoft.dasheng.miniapp.entity.WxMaMessage.class);
                entity.setEvent(event);
                entity.setToUserName(toUser);
                entity.setFromUserName(fromUser);
                entity.setEventType(msgType);
                entity.setMsgId(Long.parseLong(msgId));
                messageList.add(BeanUtil.toBean(wxMaSubscribeMsgEventJson, cn.atsoft.dasheng.miniapp.entity.WxMaMessage.class));
            }
            wxMaMessageService.saveBatch(messageList);
            WxMaXmlOutMessage outMessage = this.route(inMessage);

           if (outMessage == null) {
//               outMessage = new WxMaXmlOutMessage();
                return "success";
            }

            out = outMessage.toEncryptedXml(wxCpClient.getWxMaConfig());
            WxMaConfigHolder.remove();//清理ThreadLocal
            return out;

        }
        WxMaConfigHolder.remove();//清理ThreadLocal
        throw new RuntimeException("不可识别的加密类型：" + encryptType);
    }

    private WxMaXmlOutMessage route(WxMaMessage message) {
        try {
            WxMaXmlOutMessage route = wxMaMessageRouter.route(message);
            return route;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }


}
