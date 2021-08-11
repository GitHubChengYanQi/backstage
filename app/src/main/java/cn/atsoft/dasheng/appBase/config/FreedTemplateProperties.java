package cn.atsoft.dasheng.appBase.config;

import cn.atsoft.dasheng.app.entity.Adress;
import cn.atsoft.dasheng.app.entity.Contacts;
import cn.atsoft.dasheng.app.entity.CrmBusiness;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "freed-template")
public class FreedTemplateProperties {

    private Customer customer;
    private CrmBusiness crmbusiness;
    private Contacts contacts;
    private Adress adress;

    @Data
    public static class Customer {

        private String add;

        private String edit;

        private String delete;
    }

    @Data
    public static class CrmBusiness {
        private String add;

        private String edit;

        private String delete;
    }

    @Data
    public static class Contacts {
        private String add;

        private String edit;

        private String delete;
    }

    @Data
    public static class Adress {

        private String add;

        private String edit;

        private String delete;
    }
}
