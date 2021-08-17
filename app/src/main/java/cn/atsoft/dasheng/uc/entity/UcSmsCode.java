package cn.atsoft.dasheng.uc.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 *  - 
 * </p>
 *
 * @author Sing
 * @since 2021-03-16
 */
@TableName("uc_sms_code")
public class UcSmsCode implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "sms_id", type = IdType.AUTO)
    private Long smsId;

    /**
     * 手机号
     */
    @TableField("mobile")
    private String mobile;

    /**
     * 验证码
     */
    @TableField("code")
    private String code;

    /**
     * 发送时间
     */
      @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 是否有效
     */
    @TableField("used")
    private Integer used;

    /**
     * 设备信息
     */
    @TableField("device")
    private String device;

    /**
     * Ip地址
     */
    @TableField("ip")
    private String ip;


    public Long getSmsId() {
        return smsId;
    }

    public void setSmsId(Long smsId) {
        this.smsId = smsId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getUsed() {
        return used;
    }

    public void setUsed(Integer used) {
        this.used = used;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Override
    public String toString() {
        return "UcSmsCode{" +
        "smsId=" + smsId +
        ", mobile=" + mobile +
        ", code=" + code +
        ", createTime=" + createTime +
        ", used=" + used +
        ", device=" + device +
        ", ip=" + ip +
        "}";
    }
}
