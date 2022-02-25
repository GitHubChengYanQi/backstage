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
public class MicroServiceDirectQueueConfig {
    protected static final Logger logger = LoggerFactory.getLogger(DirectQueueConfig.class);

    @Value("${spring.rabbitmq.prefix}")
    private String tmpPrefix;


    private static String mqPrefix;

    public final static String MICROSERVICE_REAL_EXCHANGE = ".microService.real.topicExchange";
    public final static String MICROSERVICE_REAL_ROUTE = ".microService.real.route";
    public final static String MICROSERVICE_REAL_QUEUE = ".microService.real.queue";


    public final static String MICROSERVICE_DELAY_EXCHANGE = ".microService.delay.topicExchange";
    public final static String MICROSERVICE_DELAY_ROUTE = ".microService.delay.route";
    public final static String MICROSERVICE_DELAY_QUEUE = ".microService.delay.queue";

    @Bean
    public void microServiceInit() {
        mqPrefix = tmpPrefix;
    }

    /**
     * 声明队列
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
    Binding microServiceTopicDelayExchange2() {
        logger.info("绑定延迟队列");
        return BindingBuilder.bind(microServiceDelayQueue()).to(microServiceDelayExchange()).with(getMicroServiceRealRoute());
    }

    public static String getMicroServiceDelayRoute() {
        return mqPrefix + MICROSERVICE_DELAY_ROUTE;
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

    public static String getMicroServiceDelayExchange() {
        return mqPrefix + MICROSERVICE_DELAY_EXCHANGE;
    }

    public static String getMicroServiceDelayQueue() {
        return mqPrefix + MICROSERVICE_DELAY_QUEUE;
    }
}
