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

import java.util.Map;

@Configuration
public class DirectQueueConfig {
    @Value("rabbitmq.prefix")
    private static String prefix;
    protected static final Logger logger = LoggerFactory.getLogger(DirectQueueConfig.class);

    @Value("${spring.rabbitmq.prefix}")
    private String tmpPrefix;


    private static String mqPrefix;

    public final static String MESSAGE_REAL_EXCHANGE = mqPrefix + ".message.real.topicExchange";
    public final static String MESSAGE_REAL_ROUTE = mqPrefix + ".message.real.route";

    public final static String $MESSAGE_REAL_QUEUE = ".message.real.topicExchange";
    public final static String MESSAGE_REAL_QUEUE = mqPrefix + $MESSAGE_REAL_QUEUE;


    public final static String MESSAGE_DELAY_EXCHANGE = mqPrefix + ".message.delay.topicExchange";
    public final static String MESSAGE_DELAY_ROUTE = ".message.delay.route";
    public final static String MESSAGE_DELAY_QUEUE = mqPrefix + ".message.delay.topicExchange";

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
        logger.info(getMessageDelayRoute());
        return new Queue(getMessageDelayRoute());
    }


    @Bean
    public Queue messageDelayQueue() {

        Map<String, Object> argsMap = Maps.newHashMap();
        argsMap.put("x-dead-letter-exchange", MESSAGE_REAL_EXCHANGE); //真正的交换机
        argsMap.put("x-dead-letter-routing-key", MESSAGE_REAL_ROUTE); //真正的路由键
        return new Queue(tmpPrefix + MESSAGE_DELAY_QUEUE, true, false, false, argsMap);
    }

    /**
     * 声明交换机
     *
     * @return TopicExchange
     */
    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(MESSAGE_REAL_EXCHANGE);
    }

    @Bean
    public TopicExchange topicDelayExchange() {
        return new TopicExchange(MESSAGE_DELAY_EXCHANGE);
    }

    /**
     * 绑定交换机
     */
    @Bean
    Binding bindingTopicExchange() {
        logger.info("绑定队列");
        return BindingBuilder.bind(messageQueue()).to(topicExchange()).with(MESSAGE_REAL_ROUTE);
    }

    @Bean
    Binding bindingTopicDelayExchange() {
        logger.info("绑定延迟队列");
        return BindingBuilder.bind(messageDelayQueue()).to(topicDelayExchange()).with(MESSAGE_DELAY_ROUTE);
    }

    public static String getMessageDelayRoute() {
        return mqPrefix + MESSAGE_DELAY_ROUTE;
    }
}
