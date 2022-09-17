package cn.atsoft.dasheng.form.pojo;

import lombok.Data;

import java.util.List;


@Data
public class AuditParam {
    private Long taskId;

    private Integer status;

    private String note;

    private String userIds;

    private String photoId;

    private Long pid;

    private List<Long> logIds;
    private String revokeContent;
}
