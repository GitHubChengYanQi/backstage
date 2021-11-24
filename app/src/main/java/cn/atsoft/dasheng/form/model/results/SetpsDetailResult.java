package cn.atsoft.dasheng.form.model.results;

import cn.atsoft.dasheng.erp.model.result.QualityTaskResult;
import cn.atsoft.dasheng.form.model.result.ActivitiAuditResult;
import lombok.Data;

@Data
public class SetpsDetailResult {
    private ActivitiAuditResult auditResult;
    private QualityTaskResult qualityTaskResult;
}
