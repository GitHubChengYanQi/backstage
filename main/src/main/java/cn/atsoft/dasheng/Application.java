package cn.atsoft.dasheng;


import cn.atsoft.dasheng.appBase.aop.DocumentFieldAopService;
import cn.atsoft.dasheng.core.config.MybatisDataSourceAutoConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * SpringBoot方式启动类
 */
@EnableTransactionManagement
@SpringBootApplication(exclude = {MybatisDataSourceAutoConfiguration.class})
public class Application {

    private final static Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        logger.info(Application.class.getSimpleName() + " is success!");
    }
    @Bean
    public DocumentFieldAopService documentFieldAopService() {
        return new DocumentFieldAopService();
    }

}
