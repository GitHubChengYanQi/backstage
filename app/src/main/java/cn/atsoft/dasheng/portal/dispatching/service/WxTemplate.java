package cn.atsoft.dasheng.portal.dispatching.service;


import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.WxMaSubscribeService;
import cn.binarywang.wx.miniapp.bean.WxMaSubscribeMessage;
import lombok.Data;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@Data

public class WxTemplate {
    @Autowired
    private WxMaService wxMaService;


    public void send(String openid, String page, List<WxMaSubscribeMessage.MsgData> data) {
        WxMaSubscribeService wxMaSubscribeService = wxMaService.getSubscribeService();
        WxMaSubscribeMessage wxMaSubscribeMessage = new WxMaSubscribeMessage();
        wxMaSubscribeMessage.setToUser(openid);
        wxMaSubscribeMessage.setTemplateId("32B3xgUL-IgcTfpiYlcoVNaIR_TVweOdjB0Zftu38jM");
        wxMaSubscribeMessage.setPage(page);
        wxMaSubscribeMessage.setData(data);
        wxMaSubscribeMessage.setLang("zh_CN");

        try {
            wxMaSubscribeService.sendSubscribeMsg(wxMaSubscribeMessage);
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
    }
}
