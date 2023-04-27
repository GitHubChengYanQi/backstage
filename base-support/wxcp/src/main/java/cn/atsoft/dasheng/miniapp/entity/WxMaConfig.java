package cn.atsoft.dasheng.miniapp.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 微信小程序配置表（对应租户）
 * </p>
 *
 * @author Captain_Jazz
 * @since 2023-04-25
 */
@TableName("wx_ma_config")
public class WxMaConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
      @TableId(value = "wx_ma_config_id", type = IdType.ID_WORKER)
    private Long wxMaConfigId;

    /**
     * 租户id
     */
      @TableField(value = "tenant_id", fill = FieldFill.INSERT)
    private Long tenantId;

    @TableField("appid")
    private String appid;

    @TableField("secret")
    private String secret;

    @TableField("aesKey")
    private String aesKey;

    @TableField("token")
    private String token;

    @TableField("msg_data_format")
    private String msgDataFormat;

    @TableField("display")
    private Integer display;

      @TableField(value = "create_user", fill = FieldFill.INSERT)
    private Long createUser;

      @TableField(value = "update_user", fill = FieldFill.UPDATE)
    private Long updateUser;

      @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

      @TableField(value = "update_time", fill = FieldFill.UPDATE)
    private Date updateTime;

    /**
     * 部门编号
     */
      @TableField(value = "deptId", fill = FieldFill.INSERT)
    private Long deptId;


    public Long getWxMaConfigId() {
        return wxMaConfigId;
    }

    public void setWxMaConfigId(Long wxMaConfigId) {
        this.wxMaConfigId = wxMaConfigId;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getAesKey() {
        return aesKey;
    }

    public void setAesKey(String aesKey) {
        this.aesKey = aesKey;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMsgDataFormat() {
        return msgDataFormat;
    }

    public void setMsgDataFormat(String msgDataFormat) {
        this.msgDataFormat = msgDataFormat;
    }

    public Integer getDisplay() {
        return display;
    }

    public void setDisplay(Integer display) {
        this.display = display;
    }

    public Long getCreateUser() {
        return createUser;
    }

    public void setCreateUser(Long createUser) {
        this.createUser = createUser;
    }

    public Long getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(Long updateUser) {
        this.updateUser = updateUser;
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

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    @Override
    public String toString() {
        return "WxMaConfig{" +
        "wxMaConfigId=" + wxMaConfigId +
        ", tenantId=" + tenantId +
        ", appid=" + appid +
        ", secret=" + secret +
        ", aesKey=" + aesKey +
        ", token=" + token +
        ", msgDataFormat=" + msgDataFormat +
        ", display=" + display +
        ", createUser=" + createUser +
        ", updateUser=" + updateUser +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        ", deptId=" + deptId +
        "}";
    }
}
