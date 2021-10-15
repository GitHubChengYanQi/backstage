package cn.atsoft.dasheng.appBase.service;

import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.cp.api.WxCpMessageService;
import me.chanjar.weixin.cp.api.impl.WxCpMessageServiceImpl;
import me.chanjar.weixin.cp.bean.message.WxCpMessage;
import org.springframework.beans.factory.annotation.Autowired;


public class WxMpService {
    @Autowired
    private WxCpMessageService wxCpMessageServiceImpl;

    public void sendMessage() throws WxErrorException {
        WxCpMessage wxCpMessage = new WxCpMessage();
//        wxCpMessage.setBtnTxt();
       wxCpMessage.setAgentId(1);
        wxCpMessageServiceImpl.send(wxCpMessage);

    }
}
