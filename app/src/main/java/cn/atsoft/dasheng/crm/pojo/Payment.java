package cn.atsoft.dasheng.crm.pojo;

import cn.hutool.core.date.DateTime;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class Payment {

    private List<PaymentDetail> details;

    @Data
    public class PaymentDetail {
        private Date time;     //时间
        private Integer money;    //金额
        private Integer percent;  //百分比
        private String name;      //名称
    }

}
