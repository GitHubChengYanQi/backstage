package cn.atsoft.dasheng.form.pojo;

import lombok.Data;

import java.util.List;

@Data
public class AuditParam {
    private Long taskId;

    private Integer status;

    private String note;

    private List<Long> userIds;
}
