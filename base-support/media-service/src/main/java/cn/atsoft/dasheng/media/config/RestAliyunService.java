package cn.atsoft.dasheng.media.config;

import com.aliyun.oss.ClientBuilderConfiguration;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.comm.Protocol;
import com.aliyun.teaopenapi.models.Config;
import lombok.Data;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

@Service
@EnableConfigurationProperties(RestAliConfiguration.class)
@Data
public class RestAliyunService {

    private OSS ossClient;
    private RestAliConfiguration configuration;

    public RestAliyunService(RestAliConfiguration config) {
        this.configuration = config;
    }

    public RestAliConfiguration getConfig() {
        return configuration;
    }

    public OSS getOssClient() {
//        if (ToolUtil.isEmpty(ossClient)){
            ClientBuilderConfiguration clientBuilderConfiguration = new ClientBuilderConfiguration();
            clientBuilderConfiguration.setProtocol(Protocol.HTTPS);
            return new OSSClientBuilder().build(configuration.getOss().getEndpoint(), configuration.getAccessId(), configuration.getAccessKey(),clientBuilderConfiguration);
//        }
//        return ossClient;
    }
    public OSS getPrivateOssClient() {
//        if (ToolUtil.isEmpty(ossClient)){
            ClientBuilderConfiguration clientBuilderConfiguration = new ClientBuilderConfiguration();
            clientBuilderConfiguration.setProtocol(Protocol.HTTPS);
            return new OSSClientBuilder().build(configuration.getOss2().getEndpoint(), configuration.getAccessId(), configuration.getAccessKey(),clientBuilderConfiguration);
//        }
//        return ossClient;
    }

    public com.aliyun.dysmsapi20170525.Client getSmsClient() throws Exception {
        Config config = new Config()
                // 您的AccessKey ID
                .setAccessKeyId(configuration.getAccessId())
                // 您的AccessKey Secret
                .setAccessKeySecret(configuration.getAccessKey());
        // 访问的域名
        config.endpoint = "dysmsapi.aliyuncs.com";
        return new com.aliyun.dysmsapi20170525.Client(config);
    }
}
