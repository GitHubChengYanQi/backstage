package cn.atsoft.dasheng.erp.service.impl;

import cn.atsoft.dasheng.app.entity.BusinessTrack;
import cn.atsoft.dasheng.app.entity.Message;
import cn.atsoft.dasheng.app.service.BusinessTrackService;
import cn.atsoft.dasheng.appBase.service.WxCpService;
import cn.atsoft.dasheng.binding.wxUser.entity.WxuserInfo;
import cn.atsoft.dasheng.binding.wxUser.service.WxuserInfoService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.message.enmu.MessageType;
import cn.atsoft.dasheng.message.entity.MarkDownTemplate;
import cn.atsoft.dasheng.message.entity.MessageEntity;
import cn.atsoft.dasheng.message.producer.MessageProducer;
import cn.atsoft.dasheng.sendTemplate.WxCpSendTemplate;
import cn.atsoft.dasheng.sendTemplate.WxCpTemplate;
import cn.atsoft.dasheng.uc.entity.UcOpenUserInfo;
import cn.atsoft.dasheng.uc.service.UcOpenUserInfoService;
import cn.hutool.core.date.DateTime;
import cn.hutool.json.JSONUtil;
import lombok.Data;
import me.chanjar.weixin.cp.bean.message.WxCpMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Data
public class OutstockSendTemplate {
    @Autowired
    private WxuserInfoService wxuserInfoService;
    @Autowired
    private UcOpenUserInfoService ucOpenUserInfoService;
    @Autowired
    private WxCpService wxCpService;
    @Autowired
    private BusinessTrackService businessTrackService;
    @Autowired
    private WxCpSendTemplate wxCpSendTemplate;

    @Autowired
    MessageProducer messageProducer;

    private String url;

    private Long userId;

    private Long sourceId;


    //企业微信推送
    public List<String> userIds() {
        //获取uuid
        List<String> uuIds = new ArrayList<>();
        WxuserInfo wxuserInfo = wxuserInfoService.query().eq("user_id", userId).eq("source", "wxCp").one();
        if (ToolUtil.isNotEmpty(wxuserInfo)) {

            UcOpenUserInfo ucOpenUserInfo = ucOpenUserInfoService.query().eq("member_id", wxuserInfo.getMemberId()).eq("source", "wxCp").one();

            if (ToolUtil.isNotEmpty(ucOpenUserInfo)) {
                uuIds.add(ucOpenUserInfo.getUuid());
            }

        }
        return uuIds;
    }

    public String getTitle() {
        return "出库提醒";
    }

    public String getDescription() {
        return "有新的物料需要出库";
    }


    public String getUrl() {
        return url;
    }
    public void send(String url){
        List<Long> users = new ArrayList();
        users.add(getUserId());


        wxCpSendTemplate.sendMarkDownTemplate(new MarkDownTemplate() {{
            setItems("审批被否决");
            setDescription("有新的物料需要出库");
            setUrl(url);
            setUserIds(users);
        }});


    }
    public void sendTemplate() {
        MessageEntity messageEntity = new MessageEntity();
        messageEntity.setType(MessageType.CP);


        List<String> userIds = userIds();
        if (ToolUtil.isNotEmpty(userIds)) {
            WxCpMessage wxCpMessage = new WxCpMessage();
            wxCpMessage.setMsgType("textcard");
            wxCpMessage.setTitle(getTitle());
            wxCpMessage.setDescription(getDescription());
            wxCpMessage.setUrl(getUrl());
            for (String userId : userIds) {
                wxCpMessage.setToUser(userId);
                messageEntity.setCpData(wxCpMessage);
                messageEntity.setTimes(0);
                messageEntity.setMaxTimes(2);
                try {
                    messageProducer.sendMessage(messageEntity);
//                    wxCpService.getWxCpClient().getMessageService().send(wxCpMessage);
                    //添加代办信息
                } catch (Exception e) {
                    e.printStackTrace();
                }
                messageEntity.setType(MessageType.MESSAGE);
                Message message = new Message();
                message.setTime(new DateTime());
                message.setTitle("出库提醒");
                message.setUrl(getUrl());
                message.setContent("您有新的出库单需要操作");
                message.setType(3);
                message.setSort(0L);
                messageEntity.setMessage(message);
                messageProducer.sendMessage(messageEntity);
            }
        }
    }


}
