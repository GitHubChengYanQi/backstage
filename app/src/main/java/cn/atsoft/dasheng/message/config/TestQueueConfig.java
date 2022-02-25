package cn.atsoft.dasheng.message.config;

import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class TestQueueConfig {

    protected static final Logger logger = LoggerFactory.getLogger(TestQueueConfig.class);

    @Value("${spring.rabbitmq.testPrefix}")
    private String tmpPrefix;


    private static String mqPrefix;

    public final static String MESSAGE_REAL_EXCHANGE = ".message.real.topicExchange";
    public final static String MESSAGE_REAL_ROUTE = ".message.real.route";
    public final static String MESSAGE_REAL_QUEUE = ".message.real.queue";


    public final static String MESSAGE_DELAY_EXCHANGE = ".message.delay.topicExchange";
    public final static String MESSAGE_DELAY_ROUTE = ".message.delay.route";
    public final static String MESSAGE_DELAY_QUEUE = ".message.delay.queue";





    @Bean
    public void Testinit() {
        mqPrefix = tmpPrefix;
    }

    /**
     * 声明队列
     *
     * @return Queue
     */
    @Bean
    public Queue TestmessageQueue() {
        logger.info(mqPrefix);
        logger.info(getMessageRealQueue());
        return new Queue(getMessageRealQueue());
    }


    @Bean
    public Queue TestmessageDelayQueue() {

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
    public TopicExchange TesttopicExchange() {
        return new TopicExchange(getMessageRealExchange());
    }

    @Bean
    public TopicExchange TesttopicDelayExchange() {
        return new TopicExchange(getMessageDelayExchange());
    }

    /**
     * 绑定交换机
     */
    @Bean
    Binding TestbindingTopicExchange() {
        logger.info("绑定队列");
        return BindingBuilder.bind(TestmessageQueue()).to(TesttopicExchange()).with(getMessageRealRoute());
    }

    @Bean
    Binding TestbindingTopicDelayExchange() {
        logger.info("绑定延迟队列");
        return BindingBuilder.bind(TestmessageDelayQueue()).to(TesttopicDelayExchange()).with(getMessageRealRoute());
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
}
