package cn.atsoft.dasheng.sendTemplate;


import cn.atsoft.dasheng.app.entity.Message;
import cn.atsoft.dasheng.app.service.MessageService;
import cn.atsoft.dasheng.binding.wxUser.entity.WxuserInfo;
import cn.atsoft.dasheng.binding.wxUser.service.WxuserInfoService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.config.MobileService;
import cn.atsoft.dasheng.erp.service.impl.ActivitiProcessTaskSend;
import cn.atsoft.dasheng.form.service.ActivitiProcessTaskService;
import cn.atsoft.dasheng.message.enmu.MessageType;
import cn.atsoft.dasheng.message.entity.MarkDownTemplate;
import cn.atsoft.dasheng.message.entity.MessageEntity;
import cn.atsoft.dasheng.message.producer.MessageProducer;
import cn.atsoft.dasheng.message.topic.TopicMessage;
import cn.atsoft.dasheng.sendTemplate.pojo.UserId2WxOpenId;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import cn.atsoft.dasheng.uc.entity.UcOpenUserInfo;
import cn.atsoft.dasheng.uc.service.UcOpenUserInfoService;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.Data;
import me.chanjar.weixin.cp.bean.message.WxCpMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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
    @Autowired
    private ActivitiProcessTaskService activitiProcessTaskService;
    @Autowired
    private MessageService messageService;

    @Autowired
    private MobileService mobileService;

//    //添加系统小铃铛信息
//    private Message message;

    private Long sourceId;

    private String source;

    private WxCpTemplate wxCpTemplate;

    //获取userId
    private List<String> userIds() {
        //获取uuid
        List<String> uuIds = new ArrayList<>();
//        List<WxuserInfo> wxuserInfos = wxCpTemplate.getUserIds().size() == 0 ? new ArrayList<>() : wxuserInfoService.query().in("user_id", wxCpTemplate.getUserIds()).eq("source", "wxCp").list();
        List<WxuserInfo> wxuserInfos = wxCpTemplate.getUserIds().size() == 0 ? new ArrayList<>() : wxuserInfoService.query().in("user_id", wxCpTemplate.getUserIds()).list();

        List<Long> memberIds = new ArrayList<>();
        if (ToolUtil.isNotEmpty(wxuserInfos)) {
            for (WxuserInfo wxuserInfo : wxuserInfos) {
                memberIds.add(wxuserInfo.getMemberId());
            }
        }

//        List<UcOpenUserInfo> userInfoList = memberIds.size() == 0 ? new ArrayList<>() : ucOpenUserInfoService.query().in("member_id", memberIds).eq("source", "wxCp").list();
        List<UcOpenUserInfo> userInfoList = memberIds.size() == 0 ? new ArrayList<>() : ucOpenUserInfoService.query().in("member_id", memberIds).list();
        if (ToolUtil.isNotEmpty(userInfoList)) {
            for (UcOpenUserInfo ucOpenUserInfo : userInfoList) {
                uuIds.add(ucOpenUserInfo.getUuid());
            }
        }

        return uuIds;
    }

    private List<String> userIds2UuIds(List<Long> userIds) {
        List<String> uuIds = new ArrayList<>();
//        List<WxuserInfo> wxuserInfos = userIds.size() == 0 ? new ArrayList<>() : wxuserInfoService.query().in("user_id", userIds).eq("source", "wxCp").list();
        List<WxuserInfo> wxuserInfos = userIds.size() == 0 ? new ArrayList<>() : wxuserInfoService.query().in("user_id", userIds).list();

        List<Long> memberIds = new ArrayList<>();
        if (ToolUtil.isNotEmpty(wxuserInfos)) {
            for (WxuserInfo wxuserInfo : wxuserInfos) {
                memberIds.add(wxuserInfo.getMemberId());
            }
        }

//        List<UcOpenUserInfo> userInfoList = memberIds.size() == 0 ? new ArrayList<>() : ucOpenUserInfoService.query().in("member_id", memberIds).eq("source", "wxCp").list();
        List<UcOpenUserInfo> userInfoList = memberIds.size() == 0 ? new ArrayList<>() : ucOpenUserInfoService.query().in("member_id", memberIds).list();
        if (ToolUtil.isNotEmpty(userInfoList)) {
            for (UcOpenUserInfo ucOpenUserInfo : userInfoList) {
                uuIds.add(ucOpenUserInfo.getUuid());
            }
        }

        return uuIds;
    }
    private List<UserId2WxOpenId> userIds2UuIdsObject(List<Long> userIds) {
        List<UserId2WxOpenId> userId2WxOpenIds = new ArrayList<>();
        List<WxuserInfo> wxuserInfos = userIds.size() == 0 ? new ArrayList<>() : wxuserInfoService.query().in("user_id", userIds).orderByDesc("create_time").groupBy("user_id").list();
//        List<WxuserInfo> wxuserInfos = userIds.size() == 0 ? new ArrayList<>() : wxuserInfoService.query().in("user_id", userIds).orderByDesc("create_time").groupBy("user_id").list();

        List<Long> memberIds = new ArrayList<>();
        if (ToolUtil.isNotEmpty(wxuserInfos)) {
            for (WxuserInfo wxuserInfo : wxuserInfos) {
                memberIds.add(wxuserInfo.getMemberId());
            }
        }

//        List<UcOpenUserInfo> userInfoList = memberIds.size() == 0 ? new ArrayList<>() : ucOpenUserInfoService.query().in("member_id", memberIds).eq("source", "wxCp").list();
        List<UcOpenUserInfo> userInfoList = memberIds.size() == 0 ? new ArrayList<>() : ucOpenUserInfoService.query().in("member_id", memberIds).eq("source", "wxCp").list();
        if (ToolUtil.isNotEmpty(userInfoList)) {
            for (Long userId : userIds) {
                for (WxuserInfo wxuserInfo : wxuserInfos) {
                    for (UcOpenUserInfo ucOpenUserInfo : userInfoList) {
                        if (userId.equals(wxuserInfo.getUserId()) && wxuserInfo.getMemberId().equals(ucOpenUserInfo.getMemberId())){
                            UserId2WxOpenId userId2WxOpenId = new UserId2WxOpenId();
                            userId2WxOpenId.setUserId(userId);
                            userId2WxOpenId.setMemberId(wxuserInfo.getMemberId());
                            userId2WxOpenId.setOpenId(ucOpenUserInfo.getUuid());
                            userId2WxOpenIds.add(userId2WxOpenId);
                        }
                    }
                }
            }
        }

        return userId2WxOpenIds;
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
        logger.info("调用推送 推送人： " +JSON.toJSONString(userIds));
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
//            MessageEntity messageEntity = new MessageEntity();
//            messageEntity.setType(MessageType.CP);
//            WxCpMessage wxCpMessage = new WxCpMessage();
//            wxCpMessage.setMsgType("markdown");
//            wxCpMessage.setContent(this.getContent(markDownTemplate));
//            List<String> userIds = userIds2UuIds(markDownTemplate.getUserIds());
        MarkDownTemplate itemAndDescription = this.selectTitle(markDownTemplate);

        List<UserId2WxOpenId> userId2WxOpenIds = userIds2UuIdsObject(markDownTemplate.getUserIds());
        if (ToolUtil.isNotEmpty(userId2WxOpenIds)) {

            List<Message> messageEntities = new ArrayList<>();
            for (Long userId : markDownTemplate.getUserIds()) {
                Message message = new Message();
                message.setTime(new DateTime());
                message.setTitle(itemAndDescription.getTitle());
                message.setContent(itemAndDescription.getItems());
                message.setType(markDownTemplate.getType());
                message.setUserId(userId);
                message.setSort(0L);
                message.setSourceId(markDownTemplate.getSourceId());
                message.setSource(markDownTemplate.getSource());
//                message.setUrl(markDownTemplate.getUrl());
                if (ToolUtil.isNotEmpty(markDownTemplate.getCreateUser())) {
                    message.setPromoter(markDownTemplate.getCreateUser());
                }
                messageEntities.add(message);
//                messageEntity.setMessage(message);
                logger.info("铃铛保存" + JSON.toJSONString(message));
//                messageProducer.sendMessage(messageEntity, 100);
            }
            messageService.saveBatch(messageEntities);



            for (Message message : messageEntities) {
                /**
                 * url换成messageId
                 */
                markDownTemplate.setUrl(mobileService.getMobileConfig().getApiUrl() + "/message/jump?id=" + message.getMessageId());
                MessageEntity messageEntity = new MessageEntity();
                messageEntity.setType(MessageType.CP);
                WxCpMessage wxCpMessage = new WxCpMessage();
                wxCpMessage.setMsgType("markdown");
                wxCpMessage.setContent(this.getContent(markDownTemplate,itemAndDescription));
                /**
                 * 匹配openId
                 */
                for (UserId2WxOpenId userId2WxOpenId : userId2WxOpenIds) {
                    if (userId2WxOpenId.getUserId().equals(message.getUserId())){
                        wxCpMessage.setToUser(userId2WxOpenId.getOpenId());
                        break;
                    }
                }
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
        }
    }

    public String getContent(MarkDownTemplate markDownTemplate,MarkDownTemplate markDownTemplate1) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("**").append(markDownTemplate1.getTitle()).append("**").append("\n\n");

        if (ToolUtil.isNotEmpty(markDownTemplate1.getItems())) {
            stringBuffer.append(markDownTemplate1.getItems()).append("\n\n");
        }
        if (ToolUtil.isNotEmpty(markDownTemplate1.getDescription())) {
            stringBuffer.append(markDownTemplate1.getDescription()).append("\n\n").append("\n\n");
        }
        if (ToolUtil.isNotEmpty(markDownTemplate.getCreateTime())) {
            String date = DateUtil.format(markDownTemplate1.getCreateTime(), "yyyy/MM/dd HH:mm:ss");
            stringBuffer.append(date).append("\n \n");
        }
        if (ToolUtil.isNotEmpty(markDownTemplate1.getUrl())) {
            stringBuffer.append("[点击查看详情](").append(markDownTemplate1.getUrl()).append(")");
        }
        return stringBuffer.toString();
    }

    MarkDownTemplate selectTitle(MarkDownTemplate markDownTemplate) {
        if (ToolUtil.isEmpty(markDownTemplate.getCreateTime())) {
            markDownTemplate.setCreateTime(new Date());
        }
        if (ToolUtil.isNotEmpty(markDownTemplate.getFunction())) {
            Map<String, String> data = new HashMap<>();
            if (ToolUtil.isNotEmpty(markDownTemplate.getTaskId())) {
                data = activitiProcessTaskService.getSendData(markDownTemplate.getTaskId());
                if (ToolUtil.isNotEmpty(data.get("items"))) {
                    markDownTemplate.setItems(data.get("items"));
                }
            }
            switch (markDownTemplate.getFunction()) {
                case audit:
                case refuse:
                case send:
                case pickSend:
                case warning:
                case transfer:
                    markDownTemplate.setTitle(markDownTemplate.getFunction().getEnumName());
                    break;
                case atPerson:
                case toPerson:
                case forward:
                    String enumName = markDownTemplate.getFunction().getEnumName();
                    if (ToolUtil.isNotEmpty(markDownTemplate.getUserId())) {
                        User user = userService.getById(markDownTemplate.getUserId());
                        enumName = enumName.replace("${userName}", user.getName());
                        markDownTemplate.setTitle(enumName);
                    }
                    break;
                case done:
                case action:
                    try {
                        markDownTemplate.setTitle(markDownTemplate.getFunction().getEnumName().replace("${functionName}", data.get("function")));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;
                default:
                    break;
            }
            if (ToolUtil.isNotEmpty(data.get("description"))) {
                markDownTemplate.setDescription(data.get("description"));
            }
            if (ToolUtil.isNotEmpty(data.get("coding"))) {
                markDownTemplate.setCoding(data.get("coding"));
            }

        }
        return markDownTemplate;
    }

}
