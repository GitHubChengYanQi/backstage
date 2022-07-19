package cn.atsoft.dasheng.erp.service.impl;

import cn.atsoft.dasheng.app.entity.BusinessTrack;
import cn.atsoft.dasheng.app.entity.Message;
import cn.atsoft.dasheng.app.model.params.BusinessTrackParam;
import cn.atsoft.dasheng.app.service.BusinessTrackService;
import cn.atsoft.dasheng.appBase.service.WxCpService;
import cn.atsoft.dasheng.appBase.service.sendTemplae;
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
import cn.atsoft.dasheng.userInfo.service.UserInfoService;
import cn.hutool.core.date.DateTime;
import lombok.Data;
import me.chanjar.weixin.cp.bean.message.WxCpMessage;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static cn.atsoft.dasheng.form.pojo.RuleType.send;

@Service
@Data
public class InstockSendTemplate {
    @Autowired
    private WxuserInfoService wxuserInfoService;
    @Autowired
    private UcOpenUserInfoService ucOpenUserInfoService;
    @Autowired
    private WxCpService wxCpService;
    @Autowired
    private BusinessTrackService businessTrackService;

    @Autowired
    MessageProducer messageProducer;

    @Autowired
    private WxCpSendTemplate wxCpSendTemplate;


    private String url;

    private BusinessTrack businessTrack;

    private Long sourceId;


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
        return "有新的物料需要入库";
    }


    public String getUrl() {
        return url;
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
                    //添加代办信息
                    messageEntity.setType(MessageType.CP);
                    messageProducer.sendMessage(messageEntity, 100);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
               Message message = new Message();
               message.setTime(new DateTime());
               message.setTitle("入库提醒");
               message.setContent("您有新的入库单需要操作");
               message.setType(3);
               message.setSort(0L);
               message.setUrl(getUrl());
               message.setUserId(businessTrack.getUserId());
               messageEntity.setType(MessageType.MESSAGE);
               messageEntity.setMessage(message);
               messageProducer.sendMessage(messageEntity, 100);




        }
    }



}
