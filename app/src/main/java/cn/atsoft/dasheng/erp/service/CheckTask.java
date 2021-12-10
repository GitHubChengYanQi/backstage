package cn.atsoft.dasheng.erp.service;

import cn.atsoft.dasheng.erp.entity.QualityTask;
import cn.atsoft.dasheng.form.pojo.RuleType;

public interface CheckTask {
    Boolean checkTask(Long qualityTaskId, RuleType ruleType);
}
