package cn.atsoft.dasheng.app.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;

/**
 * <p>
 * 跟进内容
 * </p>
 *
 * @author cheng
 * @since 2021-09-17
 */
@TableName("daoxin_crm_business_track")
public class BusinessTrack implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 跟进内容id
     */
    @TableId(value = "track_id", type = IdType.ID_WORKER)
    private Long trackId;

    /**
     * 跟踪内容
     */
    @TableField("message")
    private String message;

    /**
     * 创建者
     */
    @TableField(value = "create_user", fill = FieldFill.INSERT)
    private Long createUser;

    @TableField("state")
    private Integer state;
    /**
     * 修改者
     */
    @TableField(value = "update_user", fill = FieldFill.UPDATE)
    private Long updateUser;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 修改时间
     */
    @TableField(value = "update_time", fill = FieldFill.UPDATE)
    private Date updateTime;

    /**
     * 消息提醒内容
     */
    @TableField("tixing")
    private String tixing;

    /**
     * 跟踪类型
     */
    @TableField("type")
    private String type;

    /**
     * 状态
     */
    @TableField("display")
    private Integer display;

    /**
     * 提醒时间
     */
    @TableField("time")
    private Date time;

    /**
     * 提醒内容
     */
    @TableField("note")
    private String note;

    /**
     * 图片
     */
    @TableField("image")
    private String image;

    /**
     * 经度
     */
    @TableField("longitude")
    private Double longitude;

    /**
     * 纬度
     */
    @TableField("latitude")
    private Double latitude;

    /**
     * 负责人
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 部门id
     */
    @TableField(value = "deptId", fill = FieldFill.INSERT)
    private Long deptId;


    @TableField(value = "tenant_id" , fill = FieldFill.INSERT)
    private Long tenantId;

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    /**
     * 分类
     */
    @TableField("classify")
    private Integer classify;

    /**
     * 分类id
     */
    @TableField("classify_id")
    private Long classifyId;

    /**
     * 跟进总表id
     */
    @TableField("track_message_id")
    private Long trackMessageId;


    public Long getTrackId() {
        return trackId;
    }

    public void setTrackId(Long trackId) {
        this.trackId = trackId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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

    public String getTixing() {
        return tixing;
    }

    public void setTixing(String tixing) {
        this.tixing = tixing;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getDisplay() {
        return display;
    }

    public void setDisplay(Integer display) {
        this.display = display;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public Integer getClassify() {
        return classify;
    }

    public void setClassify(Integer classify) {
        this.classify = classify;
    }

    public Long getClassifyId() {
        return classifyId;
    }

    public void setClassifyId(Long classifyId) {
        this.classifyId = classifyId;
    }

    public Long getTrackMessageId() {
        return trackMessageId;
    }

    public void setTrackMessageId(Long trackMessageId) {
        this.trackMessageId = trackMessageId;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "BusinessTrack{" +
                "trackId=" + trackId +
                ", message=" + message +
                ", createUser=" + createUser +
                ", updateUser=" + updateUser +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", tixing=" + tixing +
                ", type=" + type +
                ", display=" + display +
                ", time=" + time +
                ", note=" + note +
                ", image=" + image +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", userId=" + userId +
                ", deptId=" + deptId +
                ", classify=" + classify +
                ", classifyId=" + classifyId +
                ", trackMessageId=" + trackMessageId +
                "}";
    }
}
