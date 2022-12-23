package cn.atsoft.dasheng.erp.model.params;

import cn.atsoft.dasheng.erp.enums.ViewTypeEnum;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class DataStatisticsViewParam {
    private Date beginTime;
    private Date endTime;
    private Integer frame;//图表时间间隔

    private ViewTypeEnum searchType;
    private String type;
    private Long createUser;
    private Long materialId;
    private Long storehouseId;
    private List<Date> dateParams;
    private List<String> dates;
    private Long customerId;
    private List<Long> spuClassIds;
    private Long spuClassId;
    private Integer status;

    private Long instockOrderId;
    private Long userId;
    private String cycle;
    private String viewMode;
}
