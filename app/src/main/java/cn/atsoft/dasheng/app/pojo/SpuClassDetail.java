package cn.atsoft.dasheng.app.pojo;

import lombok.Data;

import java.util.List;

@Data
public class SpuClassDetail {

    private String spuClass;

    private Long num;

    private Long count;

    private Long normalNum;

    private Long normalCount;

    private Long errorNum;

    private Long errorCount;

    private List<Long> errorInkindIds;

}
