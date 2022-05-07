package cn.atsoft.dasheng.message.entity;

import cn.atsoft.dasheng.message.enmu.AuditEnum;
import cn.atsoft.dasheng.message.enmu.MicroServiceType;
import cn.atsoft.dasheng.message.enmu.OperationType;
import lombok.Data;

@Data
public class AuditEntity {
    /**
     * 参数对象
     */
    private Long taskId;

    private Integer auditStatus;

    private Long formStatus;

    private String form;
    private Long formId;

    private Long actionId;

    private AuditEnum auditType;


    /**
     * 延迟时间
     */
    private Integer expiration;

    /**
     * 最大推送次数
     */
    private Integer maxTimes;

    /**
     * 当前推送次数
     */
    private Integer times;
}
