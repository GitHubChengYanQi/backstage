package cn.atsoft.dasheng.erp.service.impl;

import cn.atsoft.dasheng.app.entity.BusinessTrack;
import cn.atsoft.dasheng.app.model.params.BusinessTrackParam;
import cn.atsoft.dasheng.appBase.service.WxCpService;
import cn.atsoft.dasheng.appBase.service.sendTemplae;
import cn.atsoft.dasheng.binding.wxUser.entity.WxuserInfo;
import cn.atsoft.dasheng.binding.wxUser.service.WxuserInfoService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.uc.entity.UcOpenUserInfo;
import cn.atsoft.dasheng.uc.service.UcOpenUserInfoService;
import cn.atsoft.dasheng.userInfo.service.UserInfoService;
import lombok.Data;
import me.chanjar.weixin.cp.bean.message.WxCpMessage;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Data
public class InstockSendTemplate {
    @Autowired
    private WxuserInfoService wxuserInfoService;
    @Autowired
    private UcOpenUserInfoService ucOpenUserInfoService;
    @Autowired
    private WxCpService wxCpService;

    private String url;

    private BusinessTrack businessTrack;


    //企业微信推送
    public List<String> userIds() {
        //获取uuid
        List<String> uuIds = new ArrayList<>();
        WxuserInfo wxuserInfo = wxuserInfoService.query().eq("user_id", businessTrack.getUserId()).eq("source", "wxCp").one();
        if (ToolUtil.isNotEmpty(wxuserInfo)) {

            UcOpenUserInfo ucOpenUserInfo = ucOpenUserInfoService.query().eq("member_id", wxuserInfo.getMemberId()).eq("source", "wxCp").one();

            if (ToolUtil.isNotEmpty(ucOpenUserInfo)) {
                uuIds.add(ucOpenUserInfo.getUuid());
            }

        }
        return uuIds;
    }

    public String getTitle() {
        return "入库提醒";
    }

    public String getDescription() {
        return "有新的入库消息";
    }


    public String getUrl() {
        return url;
    }

    public void sendTemolate() {
        List<String> userIds = userIds();
        if (ToolUtil.isNotEmpty(userIds)) {
            WxCpMessage wxCpMessage = new WxCpMessage();
            wxCpMessage.setMsgType("textcard");
            wxCpMessage.setTitle(getTitle());
            wxCpMessage.setDescription(getDescription());
            wxCpMessage.setUrl(getUrl());
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
