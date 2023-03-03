package cn.atsoft.dasheng.form.model.results;

import cn.atsoft.dasheng.erp.model.result.QualityTaskResult;
import cn.atsoft.dasheng.form.entity.ActivitiProcess;
import cn.atsoft.dasheng.audit.model.result.ActivitiAuditResult;
import lombok.Data;

import java.util.List;

@Data
public class SetpsDetailResult {

    private Long taskId;
    private String taskName;

    private ActivitiAuditResult auditResult;

    private ActivitiProcess activitiProcess;

    private QualityTaskResult qualityTaskResult;

    private List<ActivitiAuditResult> auditResults;
}
