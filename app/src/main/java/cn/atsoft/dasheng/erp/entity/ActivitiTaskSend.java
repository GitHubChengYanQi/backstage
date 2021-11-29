package cn.atsoft.dasheng.erp.entity;

import cn.atsoft.dasheng.form.pojo.AuditRule;
import lombok.Data;

import java.util.List;

@Data
public class ActivitiTaskSend {
    private String url;
    private String title;
    private String text;
    private List<Long> users;
    private AuditRule auditRule;
    private Long taskId;
    private String stepsId;
}
