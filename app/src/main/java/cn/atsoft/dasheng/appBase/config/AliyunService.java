package cn.atsoft.dasheng.appBase.config;

import com.aliyun.oss.ClientBuilderConfiguration;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.comm.Protocol;
import lombok.Data;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

@Service
@EnableConfigurationProperties(AliConfiguration.class)
@Data
public class AliyunService {

    private AliConfiguration configuration;

    public AliyunService(AliConfiguration config) {
        this.configuration = config;
    }

    public AliConfiguration getConfig() {
        return configuration;
    }

    public OSS getOssClient() {
        ClientBuilderConfiguration clientBuilderConfiguration = new ClientBuilderConfiguration();
        clientBuilderConfiguration.setProtocol(Protocol.HTTPS);
        return new OSSClientBuilder().build(configuration.getOss().getEndpoint(), configuration.getAccessId(), configuration.getAccessKey(),clientBuilderConfiguration);
    }
}
