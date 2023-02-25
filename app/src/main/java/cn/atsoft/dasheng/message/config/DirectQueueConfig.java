package cn.atsoft.dasheng.message.config;

import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.Map;

//@Configuration
public class DirectQueueConfig {

    protected static final Logger logger = LoggerFactory.getLogger(DirectQueueConfig.class);

    @Value("${spring.rabbitmq.prefix}")
    private String tmpPrefix;


    private static String mqPrefix;

    public final static String MESSAGE_REAL_EXCHANGE = ".message.real.topicExchange";
    public final static String MESSAGE_REAL_ROUTE = ".message.real.route";
    public final static String MESSAGE_REAL_QUEUE = ".message.real.queue";


    public final static String MESSAGE_DELAY_EXCHANGE = ".message.delay.topicExchange";
    public final static String MESSAGE_DELAY_ROUTE = ".message.delay.route";
    public final static String MESSAGE_DELAY_QUEUE = ".message.delay.queue";


    /**
     * 内部调用消息队列
     */
    public final static String MICROSERVICE_REAL_EXCHANGE = ".microService.real.topicExchange";
    public final static String MICROSERVICE_REAL_ROUTE = ".microService.real.route";
    public final static String MICROSERVICE_REAL_QUEUE = ".microService.real.queue";


    public final static String MICROSERVICE_DELAY_EXCHANGE = ".microService.delay.topicExchange";
    public final static String MICROSERVICE_DELAY_ROUTE = ".microService.delay.route";
    public final static String MICROSERVICE_DELAY_QUEUE = ".microService.delay.queue";

    /**
     * 审批流消息队列
     */

    public final static String AUDIT_REAL_EXCHANGE = ".auditService.real.topicExchange";
    public final static String AUDIT_REAL_ROUTE = ".auditService.real.route";
    public final static String AUDIT_REAL_QUEUE = ".auditService.real.queue";


    public final static String AUDIT_DELAY_EXCHANGE = ".auditService.delay.topicExchange";
    public final static String AUDIT_DELAY_ROUTE = ".auditService.delay.route";
    public final static String AUDIT_DELAY_QUEUE = ".auditService.delay.queue";


    /**
     * 审批流消息队列
     */

    public final static String REMARKS_REAL_EXCHANGE = ".remarkService.real.topicExchange";
    public final static String REMARKS_REAL_ROUTE = ".remarkService.real.route";
    public final static String REMARKS_REAL_QUEUE = ".remarkService.real.queue";


    public final static String REMARKS_DELAY_EXCHANGE = ".remarkService.delay.topicExchange";
    public final static String REMARKS_DELAY_ROUTE = ".remarkService.delay.route";
    public final static String REMARKS_DELAY_QUEUE = ".remarkService.delay.queue";





    @Bean
    public void init() {
        mqPrefix = tmpPrefix;
    }

    /**
     * 声明队列
     *
     * @return Queue
     */
    @Bean
    public Queue messageQueue() {
        logger.info(mqPrefix);
        logger.info(getMessageRealQueue());
        return new Queue(getMessageRealQueue());
    }


    @Bean
    public Queue messageDelayQueue() {

        Map<String, Object> argsMap = Maps.newHashMap();
        argsMap.put("x-dead-letter-exchange", getMessageRealExchange()); //真正的交换机
        argsMap.put("x-dead-letter-routing-key", getMessageRealRoute()); //真正的路由键
        return new Queue(getMessageDelayQueue() , true, false, false, argsMap);
    }

    /**
     * 声明交换机
     *
     * @return TopicExchange
     */
    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(getMessageRealExchange());
    }

    @Bean
    public TopicExchange topicDelayExchange() {
        return new TopicExchange(getMessageDelayExchange());
    }

    /**
     * 绑定交换机
     */
    @Bean
    Binding bindingTopicExchange() {
        logger.info("绑定队列");
        return BindingBuilder.bind(messageQueue()).to(topicExchange()).with(getMessageRealRoute());
    }

    @Bean
    Binding bindingTopicDelayExchange() {
        logger.info("绑定延迟队列");
        return BindingBuilder.bind(messageDelayQueue()).to(topicDelayExchange()).with(getMessageRealRoute());
    }

    public static String getMessageDelayRoute() {
        return mqPrefix + MESSAGE_DELAY_ROUTE;
    }

    public static String getMessageRealExchange() {
        return mqPrefix + MESSAGE_REAL_EXCHANGE;
    }

    public static String getMessageRealRoute() {
        return mqPrefix + MESSAGE_REAL_ROUTE;
    }

    public static String getMessageRealQueue() {
        return mqPrefix + MESSAGE_REAL_QUEUE;
    }

    public static String getMessageDelayExchange() {
        return mqPrefix + MESSAGE_DELAY_EXCHANGE;
    }

    public static String getMessageDelayQueue() {
        return mqPrefix + MESSAGE_DELAY_QUEUE;
    }



    /**
     * 声明内部调用服务队列
     *
     * @return Queue
     */
    @Bean
    public Queue microServiceQueue() {
        logger.info(mqPrefix);
        logger.info(getMicroServiceRealQueue());
        return new Queue(getMicroServiceRealQueue());
    }


    @Bean
    public Queue microServiceDelayQueue() {

        Map<String, Object> argsMap = Maps.newHashMap();
        argsMap.put("x-dead-letter-exchange", getMicroServiceRealExchange()); //真正的交换机
        argsMap.put("x-dead-letter-routing-key", getMicroServiceRealRoute()); //真正的路由键
        return new Queue(getMicroServiceDelayQueue() , true, false, false, argsMap);
    }

    /**
     * 声明交换机
     *
     * @return TopicExchange
     */
    @Bean
    public TopicExchange microServiceExchange() {
        return new TopicExchange(getMicroServiceRealExchange());
    }

    @Bean
    public TopicExchange microServiceDelayExchange() {
        return new TopicExchange(getMicroServiceDelayExchange());
    }

    /**
     * 绑定交换机
     */
    @Bean
    Binding microServiceTopicExchange() {
        logger.info("绑定队列");
        return BindingBuilder.bind(microServiceQueue()).to(microServiceExchange()).with(getMicroServiceRealRoute());
    }

    @Bean
    Binding microServiceTopicDelayExchange() {
        logger.info("绑定延迟队列");
        return BindingBuilder.bind(microServiceDelayQueue()).to(microServiceDelayExchange()).with(getMicroServiceRealRoute());
    }


    public static String getMicroServiceRealExchange() {
        return mqPrefix + MICROSERVICE_REAL_EXCHANGE;
    }

    public static String getMicroServiceRealRoute() {
        return mqPrefix + MICROSERVICE_REAL_ROUTE;
    }

    public static String getMicroServiceRealQueue() {
        return mqPrefix + MICROSERVICE_REAL_QUEUE;
    }

    public static String getMicroServiceDelayRoute() {
        return mqPrefix + MICROSERVICE_DELAY_ROUTE;
    }

    public static String getMicroServiceDelayExchange() {
        return mqPrefix + MICROSERVICE_DELAY_EXCHANGE;
    }

    public static String getMicroServiceDelayQueue() {
        return mqPrefix + MICROSERVICE_DELAY_QUEUE;
    }




    /**
     * 声明审批流服务队列
     *
     * @return Queue
     */
    @Bean
    public Queue auditQueue() {
        logger.info(mqPrefix);
        logger.info(getAuditRealQueue());
        return new Queue(getAuditRealQueue());
    }


    @Bean
    public Queue auditDelayQueue() {

        Map<String, Object> argsMap = Maps.newHashMap();
        argsMap.put("x-dead-letter-exchange", getAuditRealExchange()); //真正的交换机
        argsMap.put("x-dead-letter-routing-key", getAuditRealRoute()); //真正的路由键
        return new Queue(getAuditDelayExchange() , true, false, false, argsMap);
    }

    /**
     * 声明交换机
     *
     * @return TopicExchange
     */
    @Bean
    public TopicExchange auditExchange() {
        return new TopicExchange(getAuditRealExchange());
    }

    @Bean
    public TopicExchange auditDelayExchange() {
        return new TopicExchange(getAuditDelayExchange());
    }

    /**
     * 绑定交换机
     */
    @Bean
    Binding auditServiceTopicExchange() {
        logger.info("绑定队列");
        return BindingBuilder.bind(auditQueue()).to(auditExchange()).with(getAuditRealRoute());
    }

    @Bean
    Binding auditServiceTopicDelayExchange() {
        logger.info("绑定延迟队列");
        return BindingBuilder.bind(auditDelayQueue()).to(auditDelayExchange()).with(getAuditRealRoute());
    }


    public static String getAuditRealExchange() {
        return mqPrefix + AUDIT_REAL_EXCHANGE;
    }

    public static String getAuditRealRoute() {
        return mqPrefix + AUDIT_REAL_ROUTE;
    }

    public static String getAuditRealQueue() {
        return mqPrefix + AUDIT_REAL_QUEUE;
    }

    public static String getAuditDelayRoute() {
        return mqPrefix + AUDIT_DELAY_ROUTE;
    }

    public static String getAuditDelayExchange() {
        return mqPrefix + AUDIT_DELAY_EXCHANGE;
    }

    public static String getAuditDelayQueue() {
        return mqPrefix + AUDIT_DELAY_QUEUE;
    }




    //=========================单据动态===========================

    @Bean
    public Queue remarksQueue() {
        logger.info(mqPrefix);
        logger.info(getRemarksRealQueue());
        return new Queue(getRemarksRealQueue());
    }


    @Bean
    public Queue remarksDelayQueue() {

        Map<String, Object> argsMap = Maps.newHashMap();
        argsMap.put("x-dead-letter-exchange", getRemarksRealExchange()); //真正的交换机
        argsMap.put("x-dead-letter-routing-key", getRemarksRealRoute()); //真正的路由键
        return new Queue(getRemarksDelayExchange() , true, false, false, argsMap);
    }


    /**
     * 声明交换机
     *
     * @return TopicExchange
     */
    @Bean
    public TopicExchange remarksExchange() {
        return new TopicExchange(getRemarksRealExchange());
    }

    @Bean
    public TopicExchange remarksDelayExchange() {
        return new TopicExchange(getRemarksDelayExchange());
    }

    /**
     * 绑定交换机
     */
    @Bean
    Binding remarksServiceTopicExchange() {
        logger.info("绑定队列");
        return BindingBuilder.bind(remarksQueue()).to(remarksExchange()).with(getRemarksRealRoute());
    }

    @Bean
    Binding remarksServiceTopicDelayExchange() {
        logger.info("绑定延迟队列");
        return BindingBuilder.bind(remarksDelayQueue()).to(remarksDelayExchange()).with(getRemarksRealRoute());
    }








    public static String getRemarksRealExchange() {
        return mqPrefix + REMARKS_REAL_EXCHANGE;
    }

    public static String getRemarksRealRoute() {
        return mqPrefix + REMARKS_REAL_ROUTE;
    }

    public static String getRemarksRealQueue() {
        return mqPrefix + REMARKS_REAL_QUEUE;
    }

    public static String getRemarksDelayRoute() {
        return mqPrefix + REMARKS_DELAY_ROUTE;
    }

    public static String getRemarksDelayExchange() {
        return mqPrefix + REMARKS_DELAY_EXCHANGE;
    }

    public static String getRemarksDelayQueue() {
        return mqPrefix + REMARKS_DELAY_QUEUE;
    }
}
