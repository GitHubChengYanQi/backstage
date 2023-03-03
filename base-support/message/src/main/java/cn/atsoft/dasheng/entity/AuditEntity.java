package cn.atsoft.dasheng.entity;

import cn.atsoft.dasheng.enmu.AuditEnum;
import cn.atsoft.dasheng.enmu.AuditMessageType;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AuditEntity {


    /**
     * 审批任务id
     */
    private Long taskId;

    private Long loginUserId;

    /**
     * 审批动作
     */
    private Integer auditStatus;

    private String token;
    /**
     * 单据状态
     */
    private Long formStatus;

    /**
     * 来源
     */
    private String form;

    /**
     * 来源id
     */
    private Long formId;

    /**
     * 单据动作id
     */
    private Long actionId;

    /**
     * 审批动作枚举）
     */
    private AuditEnum auditType;
    /**
     * 消息类型（创建审批任务 || 执行审批动作）
     */
    @NotNull
    private AuditMessageType messageType;
//    /**
//     * 审批流程
//     */
//    ActivitiProcess activitiProcess;

    /**
     * 延迟时间
     */
    private Integer expiration;

    /**
     * 最大推送次数
     */
    private Integer maxTimes = 2;

    /**
     * 当前推送次数
     */
    private Integer times = 1;
}
