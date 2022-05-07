package cn.atsoft.dasheng.message.service;

import cn.atsoft.dasheng.form.service.ActivitiProcessLogService;
import cn.atsoft.dasheng.message.entity.AuditEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuditMessageService {
    @Autowired
    private ActivitiProcessLogService activitiProcessLogService;

    public void auditDo(AuditEntity auditEntity) {
        switch (auditEntity.getAuditType()){
            case audit:
                activitiProcessLogService.audit(auditEntity.getTaskId(),1);
                break;
            case autoAudit:
                activitiProcessLogService.autoAudit(auditEntity.getTaskId(),1);
                break;
            case refuse:
                activitiProcessLogService.audit(auditEntity.getTaskId(),2);
                break;
            case checkAction:
                activitiProcessLogService.checkAction(auditEntity.getFormId(),auditEntity.getForm(), auditEntity.getActionId());
                break;
        }
    }
}
