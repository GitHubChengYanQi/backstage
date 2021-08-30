package cn.atsoft.dasheng.userInfo.controller;


import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.WxMaSubscribeService;
import cn.binarywang.wx.miniapp.bean.WxMaSubscribeMessage;
import lombok.Data;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.WxMpTemplateMsgService;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateIndustry;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateIndustryEnum;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Data

public class WxTemplate {
    @Autowired
    private WxMaService wxMaService;
    @Autowired
    private WxMpService wxMpService;


    /**
     * 订阅消息
     *
     * @param openid
     * @param data
     */
    public void send(String openid, List<WxMaSubscribeMessage.MsgData> data) {
        WxMaSubscribeService wxMaSubscribeService = wxMaService.getSubscribeService();
        WxMaSubscribeMessage wxMaSubscribeMessage = new WxMaSubscribeMessage();
        wxMaSubscribeMessage.setToUser(openid);
        wxMaSubscribeMessage.setTemplateId("32B3xgUL-IgcTfpiYlcoVNaIR_TVweOdjB0Zftu38jM");
        wxMaSubscribeMessage.setData(data);
        wxMaSubscribeMessage.setLang("zh_CN");
        try {
            wxMaSubscribeService.sendSubscribeMsg(wxMaSubscribeMessage);
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
    }

    /**
     * 模板消息
     *
     * @param openid
     * @param data
     * @return
     */

    public String template(String openid, List<WxMpTemplateData> data) {
        WxMpTemplateMsgService templateMsgService = wxMpService.getTemplateMsgService();
//        templateMsgService.setIndustry();

        WxMpTemplateMessage wxMpTemplateMessage = new WxMpTemplateMessage();
        wxMpTemplateMessage.setTemplateId("623b049c5bad1bba929b055bdd4862f0");
        wxMpTemplateMessage.setData(data);
        wxMpTemplateMessage.setToUser(openid);
        WxMpTemplateMessage.MiniProgram miniProgram = new WxMpTemplateMessage.MiniProgram();
        miniProgram.setAppid("wx6b94599d68b93b0f");
        wxMpTemplateMessage.setMiniProgram(miniProgram);
        WxMpTemplateIndustry wxMpTemplateIndustry = new WxMpTemplateIndustry();


        try {
            String sendTemplateMsg = templateMsgService.sendTemplateMsg(wxMpTemplateMessage);

            return sendTemplateMsg;
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
        return null;
    }


}
