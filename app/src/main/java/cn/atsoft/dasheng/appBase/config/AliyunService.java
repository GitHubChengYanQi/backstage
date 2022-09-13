package cn.atsoft.dasheng.appBase.config;

import cn.atsoft.dasheng.core.util.ToolUtil;
import com.aliyun.oss.ClientBuilderConfiguration;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.comm.Protocol;
import com.aliyun.teaopenapi.models.Config;
import lombok.Data;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

@Service
@EnableConfigurationProperties(AliConfiguration.class)
@Data
public class AliyunService {

    private OSS ossClient;
    private AliConfiguration configuration;

    public AliyunService(AliConfiguration config) {
        this.configuration = config;
    }

    public AliConfiguration getConfig() {
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
