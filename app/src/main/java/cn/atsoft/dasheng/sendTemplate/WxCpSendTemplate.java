package cn.atsoft.dasheng.sendTemplate;


import cn.atsoft.dasheng.app.entity.Message;
import cn.atsoft.dasheng.binding.wxUser.entity.WxuserInfo;
import cn.atsoft.dasheng.binding.wxUser.service.WxuserInfoService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.service.impl.ActivitiProcessTaskSend;
import cn.atsoft.dasheng.message.enmu.MessageType;
import cn.atsoft.dasheng.message.entity.MarkDownTemplate;
import cn.atsoft.dasheng.message.entity.MessageEntity;
import cn.atsoft.dasheng.message.producer.MessageProducer;
import cn.atsoft.dasheng.message.topic.TopicMessage;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
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
    private MessageProducer messageProducer;
    @Autowired
    private UserService userService;

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

    private List<String> userIds2UuIds(List<Long> userIds) {
        List<String> uuIds = new ArrayList<>();
        List<WxuserInfo> wxuserInfos = userIds.size() == 0 ? new ArrayList<>() : wxuserInfoService.query().in("user_id", userIds).eq("source", "wxCp").list();

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
                    logger.info("微信消息" + messageEntity.getCpData().getDescription());
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
                logger.info("铃铛发送" + messageEntity.getCpData().getDescription());
                messageProducer.sendMessage(messageEntity);
            }
        }

    }


    public void sendMarkDownTemplate(MarkDownTemplate markDownTemplate) {
        MessageEntity messageEntity = new MessageEntity();
        messageEntity.setType(MessageType.CP);
        WxCpMessage wxCpMessage = new WxCpMessage();
        wxCpMessage.setMsgType("markdown");
        wxCpMessage.setContent(this.getContent(markDownTemplate));
        List<String> userIds = userIds2UuIds(markDownTemplate.getUserIds());
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
                    logger.info("微信MarkDown消息" + messageEntity.getCpData().getDescription());
                    messageProducer.sendMessage(messageEntity);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            for (Long userId : markDownTemplate.getUserIds()) {
                messageEntity.setType(MessageType.MESSAGE);
                Message message = new Message();
                message.setTime(new DateTime());
                message.setTitle(markDownTemplate.getTitle());
                message.setContent(markDownTemplate.getDescription());
                message.setType(markDownTemplate.getType());
                message.setUserId(userId);
                message.setSort(0L);
                message.setSourceId(markDownTemplate.getSourceId());
                message.setSource(markDownTemplate.getSource());
                message.setUrl(markDownTemplate.getUrl());
                messageEntity.setMessage(message);
                logger.info("铃铛发送" + messageEntity.getCpData().getDescription());
                messageProducer.sendMessage(messageEntity);
            }
        }
    }

    public String getContent(MarkDownTemplate markDownTemplate) {
        if (ToolUtil.isNotEmpty(markDownTemplate.getCreateUser())) {
            User user = userService.getById(markDownTemplate.getCreateUser());
            markDownTemplate.setCreateUserName(user.getName());
        }
        StringBuffer stringBuffer = new StringBuffer();

        if (ToolUtil.isNotEmpty(markDownTemplate.getType())) {
            this.selectTitle(markDownTemplate);
            stringBuffer.append("您有新的").append(markDownTemplate.getTitle()).append("\n \n");
        } else {
            stringBuffer.append("您有新的消息").append("\n \n");
        }
        stringBuffer.append(">**").append("任务详情").append("**").append("\n \n");
        if (ToolUtil.isNotEmpty(markDownTemplate.getItems())) {
            stringBuffer.append(">**事　项**：").append("<font color=\"info\">").append(markDownTemplate.getItems()).append("</font>").append("\n \n ");
        }

        if (ToolUtil.isNotEmpty(markDownTemplate.getDescription())) {
            stringBuffer.append(">**详　情**：").append("<font color=\"warning\">").append(markDownTemplate.getDescription()).append("</font>").append("\n\n");
        }
        if (ToolUtil.isEmpty(markDownTemplate.getRemark())) {
            stringBuffer.append(">**备　注**：").append("<font color=\"comment\">").append(" ").append("</font>").append("\n\n");
        }else {
            stringBuffer.append(">**备　注**：").append("<font color=\"comment\">").append(markDownTemplate.getRemark()).append("</font>").append("\n\n");
        }
        if (ToolUtil.isNotEmpty(markDownTemplate.getCreateUserName())) {
            stringBuffer.append(">**发起人**：").append(markDownTemplate.getCreateUserName()).append("\n \n");
        }
        if (ToolUtil.isNotEmpty(markDownTemplate.getUrl())) {
            stringBuffer.append(">").append("\n\n");
            stringBuffer.append(">[点击查看详情](").append(markDownTemplate.getUrl()).append(")");
        }
        return stringBuffer.toString();
    }

    void selectTitle(MarkDownTemplate markDownTemplate) {
        ToolUtil.isNotEmpty(markDownTemplate.getType());
        {
            switch (markDownTemplate.getType()) {
                case 2:
                    markDownTemplate.setTitle("消息");
                    break;
                case 1:
                    markDownTemplate.setTitle("通知");
                    break;
                case 0:
                    markDownTemplate.setTitle("待办");
                    break;
            }
        }
    }

}
