package cn.atsoft.dasheng.appBase.service;

import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.WxMpTemplateMsgService;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

abstract public class sendTemplae implements sendTemplateInterface {

    @Autowired
    private WxMpService wxMpService;


    @Override
    public void send() throws WxErrorException {

        List<WxMpTemplateData> data = this.getTemplateData();
        String templateId = this.getTemplateId();
        String page = this.getPage();
        List<String> openIds = this.getOpenIds();

        WxMpTemplateMsgService templateMsgService = wxMpService.getTemplateMsgService();
        WxMpTemplateMessage wxMpTemplateMessage = new WxMpTemplateMessage();
        wxMpTemplateMessage.setTemplateId(templateId);
        wxMpTemplateMessage.setData(data);
        wxMpTemplateMessage.setUrl(page);
        wxMpTemplateMessage.setUrl(page);
        for (String openId : openIds) {
            wxMpTemplateMessage.setToUser(openId);
            templateMsgService.sendTemplateMsg(wxMpTemplateMessage);
        }
    }
}
