package cn.atsoft.dasheng.audit.controller;

import cn.atsoft.dasheng.audit.entity.WxAudit;
import cn.atsoft.dasheng.audit.service.RestWxCpService;
import com.alibaba.fastjson.JSON;
import me.chanjar.weixin.cp.bean.message.WxCpXmlMessage;
import me.chanjar.weixin.cp.bean.message.WxCpXmlOutMessage;
import me.chanjar.weixin.cp.message.WxCpMessageRouter;
import me.chanjar.weixin.cp.util.crypto.WxCpCryptUtil;
import me.chanjar.weixin.mp.api.WxMpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wx/messageCallBack")
public class WxController {
    protected static final Logger logger = LoggerFactory.getLogger(WxAudit.class);

    @Autowired
    private RestWxCpService wxCpService;
    @Autowired
    private WxMpService wxMpService;

    @PostMapping(produces = "application/xml; charset=UTF-8")
    public String post(@RequestBody String requestBody,
                       @RequestParam("msg_signature") String signature,
                       @RequestParam("timestamp") String timestamp,
                       @RequestParam("nonce") String nonce) {
        logger.info("\n接收微信请求：[signature=[{}], timestamp=[{}], nonce=[{}], requestBody=[\n{}\n] ",
                signature, timestamp, nonce, requestBody);

        WxCpXmlMessage inMessage = WxCpXmlMessage.fromEncryptedXml(requestBody, wxCpService.getWxCpClient().getWxCpConfigStorage(), timestamp, nonce, signature);
        logger.info("\n消息解密后内容为：\n{} ", JSON.toJSONString(inMessage));
        WxCpXmlOutMessage outMessage = this.route(inMessage);
        if (outMessage == null) {
            return "";
        }
        String out = outMessage.toEncryptedXml(wxCpService.getWxCpClient().getWxCpConfigStorage());
        logger.debug("\n组装回复信息：{}", out);
        return null;
    }

    @GetMapping(produces = "text/plain;charset=utf-8")
    public String authGet(
                          @RequestParam(name = "msg_signature", required = false) String signature,
                          @RequestParam(name = "timestamp", required = false) String timestamp,
                          @RequestParam(name = "nonce", required = false) String nonce,
                          @RequestParam(name = "echostr", required = false) String echostr) {

        if (wxCpService.getWxCpClient().checkSignature(signature, timestamp,echostr ,nonce)) {
//            logger.info("\n接收微信请求：[signature=[{}], timestamp=[{}], nonce=[{}], requestBody=[\n{}\n] ",
//                    signature, timestamp, nonce, requestBody);

//            WxCpXmlMessage inMessage = WxCpXmlMessage.fromEncryptedXml(requestBody, wxCpService.getWxCpClient().getWxCpConfigStorage(), timestamp, nonce, signature);
//            logger.info("\n消息解密后内容为：\n{} ", JSON.toJSONString(inMessage));
//            WxCpXmlOutMessage outMessage = this.route(inMessage);
//            if (outMessage == null) {
//                return "";
//            }
//            String out = outMessage.toEncryptedXml(wxCpService.getWxCpClient().getWxCpConfigStorage());
//            logger.debug("\n组装回复信息：{}", out);


            return new WxCpCryptUtil(wxCpService.getWxCpClient().getWxCpConfigStorage()).decrypt(echostr);
//            return nonce;
        }
        return "非法请求";
    }

    private WxCpXmlOutMessage route( WxCpXmlMessage message) {
        try {
            return wxCpService.newRouter(wxCpService.getWxCpClient()).route(message);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return null;
    }
}
