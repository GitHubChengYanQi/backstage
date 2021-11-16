package cn.atsoft.dasheng.message.producer;

import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.message.entity.MessageEntity;
import com.alibaba.fastjson.JSON;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static cn.atsoft.dasheng.message.config.DirectQueueConfig.*;

@Component
public class MessageProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 普通队列
     * @param messageEntity 消息内容
     */
    public void sendMessage(MessageEntity messageEntity){
        messageEntity.setTimes(1+ messageEntity.getTimes());
        if(ToolUtil.isNotEmpty(messageEntity.getMaxTimes()) && messageEntity.getTimes()<= messageEntity.getMaxTimes()) {
            rabbitTemplate.convertAndSend(MESSAGE_REAL_EXCHANGE, MESSAGE_REAL_ROUTE, JSON.toJSONString(messageEntity));
        }
    }

    /**
     * 延迟队列
     * @param messageEntity 消息
     * @param ttl 延迟 毫秒
     */
    public void sendMessage(MessageEntity messageEntity, Integer ttl){

        messageEntity.setExpiration(ttl);
        messageEntity.setTimes(1+ messageEntity.getTimes());
        if(ToolUtil.isNotEmpty(messageEntity.getMaxTimes()) && messageEntity.getTimes()<= messageEntity.getMaxTimes()){
            rabbitTemplate.convertAndSend(MESSAGE_DELAY_EXCHANGE,MESSAGE_DELAY_ROUTE,JSON.toJSONString(messageEntity), message -> {
                MessageProperties messageProperties = message.getMessageProperties();
                messageProperties.setExpiration(ttl.toString());//单位是毫秒
                return message;
            });
        }


    }
}
