package cn.atsoft.dasheng.erp.config;


import lombok.Data;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

@Service
@Data
@EnableConfigurationProperties(MobileConfig.class)
public class MobileService {

    private MobileConfig mobileConfig;

    public MobileService(MobileConfig mobileConfig) {
        this.mobileConfig = mobileConfig;
    }

    private MobileConfig getMobile() {
        return mobileConfig;
    }
}
