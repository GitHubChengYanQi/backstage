package cn.atsoft.dasheng.erp.service.impl;

import cn.atsoft.dasheng.action.Enum.QualityActionEnum;
import cn.atsoft.dasheng.erp.entity.QualityTask;
import cn.atsoft.dasheng.erp.service.CheckTask;
import cn.atsoft.dasheng.erp.service.QualityTaskService;
import cn.atsoft.dasheng.form.pojo.RuleType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CheckQualityTask implements CheckTask {
    @Autowired
    private QualityTaskService qualityTaskService;

    public Boolean checkTask(Long qualityTaskId, RuleType ruleType) {
        QualityTask qualityTask = qualityTaskService.getById(qualityTaskId);
        switch (ruleType) {

            case quality_dispatch:
                if (qualityTask.getState().equals(1)||qualityTask.getState().equals(-1)) {
                    return true;
                } else {
                    return false;
                }
            case quality_perform:
                if (qualityTask.getState().equals(2)) {
                    return true;
                } else {
                    return false;
                }
            case quality_complete:
                Long status = QualityActionEnum.done.getStatus();
                Long state = Long.valueOf(qualityTask.getState());
                if (status.equals(state)) {
                    return true;
                } else {
                    return false;
                }
            default:
                return false;
        }

    }
}
