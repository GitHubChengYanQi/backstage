package cn.atsoft.dasheng.erp.pojo;

import lombok.Data;

import java.util.List;

@Data
public class PositionLoop {
    private Long key;
    private String title;
    private Long pid;

    private List<PositionLoop> loops;
}
