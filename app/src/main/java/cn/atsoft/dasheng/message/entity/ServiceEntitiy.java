package cn.atsoft.dasheng.message.entity;

import cn.atsoft.dasheng.app.entity.Message;
import cn.atsoft.dasheng.message.enmu.ServiceType;
import lombok.Data;

@Data
public class ServiceEntitiy {

    private ServiceType type;

    private Object object;

    private Long id;

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
    /**
     * 代办消息
     */
    private Message message;

}
