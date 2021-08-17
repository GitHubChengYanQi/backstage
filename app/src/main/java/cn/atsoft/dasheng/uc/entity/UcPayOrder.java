package cn.atsoft.dasheng.uc.entity;

import cn.atsoft.dasheng.uc.model.params.PayDetails;
import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author Sing
 * @since 2021-03-21
 */
@TableName(value = "uc_pay_order", autoResultMap = true)
public class UcPayOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 支付编码
     */
    @TableId(value = "pay_id", type = IdType.ASSIGN_ID)
    private Long payId;

    @TableField("title")
    private String title;

    @TableField("body")
    private String body;
    /**
     * 支付配置，余额对应币种的可用额度，在线支付额度，支付单号，支付完成通知地址等配置信息
     */
    @TableField(value = "pay_details", typeHandler = FastjsonTypeHandler.class)
    private PayDetails payDetails;

    /**
     * 商户订单号
     */
    @TableField("out_trade_no")
    private String outTradeNo;

    /**
     * 异步通知地址
     */
    @TableField("notify_url")
    private String notifyUrl;

    /**
     * 支付金额
     */
    @TableField("total_amount")
    private Integer totalAmount;

    @TableField("pay_type")
    private String payType;

    @TableField("status")
    private Integer status;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(value = "update_time", fill = FieldFill.UPDATE)
    private Date updateTime;

    @TableField("push_time")
    private Date pushTime;

    @TableField(value = "result_raw", typeHandler = FastjsonTypeHandler.class)
    private String resultRaw;

    @TableField("push_times")
    private Integer pushTimes;


    public Long getPayId() {
        return payId;
    }

    public void setPayId(Long payId) {
        this.payId = payId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getBody() {
        return body;
    }

    public PayDetails getPayDetails() {
        return payDetails;
    }

    public void setPayDetails(PayDetails payDetails) {
        this.payDetails = payDetails;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public Integer getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Integer totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getPushTime() {
        return pushTime;
    }

    public String getResultRaw() {
        return resultRaw;
    }

    public void setPushTime(Date pushTime) {
        this.pushTime = pushTime;
    }

    public void setResultRaw(String resultRaw) {
        this.resultRaw = resultRaw;
    }

    public Integer getPushTimes() {
        return pushTimes;
    }

    public void setPushTimes(Integer pushTimes) {
        this.pushTimes = pushTimes;
    }

    @Override
    public String toString() {
        return "UcPayOrder{" +
                "payId=" + payId +
                ", payDetails=" + payDetails +
                ", outTradeNo=" + outTradeNo +
                ", notifyUrl=" + notifyUrl +
                ", totalAmount=" + totalAmount +
                ", payType=" + payType +
                ", status=" + status +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", pushTime=" + pushTime +
                ", resultRaw=" + resultRaw +
                "}";
    }
}
