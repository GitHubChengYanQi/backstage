package cn.atsoft.dasheng.message.service;

import cn.atsoft.dasheng.form.entity.ActivitiProcess;
import cn.atsoft.dasheng.form.service.ActivitiProcessLogService;
import cn.atsoft.dasheng.message.entity.AuditEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuditMessageService {
    @Autowired
    private ActivitiProcessLogService activitiProcessLogService;

    public void auditDo(AuditEntity auditEntity) {
        switch (auditEntity.getMessageType()){
            case AUDIT:
                switch (auditEntity.getAuditType()){
                    case AUDIT:
                        activitiProcessLogService.autoAudit(auditEntity.getTaskId(),1,auditEntity.getLoginUserId());
                        break;
                    case AUTO_AUDIT:
                        activitiProcessLogService.autoAudit(auditEntity.getTaskId(),1,auditEntity.getLoginUserId());
                        break;
                    case REFUSE:
                        activitiProcessLogService.autoAudit(auditEntity.getTaskId(),2,auditEntity.getLoginUserId());
                        break;
                    case CHECK_ACTION:
                        activitiProcessLogService.checkAction(auditEntity.getFormId(),auditEntity.getForm(), auditEntity.getActionId(), auditEntity.getLoginUserId());
                        break;
                    case AUDIT_START:
                        activitiProcessLogService.autoAudit(auditEntity.getTaskId(),null,auditEntity.getLoginUserId());
                        break;
                }
                break;
            case CREATE_TASK:
                /**
                 * 执行自动审批
                 */
                this.createTask(auditEntity.getActivitiProcess(), auditEntity.getTaskId(), auditEntity.getLoginUserId());
                break;

            default:
                break;
        }
    }

    public void createTask(ActivitiProcess activitiProcess,Long taskId,Long loginUserId){
        //添加log
        activitiProcessLogService.addLog(activitiProcess.getProcessId(), taskId);

        /**
         * TODO 是否需要自动审批使用消息队列
         */
        activitiProcessLogService.autoAudit(taskId, 1,loginUserId);
    }
}
