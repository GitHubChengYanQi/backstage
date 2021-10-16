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
//        String time = null;
//        String note = null;
//        String thing = null;
//        String name = null;
//        List<String> userIds = this.userIds();
//        List<WxMpTemplateData> data = this.getTemplateData();
//        for (WxMpTemplateData datum : data) {
//            if (datum.getName().equals("time2")) {
//                time = datum.getValue();
//
//            }else if (datum.getName().equals("name1")){
//                name = datum.getValue();
//            }else if (datum.getName().equals("thing4")){
//                thing = datum.getValue();
//            }else if (datum.getName().equals("thing5")){
//                note = datum.getValue();
//            }
//        }
//        StringBuffer stringBuffer = new StringBuffer();
//        stringBuffer.append("名字："+"\n \t"+name+"时间"+"\n \t"+time+"事项"+"\n \t"+thing+"\n \t"+"备注"+"\n \t"+note);
//        if (ToolUtil.isNotEmpty(userIds)) {
//            WxCpMessage wxCpMessage = new WxCpMessage();
//            wxCpMessage.setMsgType("text");
//            wxCpMessage.setContent(stringBuffer.toString());
//            for (String userId : userIds) {
//                wxCpMessage.setToUser(userId);
//                try {
//                    wxCpService.getWxCpClient().getMessageService().send(wxCpMessage);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }
        List<String> userIds = this.userIds();
        String title = this.getTitle();
        String description = this.getDescription();
        String url = this.getUrl();

        if (ToolUtil.isNotEmpty(userIds)) {
            WxCpMessage wxCpMessage = new WxCpMessage();
            wxCpMessage.setMsgType("textcard");
            wxCpMessage.setTitle(title);
            wxCpMessage.setDescription(description);
            wxCpMessage.setUrl(url);
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
