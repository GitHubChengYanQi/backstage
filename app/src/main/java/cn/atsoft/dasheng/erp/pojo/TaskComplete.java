package cn.atsoft.dasheng.erp.pojo;

import lombok.Data;

import java.util.List;

@Data

public class TaskComplete {
    private List<Long> valueIds;
    private Long dataId;
    private Integer status;
    private Long taskDetailId;
}
