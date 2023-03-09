package cn.atsoft.dasheng.message.topic;

import cn.atsoft.dasheng.app.service.MessageService;
import cn.atsoft.dasheng.appBase.service.WxCpService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.message.entity.AuditEntity;
import cn.atsoft.dasheng.message.entity.MessageEntity;
import cn.atsoft.dasheng.message.entity.MicroServiceEntity;
import cn.atsoft.dasheng.message.entity.RemarksEntity;
import cn.atsoft.dasheng.message.service.AuditMessageService;
import cn.atsoft.dasheng.message.service.MicroMessageService;
import cn.atsoft.dasheng.message.service.RemarksMessageService;
import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.Channel;
import me.chanjar.weixin.common.error.WxErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static cn.atsoft.dasheng.message.config.DirectQueueConfig.*;


@Component
public class TopicMessage {
    @Autowired
    private WxCpService wxCpService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private MicroMessageService microMessageService;

    @Autowired
    private AuditMessageService auditMessageService;

    @Autowired
    private RemarksMessageService remarksMessageService;

    protected static final Logger logger = LoggerFactory.getLogger(TopicMessage.class);

    /**
     * 微信消息推送
     * @param message
     * @param channel
     * @throws IOException
     */
    @RabbitListener(queues = "${spring.rabbitmq.prefix}" + MESSAGE_REAL_QUEUE)
    public void readMessage(Message message, Channel channel) throws IOException {
//        logger.info(new String(message.getBody()));
        MessageEntity messageEntity = JSON.parseObject(message.getBody(), MessageEntity.class);
        switch (messageEntity.getType()) {
            case CP:
//                try {
//                    wxCpService.getWxCpClient().getMessageService().send(messageEntity.getCpData());
                    logger.info("接收" + JSON.toJSONString(messageEntity.getCpData().getContent()));
//                } catch (WxErrorException e) {
//                    e.printStackTrace();
//                }
                break;

            case MP:


                

            case SMS:
                break;

            case MESSAGE:
                if (ToolUtil.isNotEmpty(messageEntity.getMessage().getSource()) && ToolUtil.isNotEmpty(messageEntity.getMessage().getSourceId())) {
//                    messageService.save(messageEntity.getMessage());
                    logger.info("小铃铛保存" + JSON.toJSONString(messageEntity.getCpData().getContent()));
                }
                break;
            default:
        }
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);

    }

    /**
     * 内部调用创建单据等
     * @param message
     * @param channel
     * @throws IOException
     */
    @RabbitListener(queues = "${spring.rabbitmq.prefix}" + MICROSERVICE_REAL_QUEUE)
    public void readMicroService(Message message, Channel channel) throws IOException {
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        MicroServiceEntity microServiceEntity = JSON.parseObject(message.getBody(), MicroServiceEntity.class);
        microMessageService.microServiceDo(microServiceEntity);
        logger.info("内部调用创建单据:"+microServiceEntity.getType()+"/"+microServiceEntity.getOperationType());
    }

    /**
     * 审批流程
     * @param message
     * @param channel
     * @throws IOException
     */
    @RabbitListener(queues = "${spring.rabbitmq.prefix}" + AUDIT_REAL_QUEUE)
    public void readAudit(Message message, Channel channel) throws IOException {
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        AuditEntity auditEntity = JSON.parseObject(message.getBody(), AuditEntity.class);
        auditMessageService.auditDo(auditEntity);
        logger.info("审批队列:"+auditEntity.getAuditType()+"/"+auditEntity.getAuditType());
    }

    /**
     * 单据动态  评论等
     * @param message
     * @param channel
     * @throws IOException
     */
    @RabbitListener(queues = "${spring.rabbitmq.prefix}" + REMARKS_REAL_QUEUE)
    public void readRemarks(Message message, Channel channel) throws IOException {
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        RemarksEntity remarksEntity = JSON.parseObject(message.getBody(), RemarksEntity.class);
        remarksMessageService.remarksServiceDo(remarksEntity);
        logger.info("单据动态:"+remarksEntity.getOperationType()+"/"+remarksEntity.getRemarksParam());
    }
}
