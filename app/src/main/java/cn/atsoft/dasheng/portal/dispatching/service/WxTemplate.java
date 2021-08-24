package cn.atsoft.dasheng.portal.dispatching.service;

import cn.atsoft.dasheng.portal.dispatching.model.params.DispatchingParam;
import cn.atsoft.dasheng.uc.utils.UserUtils;
import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.WxMaSubscribeService;
import cn.binarywang.wx.miniapp.bean.WxMaSubscribeMessage;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import lombok.Data;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Data

public class WxTemplate {
    @Autowired
    private WxMaService wxMaService;


    public void send(String openid, String templateId, String page, List<WxMaSubscribeMessage.MsgData> data) {
        WxMaSubscribeService wxMaSubscribeService = wxMaService.getSubscribeService();
        WxMaSubscribeMessage wxMaSubscribeMessage = new WxMaSubscribeMessage();
        wxMaSubscribeMessage.setToUser(openid);
        wxMaSubscribeMessage.setTemplateId(templateId);
        wxMaSubscribeMessage.setPage(page);
        wxMaSubscribeMessage.setData(data);

        try {
            wxMaSubscribeService.sendSubscribeMsg(wxMaSubscribeMessage);
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
    }
}
