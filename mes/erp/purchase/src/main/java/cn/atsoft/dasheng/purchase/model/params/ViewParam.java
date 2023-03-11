package cn.atsoft.dasheng.purchase.model.params;

import lombok.Data;

import java.util.Date;

@Data
public class ViewParam {
    private String year;
    private Date startTime;
    private Date endTime;
    private String coding;
    private String theme;
    private Long sellerId;

}
