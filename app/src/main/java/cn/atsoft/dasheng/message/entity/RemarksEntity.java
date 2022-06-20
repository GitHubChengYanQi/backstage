package cn.atsoft.dasheng.message.entity;

import cn.atsoft.dasheng.form.entity.ActivitiProcess;
import cn.atsoft.dasheng.form.model.params.RemarksParam;
import cn.atsoft.dasheng.message.enmu.AuditEnum;
import cn.atsoft.dasheng.message.enmu.AuditMessageType;
import cn.atsoft.dasheng.message.enmu.MicroServiceType;
import cn.atsoft.dasheng.message.enmu.OperationType;
import lombok.Data;

import javax.validation.constraints.NotNull;
@Data
public class RemarksEntity {
    /**
     * 参数对象
     */
    private RemarksParam remarksParam;
//    /**
//     * 类型
//     */
//    private MicroServiceType type;

    /**
     * 操作类型
     */

    private OperationType operationType;

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
    private Integer times =0;

}
