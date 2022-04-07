package cn.atsoft.dasheng.erp.pojo;

import lombok.Data;
import lombok.NonNull;

import java.util.List;

@Data

public class TaskComplete {
    @NonNull
    private List<Long> valueIds;
    private Long dataId;
    private Integer status;
    @NonNull
    private Long taskDetailId;
}
