package cn.atsoft.dasheng.purchase.model.params;

import lombok.Data;

import java.util.Date;

@Data
public class ViewParam {
    private String year;
    private String startTime;
    private String endTime;
    private String coding;
    private String theme;
    private Long sellerId;
    private Integer status;

}
