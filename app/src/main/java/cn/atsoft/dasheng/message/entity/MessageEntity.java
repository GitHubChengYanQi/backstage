package cn.atsoft.dasheng.message.entity;

import cn.atsoft.dasheng.app.entity.BusinessTrack;
import cn.atsoft.dasheng.app.entity.Message;
import cn.atsoft.dasheng.crm.model.params.TrackMessageParam;
import cn.atsoft.dasheng.message.enmu.MessageType;
import cn.binarywang.wx.miniapp.bean.WxMaSubscribeMessage;
import lombok.Data;
import me.chanjar.weixin.cp.bean.message.WxCpMessage;
import me.chanjar.weixin.mp.bean.subscribe.WxMpSubscribeMessage;
import org.bouncycastle.cms.PasswordRecipientId;

@Data
public class MessageEntity {

    /**
     * 消息类型
     */
    private MessageType type;

    /**
     * 企业微信消息内容
     */
    private WxCpMessage cpData;

    /**
     * 微信公众号订阅消息内容
     * 微信小程序订阅消息内容
     */
    private WxMpSubscribeMessage mpData;

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
    /**
     *
     */


}
