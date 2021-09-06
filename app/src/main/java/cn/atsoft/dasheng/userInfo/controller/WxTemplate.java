package cn.atsoft.dasheng.userInfo.controller;


import cn.atsoft.dasheng.portal.remind.service.RemindService;
import cn.atsoft.dasheng.portal.remindUser.service.RemindUserService;
import cn.atsoft.dasheng.binding.wxUser.service.WxuserInfoService;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import cn.atsoft.dasheng.uc.service.UcOpenUserInfoService;
import cn.binarywang.wx.miniapp.api.WxMaService;
import lombok.Data;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    @Autowired
    private RemindUserService remindUserService;
    @Autowired
    private UserService userService;

//
//    /**
//     * 订阅消息
//     *
//     * @param openid
//     * @param data
//     */
//    public void send(String openid, List<WxMaSubscribeMessage.MsgData> data) {
//        WxMaSubscribeService wxMaSubscribeService = wxMaService.getSubscribeService();
//        WxMaSubscribeMessage wxMaSubscribeMessage = new WxMaSubscribeMessage();
//        wxMaSubscribeMessage.setToUser(openid);
//        wxMaSubscribeMessage.setTemplateId("32B3xgUL-IgcTfpiYlcoVNaIR_TVweOdjB0Zftu38jM");
//        wxMaSubscribeMessage.setData(data);
//        wxMaSubscribeMessage.setLang("zh_CN");
//        try {
//            wxMaSubscribeService.sendSubscribeMsg(wxMaSubscribeMessage);
//        } catch (WxErrorException e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 模板消息
//     */

//    public String template(Long type) {
////        if (UserUtils.getUserId() == null) {
////            throw new ServiceException(500, "请先登录");
////        }
//
////        if (UserUtils.getUserId() == null) {
////            throw new ServiceException(500, "请先登录");
////        }
//        WxMpTemplateMsgService templateMsgService = wxMpService.getTemplateMsgService();
//
//
//        //查询data
//        String templateType = null;
//        QueryWrapper<Remind> remindQueryWrapper = new QueryWrapper<>();
//        remindQueryWrapper.in("type", type);
//        List<Remind> reminds = remindService.list(remindQueryWrapper);
//        List<Long> ids = new ArrayList<>();
//        for (Remind remind : reminds) {
//            ids.add(remind.getRemindId());
//            templateType = remind.getTemplateType();
//        }
//        QueryWrapper<RemindUser> queryWrapper = new QueryWrapper<>();
//        queryWrapper.in("remind_id", ids);
//        List<RemindUser> remindUserList = remindUserService.list(queryWrapper);
//        List<Long> userIds = new ArrayList<>();
//        for (RemindUser remindUser : remindUserList) {
//            userIds.add(remindUser.getUserId());
//        }
//
//        WxTemplateData parse = JSON.parseObject(templateType, WxTemplateData.class);
//
//        List<WxMpTemplateData> data = new ArrayList<>();
//        //赋值data
//        for (WxTemplateData.DataList dataList : parse.getDataList()) {
//            WxMpTemplateData wxMpTemplateData = new WxMpTemplateData();
//            wxMpTemplateData.setName(dataList.getKey());
//            wxMpTemplateData.setValue(dataList.getValue());
//            data.add(wxMpTemplateData);
//        }
//
//        //模板消息发送人
//        QueryWrapper<WxuserInfo> wxuserInfoQueryWrapper = new QueryWrapper<>();
//        wxuserInfoQueryWrapper.in("user_id", userIds);
//        List<WxuserInfo> wxuserInfoList = wxuserInfoService.list(wxuserInfoQueryWrapper);
//
//        List<Long> memberIds = new ArrayList<>();
//
//        if (wxuserInfoList.size() > 0) {
//            for (WxuserInfo wxuserInfo : wxuserInfoList) {
//                memberIds.add(wxuserInfo.getMemberId());
//            }
//            QueryWrapper<UcOpenUserInfo> ucOpenUserInfoQueryWrapper = new QueryWrapper<>();
//            ucOpenUserInfoQueryWrapper.in("member_id", memberIds).in("source", "wxMp");
//            List<UcOpenUserInfo> ucOpenUserInfos = userInfoService.list(ucOpenUserInfoQueryWrapper);
//            List<String> uuids = new ArrayList<>();
//            for (UcOpenUserInfo ucOpenUserInfo : ucOpenUserInfos) {
//                uuids.add(ucOpenUserInfo.getUuid());
//            }
//
//            for (String uuid : uuids) {
//                WxMpTemplateMessage wxMpTemplateMessage = new WxMpTemplateMessage();
//                wxMpTemplateMessage.setTemplateId(parse.getTemplateId());
//                wxMpTemplateMessage.setData(data);
//                wxMpTemplateMessage.setToUser(uuid);
//                wxMpTemplateMessage.setUrl(parse.getUrl());
//
//                try {
//                    String sendTemplateMsg = templateMsgService.sendTemplateMsg(wxMpTemplateMessage);
//                    return sendTemplateMsg;
//                } catch (WxErrorException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//
//
//        return null;
//    }
//
//    public String template(Long type, Long id, String time, String note, String details) {
////        if (UserUtils.getUserId() == null) {
////            throw new ServiceException(500, "请先登录");
////        }
//        String repalceName = null;
//        String replace = null;
//        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
//        userQueryWrapper.in("user_id", id);
//        List<User> userList = userService.list(userQueryWrapper);
//
//        for (User user : userList) {
//            repalceName = user.getName();
//        }
//        WxMpTemplateMsgService templateMsgService = wxMpService.getTemplateMsgService();
//
//
//        //查询data
//        String templateType = null;
//        QueryWrapper<Remind> remindQueryWrapper = new QueryWrapper<>();
//        remindQueryWrapper.in("type", type);
//        List<Remind> reminds = remindService.list(remindQueryWrapper);
//        List<Long> remindids = new ArrayList<>();
//        for (Remind remind : reminds) {
//            remindids.add(remind.getRemindId());
//            templateType = remind.getTemplateType();
//            if(ToolUtil.isNotEmpty(templateType)){
//                if (ToolUtil.isNotEmpty(repalceName)) {
//                    replace = templateType.replace("{{name}}", repalceName).replace("{{time}}", time);
//                    if(ToolUtil.isNotEmpty(replace)){
//                        if (ToolUtil.isNotEmpty(note)) {
//                            replace = replace.replace("{{note}}", note);
//                        }
//                        if (ToolUtil.isNotEmpty(details) ) {
//                            replace = replace.replace("{{details}}", details);
//                        }
//                    }
//                }
//
//            }
//        }
//        QueryWrapper<RemindUser> queryWrapper = new QueryWrapper<>();
//        queryWrapper.in("remind_id", remindids);
//        List<RemindUser> remindUserList = remindUserService.list(queryWrapper);
//        List<Long> userIds = new ArrayList<>();
//        for (RemindUser remindUser : remindUserList) {
//            userIds.add(remindUser.getUserId());
//        }
//
//        WxTemplateData parse = JSON.parseObject(replace, WxTemplateData.class);
//
//        List<WxMpTemplateData> data = new ArrayList<>();
//        //赋值data
//        for (WxTemplateData.DataList dataList : parse.getDataList()) {
//            WxMpTemplateData wxMpTemplateData = new WxMpTemplateData();
//            wxMpTemplateData.setName(dataList.getKey());
//            wxMpTemplateData.setValue(dataList.getValue());
//            data.add(wxMpTemplateData);
//        }
//
//        //模板消息发送人
//        QueryWrapper<WxuserInfo> wxuserInfoQueryWrapper = new QueryWrapper<>();
//        wxuserInfoQueryWrapper.in("user_id", userIds);
//        List<WxuserInfo> wxuserInfoList = wxuserInfoService.list(wxuserInfoQueryWrapper);
//        List<Long> memberIds = new ArrayList<>();
//
//        if (wxuserInfoList.size() > 0) {
//            for (WxuserInfo wxuserInfo : wxuserInfoList) {
//                memberIds.add(wxuserInfo.getMemberId());
//            }
//            QueryWrapper<UcOpenUserInfo> ucOpenUserInfoQueryWrapper = new QueryWrapper<>();
//            ucOpenUserInfoQueryWrapper.in("member_id", memberIds).in("source", "wxMp");
//            List<UcOpenUserInfo> ucOpenUserInfos = userInfoService.list(ucOpenUserInfoQueryWrapper);
//            List<String> uuids = new ArrayList<>();
//            for (UcOpenUserInfo ucOpenUserInfo : ucOpenUserInfos) {
//                uuids.add(ucOpenUserInfo.getUuid());
//            }
//
//            for (String uuid : uuids) {
//                WxMpTemplateMessage wxMpTemplateMessage = new WxMpTemplateMessage();
//                wxMpTemplateMessage.setTemplateId(parse.getTemplateId());
//                wxMpTemplateMessage.setData(data);
//                wxMpTemplateMessage.setToUser(uuid);
//                wxMpTemplateMessage.setUrl(parse.getUrl());
//
//                try {
//                    String sendTemplateMsg = templateMsgService.sendTemplateMsg(wxMpTemplateMessage);
//                    return sendTemplateMsg;
//                } catch (WxErrorException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//
//
//        return null;
//    }
}
