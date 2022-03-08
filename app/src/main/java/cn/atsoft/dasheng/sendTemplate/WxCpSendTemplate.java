package cn.atsoft.dasheng.sendTemplate;


import cn.atsoft.dasheng.app.entity.Message;
import cn.atsoft.dasheng.binding.wxUser.entity.WxuserInfo;
import cn.atsoft.dasheng.binding.wxUser.service.WxuserInfoService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.service.impl.ActivitiProcessTaskSend;
import cn.atsoft.dasheng.message.enmu.MessageType;
import cn.atsoft.dasheng.message.entity.MessageEntity;
import cn.atsoft.dasheng.message.producer.MessageProducer;
import cn.atsoft.dasheng.message.topic.TopicMessage;
import cn.atsoft.dasheng.uc.entity.UcOpenUserInfo;
import cn.atsoft.dasheng.uc.service.UcOpenUserInfoService;
import cn.hutool.core.date.DateTime;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.Data;
import me.chanjar.weixin.cp.bean.message.WxCpMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Data
public class WxCpSendTemplate {
    @Autowired
    private WxuserInfoService wxuserInfoService;
    @Autowired
    private UcOpenUserInfoService ucOpenUserInfoService;
    @Autowired
    MessageProducer messageProducer;

//    //添加系统小铃铛信息
//    private Message message;

    private Long sourceId;

    private String source;

    private WxCpTemplate wxCpTemplate;

    //获取userId
    private List<String> userIds() {
        //获取uuid
        List<String> uuIds = new ArrayList<>();
        List<WxuserInfo> wxuserInfos = wxCpTemplate.getUserIds().size() == 0 ? new ArrayList<>() : wxuserInfoService.query().in("user_id", wxCpTemplate.getUserIds()).eq("source", "wxCp").list();

        List<Long> memberIds = new ArrayList<>();
        if (ToolUtil.isNotEmpty(wxuserInfos)) {
            for (WxuserInfo wxuserInfo : wxuserInfos) {
                memberIds.add(wxuserInfo.getMemberId());
            }
        }

        List<UcOpenUserInfo> userInfoList = memberIds.size() == 0 ? new ArrayList<>() : ucOpenUserInfoService.query().in("member_id", memberIds).eq("source", "wxCp").list();
        if (ToolUtil.isNotEmpty(userInfoList)) {
            for (UcOpenUserInfo ucOpenUserInfo : userInfoList) {
                uuIds.add(ucOpenUserInfo.getUuid());
            }
        }

        return uuIds;
    }
    protected static final Logger logger = LoggerFactory.getLogger(TopicMessage.class);

    //发送模板消息
    public void sendTemplate() {

        MessageEntity messageEntity = new MessageEntity();
        messageEntity.setType(MessageType.CP);
        WxCpMessage wxCpMessage = new WxCpMessage();
        wxCpMessage.setUrl(wxCpTemplate.getUrl());
        wxCpMessage.setTitle(wxCpTemplate.getTitle());
        wxCpMessage.setDescription(wxCpTemplate.getDescription());
        wxCpMessage.setMsgType("textcard");
        List<String> userIds = userIds();
        if (ToolUtil.isNotEmpty(userIds)) {
            for (String userId : userIds) {
                wxCpMessage.setToUser(userId);
                messageEntity.setCpData(wxCpMessage);
                messageEntity.setTimes(0);
                messageEntity.setMaxTimes(2);
                try {
                    String randomString = ToolUtil.getRandomString(5);
                    String s = messageEntity.getCpData().getDescription() + randomString;
                    messageEntity.getCpData().setDescription(s);
                    logger.info("微信消息"+messageEntity.getCpData().getDescription());
                    messageProducer.sendMessage(messageEntity);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            for (Long userId : wxCpTemplate.getUserIds()) {
                messageEntity.setType(MessageType.MESSAGE);
                Message message = new Message();
                message.setTime(new DateTime());
                message.setTitle(wxCpTemplate.getTitle());
                message.setContent(wxCpTemplate.getDescription());
                message.setType(wxCpTemplate.getType());
                message.setUserId(userId);
                message.setSort(0L);
                message.setSourceId(getSourceId());
                message.setSource(getSource());
                message.setUrl(wxCpTemplate.getUrl());
                messageEntity.setMessage(message);
                logger.info("铃铛发送"+messageEntity.getCpData().getDescription());
                messageProducer.sendMessage(messageEntity);
            }
        }

    }

}
