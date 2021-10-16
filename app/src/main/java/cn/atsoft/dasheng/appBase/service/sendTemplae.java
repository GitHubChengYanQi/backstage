package cn.atsoft.dasheng.appBase.service;

import cn.atsoft.dasheng.core.util.ToolUtil;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.cp.bean.message.WxCpMessage;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.WxMpTemplateMsgService;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

abstract public class sendTemplae implements sendTemplateInterface {

    @Autowired
    private WxMpService wxMpService;

    @Autowired
    private WxCpService wxCpService;


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


        for (String openId : openIds) {
            wxMpTemplateMessage.setToUser(openId);
            try {
                templateMsgService.sendTemplateMsg(wxMpTemplateMessage);
            } catch (WxErrorException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 企业微信推送消息
     *
     * @throws WxErrorException
     */
    @Override
    public void wxCpSend() {
        List<String> userIds = this.userIds();
        if (ToolUtil.isNotEmpty(userIds)) {
            WxCpMessage wxCpMessage = new WxCpMessage();
            wxCpMessage.setMsgType("textcard");
            wxCpMessage.setTitle("新建保修");
            wxCpMessage.setDescription("<div class=\\\"gray\\\">2016年9月26日</div> <div class=\\\"normal\\\">恭喜你抽中iPhone 13一台，领奖码：xxxx</div><div class=\\\"highlight\\\">请于2021年11月11日前联系行政同事领取</div>\",");
            wxCpMessage.setUrl("http://192.168.1.228/pages/engineerOrder/index?id={{user}}");
            for (String userId : userIds) {
                wxCpMessage.setToUser(userId);
                try {
                    wxCpService.getWxCpClient().getMessageService().send(wxCpMessage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
