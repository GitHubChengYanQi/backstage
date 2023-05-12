package cn.atsoft.dasheng.miniapp.model.params;

import lombok.Data;
import cn.atsoft.dasheng.model.validator.BaseValidatingParam;
import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;

/**
 * <p>
 * 小程序订阅消息
 * </p>
 *
 * @author Captain_Jazz
 * @since 2023-05-11
 */
@Data
@ApiModel
public class WxMaMessageParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty("")
    private Long maMessageId;

    /**
     * 消息类型
     */
    @ApiModelProperty("消息类型")
    private String eventType;

    /**
     * 事件类型
     */
    @ApiModelProperty("事件类型")
    private String event;

    @ApiModelProperty("")
    private String toUserName;

    /**
     * 发送方账号(openId)
     */
    @ApiModelProperty("发送方账号(openId)")
    private String fromUserName;

    /**
     * 消息id
     */
    @ApiModelProperty("消息id")
    private Long msgId;

    /**
     * 模板id
     */
    @ApiModelProperty("模板id")
    private String templateId;

    @ApiModelProperty("")
    private Long tenantId;

    @ApiModelProperty("")
    private String node;

    @ApiModelProperty(hidden = true)
    private Date createTime;

    /**
     * 订阅结果
     */
    @ApiModelProperty("订阅结果")
    private String subscribeStatusString;

    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;

    @Override
    public String checkParam() {
        return null;
    }

}
