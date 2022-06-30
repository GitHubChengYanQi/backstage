package cn.atsoft.dasheng.form.pojo;

import lombok.Data;


@Data
public class AuditParam {
    private Long taskId;

    private Integer status;

    private String note;

    private String userIds;

    private String photoId;

    private Long pid;

    private Long logId;
}
