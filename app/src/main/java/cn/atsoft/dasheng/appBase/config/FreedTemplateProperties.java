package cn.atsoft.dasheng.appBase.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "freed-template")
public class FreedTemplateProperties {

    private Customer customer;
    private CrmBusiness crmbusiness;
    private Contacts contacts;
    private Adress adress;
    private Contract contract;
    private ErpOrder erpOrder;
    private Repair repair;

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

    @Data
    public static class Contract {
        private String add;

        private String edit;

        private String delete;
    }

    @Data
    public static class ErpOrder {

        private String add;

        private String edit;

        private String delete;
    }
    @Data
    public static class Repair{
        private String add;

        private String edit;

        private String delete;
    }
}
