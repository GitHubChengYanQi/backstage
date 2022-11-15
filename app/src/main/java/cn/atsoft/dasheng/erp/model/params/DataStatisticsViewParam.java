package cn.atsoft.dasheng.erp.model.params;

import cn.atsoft.dasheng.erp.enums.ViewTypeEnum;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class DataStatisticsViewParam {
    private Date beginTime;
    private Date endTime;

    private ViewTypeEnum searchType;
    private String type;
    private Long createUser;
    private List<Date> dateParams;
    private List<String> dates;
    private Long customerId;
    private List<Long> spuClassIds;
    private Integer status;

    private Long instockOrderId;
    private Long userId;

    private String viewMode;
}
