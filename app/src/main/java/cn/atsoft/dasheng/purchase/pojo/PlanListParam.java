package cn.atsoft.dasheng.purchase.pojo;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class PlanListParam {
    private String type;
    private String beginTime;
    private String endTime;
    private String coding;
    private Long stockNumber;
    private List<String> keys;
    private Long skuId;

}
