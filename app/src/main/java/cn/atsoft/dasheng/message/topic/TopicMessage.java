package cn.atsoft.dasheng.message.topic;

import cn.atsoft.dasheng.app.entity.BusinessTrack;
import cn.atsoft.dasheng.app.model.params.MessageParam;
import cn.atsoft.dasheng.app.service.BusinessTrackService;
import cn.atsoft.dasheng.app.service.MessageService;
import cn.atsoft.dasheng.appBase.service.WxCpService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.message.entity.MessageEntity;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.Channel;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.cp.bean.message.WxCpMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;


import static cn.atsoft.dasheng.message.config.DirectQueueConfig.MESSAGE_REAL_QUEUE;


@Component
public class TopicMessage {
    @Autowired
    private WxCpService wxCpService;

    @Autowired
    private MessageService messageService;

    protected static final Logger logger = LoggerFactory.getLogger(TopicMessage.class);

    @RabbitListener(queues = "${spring.rabbitmq.prefix}" + MESSAGE_REAL_QUEUE)
    public void readMessage(Message message, Channel channel) throws IOException {
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
//        logger.info(new String(message.getBody()));
        MessageEntity messageEntity = JSON.parseObject(message.getBody(), MessageEntity.class);
        switch (messageEntity.getType()) {
            case CP:
                try {
                    wxCpService.getWxCpClient().getMessageService().send(messageEntity.getCpData());
                    logger.info("接收" + JSON.toJSONString(messageEntity.getCpData().getDescription()));
                } catch (WxErrorException e) {
                    e.printStackTrace();
                }
                break;

            case MP:
                break;

            case SMS:
                break;

            case MESSAGE:
                if (ToolUtil.isNotEmpty(messageEntity.getMessage().getSource()) && ToolUtil.isNotEmpty(messageEntity.getMessage().getSourceId())) {
                    messageService.save(messageEntity.getMessage());
                    logger.info("小铃铛保存" + JSON.toJSONString(messageEntity.getCpData().getDescription()));
                }
                break;
            default:
        }
    }
}
