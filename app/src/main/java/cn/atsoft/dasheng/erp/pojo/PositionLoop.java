package cn.atsoft.dasheng.erp.pojo;

import lombok.Data;

import java.util.List;

@Data
public class PositionLoop {
    private Long key;
    private String title;
    private Long pid;
    private Long storeHouseId;
    private Long skuId;
    private Long num;

    private List<PositionLoop> loops;
}
