package cn.atsoft.dasheng.api.uc.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author 
 * @since 2021-08-25
 */
@TableName("uc_open_user_info")
public class OpenUserInfo implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "primary_key", type = IdType.ID_WORKER)

    private String primaryKey;

    /**
     * 会员编号
     */
    @TableField("member_id")
    private Long memberId;

    /**
     * 用户第三方系统的唯一id
     */
    @TableField("uuid")
    private String uuid;
    /**
     * 用户第三方系统的唯一id
     */
    @TableField("appid")
    private String appid;

    /**
     * 用户来源
     */
    @TableField("source")
    private String source;

    /**
     * 用户名
     */
    @TableField("username")
    private String username;

    /**
     * 用户昵称
     */
    @TableField("nickname")
    private String nickname;

    /**
     * 用户头像
     */
    @TableField("avatar")
    private String avatar;

    /**
     * 用户网址
     */
    @TableField("blog")
    private String blog;

    /**
     * 所在公司
     */
    @TableField("company")
    private String company;

    /**
     * 位置
     */
    @TableField("location")
    private String location;

    /**
     * 用户邮箱
     */
    @TableField("email")
    private String email;

    /**
     * 用户备注（各平台中的用户个人介绍）
     */
    @TableField("remark")
    private String remark;

    /**
     * 性别|1男|0女|-1未知
     */
    @TableField("gender")
    private Integer gender;

    /**
     * 第三方平台返回的原始用户信息
     */
    @TableField("rawUserInfo")
    private String rawUserInfo;

    /**
     * 手机号码，关联用户
     */
    @TableField("mobile")
    private String mobile;

    /**
     * 创建时间
     */
      @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;


    @TableField(value ="tenant_id", fill = FieldFill.INSERT)
    private Long tenantId;


    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getBlog() {
        return blog;
    }

    public void setBlog(String blog) {
        this.blog = blog;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getRawUserInfo() {
        return rawUserInfo;
    }

    public void setRawUserInfo(String rawUserInfo) {
        this.rawUserInfo = rawUserInfo;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    @Override
    public String toString() {
        return "OpenUserInfo{" +
        "primaryKey=" + primaryKey +
        ", memberId=" + memberId +
        ", uuid=" + uuid +
        ", source=" + source +
        ", username=" + username +
        ", nickname=" + nickname +
        ", avatar=" + avatar +
        ", blog=" + blog +
        ", company=" + company +
        ", location=" + location +
        ", email=" + email +
        ", remark=" + remark +
        ", gender=" + gender +
        ", rawUserInfo=" + rawUserInfo +
        ", mobile=" + mobile +
        ", createTime=" + createTime +
        "}";
    }
}
