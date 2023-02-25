package cn.atsoft.dasheng.producer;

import cn.atsoft.dasheng.config.RestDirectQueueConfig;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.entity.MessageEntity;
import cn.atsoft.dasheng.entity.MicroServiceEntity;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RestMessageProducer {
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
            //测试加入唯一key
            String randomString = ToolUtil.getRandomString(5);
            String s = messageEntity.getCpData().getDescription() + randomString;
            logger.info("发送" + messageEntity.getCpData().getDescription());
            rabbitTemplate.convertAndSend(RestDirectQueueConfig.getMessageRealExchange(), RestDirectQueueConfig.getMessageRealRoute(), JSON.toJSONString(messageEntity));

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
            rabbitTemplate.convertAndSend(RestDirectQueueConfig.getMessageRealExchange(), RestDirectQueueConfig.getMessageRealRoute(), JSON.toJSONString(messageEntity), message -> {
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
            //测试加入唯一key
            String randomString = ToolUtil.getRandomString(5);
            logger.info("内部调用创建:" + microServiceEntity.getType()+"/"+microServiceEntity.getOperationType()+"/"+randomString);
            rabbitTemplate.convertAndSend(RestDirectQueueConfig.getMicroServiceRealExchange(), RestDirectQueueConfig.getMicroServiceRealRoute(), JSON.toJSONString(microServiceEntity));

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
            rabbitTemplate.convertAndSend(RestDirectQueueConfig.getMicroServiceRealExchange(), RestDirectQueueConfig.getMicroServiceRealRoute(), JSON.toJSONString(microServiceEntity), message -> {
                MessageProperties messageProperties = message.getMessageProperties();
                messageProperties.setExpiration(ttl.toString());//单位是毫秒
                return message;
            });
        }
    }



//    /**
//     * 普通队列
//     *
//     * @param auditEntity 内容对象
//     */
//    public void auditMessageDo(AuditEntity auditEntity) {
//        auditEntity.setTimes(1 + auditEntity.getTimes());
//        if (ToolUtil.isNotEmpty(auditEntity.getMaxTimes()) && auditEntity.getTimes() <= auditEntity.getMaxTimes()) {
//            /**
//             * 打印日志
//             */
//            Long userId = LoginContextHolder.getContext().getUserId();
//            auditEntity.setLoginUserId(userId);
//            String randomString = ToolUtil.getRandomString(5);
//            StringBuffer sb = new StringBuffer();
//            sb.append("发出审批队列").append(auditEntity).append("/").append(randomString);
//            logger.info(sb.toString());
//            /**
//             * 发送
//             */
//            rabbitTemplate.convertAndSend(RestDirectQueueConfig.getAuditRealExchange(), RestDirectQueueConfig.getAuditRealRoute(), JSON.toJSONString(auditEntity));
//
//        }
//    }

//    /**
//     * 延迟队列
//     *
//     * @param auditEntity 消息
//     * @param ttl           延迟 毫秒
//     */
//    public void auditMessageDo(AuditEntity auditEntity, Integer ttl) {
//
//        auditEntity.setExpiration(ttl);
//        auditEntity.setTimes(1 + auditEntity.getTimes());
//        /**
//         * 打印日志
//         */
//        String randomString = ToolUtil.getRandomString(5);
//        StringBuffer sb = new StringBuffer();
//        sb.append("发出审批队列").append(auditEntity).append("/").append(randomString);
//        logger.info(sb.toString());
//        /**
//         * 发送
//         */
//        if (ToolUtil.isNotEmpty(auditEntity.getMaxTimes()) && auditEntity.getTimes() <= auditEntity.getMaxTimes()) {
//            rabbitTemplate.convertAndSend(RestDirectQueueConfig.getAuditRealExchange(), RestDirectQueueConfig.getAuditRealRoute(), JSON.toJSONString(auditEntity), message -> {
//                MessageProperties messageProperties = message.getMessageProperties();
//                messageProperties.setExpiration(ttl.toString());//单位是毫秒
//                return message;
//            });
//        }
//    }

//    public void remarksServiceDo(RemarksEntity remarksEntity) {
//        remarksEntity.setTimes(1 + remarksEntity.getTimes());
//        if (ToolUtil.isNotEmpty(remarksEntity.getMaxTimes()) && remarksEntity.getTimes() <= remarksEntity.getMaxTimes()) {
//            /**
//             * 打印日志
//             */
//            String randomString = ToolUtil.getRandomString(5);
//            StringBuffer sb = new StringBuffer();
//            sb.append("发出审批队列").append(remarksEntity).append("/").append(randomString);
//            logger.info(sb.toString());
//            /**
//             * 发送
//             */
//            rabbitTemplate.convertAndSend(RestDirectQueueConfig.getRemarksRealExchange(), RestDirectQueueConfig.getRemarksRealRoute(), JSON.toJSONString(remarksEntity));
//
//        }
//    }






}
