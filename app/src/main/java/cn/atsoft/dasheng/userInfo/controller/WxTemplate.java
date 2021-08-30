package cn.atsoft.dasheng.userInfo.controller;


import cn.atsoft.dasheng.portal.remind.entity.Remind;
import cn.atsoft.dasheng.portal.remind.model.params.WxTemplateData;
import cn.atsoft.dasheng.portal.remind.service.RemindService;
import cn.atsoft.dasheng.portal.wxUser.entity.WxuserInfo;
import cn.atsoft.dasheng.portal.wxUser.service.WxuserInfoService;
import cn.atsoft.dasheng.uc.entity.UcOpenUserInfo;
import cn.atsoft.dasheng.uc.service.UcOpenUserInfoService;
import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.WxMaSubscribeService;
import cn.binarywang.wx.miniapp.bean.WxMaSubscribeMessage;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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

import java.util.ArrayList;
import java.util.List;

@Service
@Data

public class WxTemplate {
    @Autowired
    private WxMaService wxMaService;
    @Autowired
    private WxMpService wxMpService;
    @Autowired
    private RemindService remindService;
    @Autowired
    private WxuserInfoService wxuserInfoService;
    @Autowired
    private UcOpenUserInfoService userInfoService;


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
     * @return
     */

    public String template(List<Long> openid, int type) {
        WxMpTemplateMsgService templateMsgService = wxMpService.getTemplateMsgService();

        String templateType = null;
        QueryWrapper<Remind> remindQueryWrapper = new QueryWrapper<>();
        remindQueryWrapper.in("type", type);
        List<Remind> reminds = remindService.list(remindQueryWrapper);
        for (Remind remind : reminds) {
            templateType = remind.getTemplateType();
        }
        WxTemplateData parse = JSON.parseObject(templateType, WxTemplateData.class);

        List<WxMpTemplateData> data = new ArrayList<>();

        for (WxTemplateData.DataList dataList : parse.getDataList()) {
            WxMpTemplateData wxMpTemplateData = new WxMpTemplateData();
            wxMpTemplateData.setName(dataList.getKey());
            wxMpTemplateData.setValue(dataList.getValue());
            data.add(wxMpTemplateData);
        }


        QueryWrapper<WxuserInfo> wxuserInfoQueryWrapper = new QueryWrapper<>();
        wxuserInfoQueryWrapper.in("user_id", openid);
        List<WxuserInfo> wxuserInfoList = wxuserInfoService.list(wxuserInfoQueryWrapper);
        List<Long> memberIds = new ArrayList<>();
        for (WxuserInfo wxuserInfo : wxuserInfoList) {
            memberIds.add(wxuserInfo.getMemberId());
        }
        QueryWrapper<UcOpenUserInfo> ucOpenUserInfoQueryWrapper = new QueryWrapper<>();
        ucOpenUserInfoQueryWrapper.in("member_id", memberIds);
        List<UcOpenUserInfo> ucOpenUserInfos = userInfoService.list(ucOpenUserInfoQueryWrapper);
        List<String> uuids = new ArrayList<>();
        for (UcOpenUserInfo ucOpenUserInfo : ucOpenUserInfos) {
            uuids.add(ucOpenUserInfo.getUuid());
        }

        for (String uuid : uuids) {

            WxMpTemplateMessage wxMpTemplateMessage = new WxMpTemplateMessage();
            wxMpTemplateMessage.setTemplateId(parse.getTemplateId());
            wxMpTemplateMessage.setData(data);
            wxMpTemplateMessage.setToUser(uuid);
            WxMpTemplateMessage.MiniProgram miniProgram = new WxMpTemplateMessage.MiniProgram();
            miniProgram.setAppid("wx6b94599d68b93b0f");
            wxMpTemplateMessage.setMiniProgram(miniProgram);
            try {
                String sendTemplateMsg = templateMsgService.sendTemplateMsg(wxMpTemplateMessage);
                return sendTemplateMsg;
            } catch (WxErrorException e) {
                e.printStackTrace();
            }
        }


        return null;
    }


}
