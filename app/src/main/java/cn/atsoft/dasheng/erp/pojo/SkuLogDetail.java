package cn.atsoft.dasheng.erp.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class SkuLogDetail {

    private String type; //任务类型

    private Integer operationNum; //操作的数量

    private Integer balance; //结余

    private String theme; //主题

    private String brandName;

    private String userName;

    private Date time;

    private String position;
}
