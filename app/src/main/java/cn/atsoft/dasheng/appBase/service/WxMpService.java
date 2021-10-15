package cn.atsoft.dasheng.appBase.service;

import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.cp.api.WxCpMessageService;
import me.chanjar.weixin.cp.api.impl.WxCpMessageServiceImpl;
import me.chanjar.weixin.cp.bean.message.WxCpMessage;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;


public class WxMpService {
    @Autowired
    private WxCpMessageService wxCpMessageServiceImpl;

    public void sendMessage() throws WxErrorException {
        WxCpMessage wxCpMessage = new WxCpMessage();

        Map<String, String> map = new HashMap<>();
        map.put("text", "aaaaaaaa");
        wxCpMessage.setBtnTxt(map.get("text"));
        wxCpMessage.setAgentId(1);
        wxCpMessageServiceImpl.send(wxCpMessage);

    }
}
