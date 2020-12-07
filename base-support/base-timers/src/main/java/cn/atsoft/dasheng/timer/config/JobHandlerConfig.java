package cn.atsoft.dasheng.timer.config;

import cn.atsoft.dasheng.timer.jobhandler.CommandJobHandler;
import cn.atsoft.dasheng.timer.jobhandler.DemoJobHandler;
import cn.atsoft.dasheng.timer.jobhandler.HttpJobHandler;
import cn.atsoft.dasheng.timer.jobhandler.ShardingJobHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * xxl-job config
 *
 * @author xuxueli 2017-04-28
 */
@Configuration
public class JobHandlerConfig {

    @Bean
    public CommandJobHandler commandJobHandler() {
        return new CommandJobHandler();
    }

    @Bean
    public DemoJobHandler demoJobHandler() {
        return new DemoJobHandler();
    }

    @Bean
    public HttpJobHandler httpJobHandler() {
        return new HttpJobHandler();
    }

    @Bean
    public ShardingJobHandler shardingJobHandler() {
        return new ShardingJobHandler();
    }

}