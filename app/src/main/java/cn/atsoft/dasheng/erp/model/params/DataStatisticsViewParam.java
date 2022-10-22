package cn.atsoft.dasheng.erp.model.params;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class DataStatisticsViewParam {
    private String type;
    private Long createUser;
    private List<Date> dateParams;
    private List<String> dates;
    private Long customerId;
}
