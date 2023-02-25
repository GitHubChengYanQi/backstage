package cn.atsoft.dasheng.entity;

import cn.atsoft.dasheng.enmu.MicroServiceType;
import cn.atsoft.dasheng.enmu.OperationType;
import lombok.Data;

@Data
public class MicroServiceEntity {
    /**
     * 参数对象
     */
    private Object object;
    /**
     * 类型
     */
    private MicroServiceType type;

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
    private Integer maxTimes;

    /**
     * 当前推送次数
     */
    private Integer times;


}
