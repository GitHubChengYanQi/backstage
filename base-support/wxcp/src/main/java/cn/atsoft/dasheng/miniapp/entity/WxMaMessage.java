package cn.atsoft.dasheng.miniapp.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 小程序订阅消息
 * </p>
 *
 * @author Captain_Jazz
 * @since 2023-05-11
 */
@TableName("wx_ma_message")
public class WxMaMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("ma_message_id")
    private Long maMessageId;

    /**
     * 消息类型
     */
    @TableField("event_type")
    private String eventType;

    /**
     * 事件类型
     */
    @TableField("event")
    private String event;

    @TableField("to_user_name")
    private String toUserName;

    /**
     * 发送方账号(openId)
     */
    @TableField("from_user_name")
    private String fromUserName;

    /**
     * 消息id
     */
    @TableField("msg_id")
    private Long msgId;

    /**
     * 模板id
     */
    @TableField("template_id")
    private String templateId;

      @TableField(value = "tenant_id", fill = FieldFill.INSERT)
    private Long tenantId;

    @TableField("node")
    private String node;

      @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 订阅结果
     */
    @TableField("subscribe_status_string")
    private String subscribeStatusString;


    public Long getMaMessageId() {
        return maMessageId;
    }

    public void setMaMessageId(Long maMessageId) {
        this.maMessageId = maMessageId;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getToUserName() {
        return toUserName;
    }

    public void setToUserName(String toUserName) {
        this.toUserName = toUserName;
    }

    public String getFromUserName() {
        return fromUserName;
    }

    public void setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
    }

    public Long getMsgId() {
        return msgId;
    }

    public void setMsgId(Long msgId) {
        this.msgId = msgId;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getSubscribeStatusString() {
        return subscribeStatusString;
    }

    public void setSubscribeStatusString(String subscribeStatusString) {
        this.subscribeStatusString = subscribeStatusString;
    }

    @Override
    public String toString() {
        return "WxMaMessage{" +
        "maMessageId=" + maMessageId +
        ", eventType=" + eventType +
        ", event=" + event +
        ", toUserName=" + toUserName +
        ", fromUserName=" + fromUserName +
        ", msgId=" + msgId +
        ", templateId=" + templateId +
        ", tenantId=" + tenantId +
        ", node=" + node +
        ", createTime=" + createTime +
        ", subscribeStatusString=" + subscribeStatusString +
        "}";
    }
}
