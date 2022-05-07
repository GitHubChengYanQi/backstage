package cn.atsoft.dasheng.message.producer;

import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.message.config.DirectQueueConfig;
import cn.atsoft.dasheng.message.entity.MessageEntity;
import cn.atsoft.dasheng.message.entity.MicroServiceEntity;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessageProducer {
    private Logger logger = LoggerFactory.getLogger(MessageEntity.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;


    /**
     * 普通队列
     *
     * @param messageEntity 消息内容
     */
    public void sendMessage(MessageEntity messageEntity) {
        messageEntity.setTimes(1 + messageEntity.getTimes());
        if (ToolUtil.isNotEmpty(messageEntity.getMaxTimes()) && messageEntity.getTimes() <= messageEntity.getMaxTimes()) {
            messageEntity.getCpData().setDescription(messageEntity.getCpData().getDescription());
            //TODO 测试加入唯一key
            String randomString = ToolUtil.getRandomString(5);
            String s = messageEntity.getCpData().getDescription() + randomString;
            logger.info("发送" + messageEntity.getCpData().getDescription());
            rabbitTemplate.convertAndSend(DirectQueueConfig.getMessageRealExchange(), DirectQueueConfig.getMessageRealRoute(), JSON.toJSONString(messageEntity));

        }
    }

    /**
     * 延迟队列
     *
     * @param messageEntity 消息
     * @param ttl           延迟 毫秒
     */
    public void sendMessage(MessageEntity messageEntity, Integer ttl) {

        messageEntity.setExpiration(ttl);
        messageEntity.setTimes(1 + messageEntity.getTimes());
        if (ToolUtil.isNotEmpty(messageEntity.getMaxTimes()) && messageEntity.getTimes() <= messageEntity.getMaxTimes()) {
            rabbitTemplate.convertAndSend(DirectQueueConfig.getMessageRealExchange(), DirectQueueConfig.getMessageRealRoute(), JSON.toJSONString(messageEntity), message -> {
                MessageProperties messageProperties = message.getMessageProperties();
                messageProperties.setExpiration(ttl.toString());//单位是毫秒
                return message;
            });
        }


    }

    /**
     * 普通队列
     *
     * @param microServiceEntity 内容对象
     */
    public void microService(MicroServiceEntity microServiceEntity) {
        microServiceEntity.setTimes(1 + microServiceEntity.getTimes());
        if (ToolUtil.isNotEmpty(microServiceEntity.getMaxTimes()) && microServiceEntity.getTimes() <= microServiceEntity.getMaxTimes()) {
            //TODO 测试加入唯一key
            String randomString = ToolUtil.getRandomString(5);
            logger.info("内部调用创建:" + microServiceEntity.getType()+"/"+microServiceEntity.getOperationType()+"/"+randomString);
            rabbitTemplate.convertAndSend(DirectQueueConfig.getMicroServiceRealExchange(), DirectQueueConfig.getMicroServiceRealRoute(), JSON.toJSONString(microServiceEntity));

        }
    }

    /**
     * 延迟队列
     *
     * @param microServiceEntity 消息
     * @param ttl           延迟 毫秒
     */
    public void microService(MicroServiceEntity microServiceEntity, Integer ttl) {

        microServiceEntity.setExpiration(ttl);
        microServiceEntity.setTimes(1 + microServiceEntity.getTimes());
        if (ToolUtil.isNotEmpty(microServiceEntity.getMaxTimes()) && microServiceEntity.getTimes() <= microServiceEntity.getMaxTimes()) {
            rabbitTemplate.convertAndSend(DirectQueueConfig.getMicroServiceRealExchange(), DirectQueueConfig.getMicroServiceRealRoute(), JSON.toJSONString(microServiceEntity), message -> {
                MessageProperties messageProperties = message.getMessageProperties();
                messageProperties.setExpiration(ttl.toString());//单位是毫秒
                return message;
            });
        }


    }
}
