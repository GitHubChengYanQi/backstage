package cn.atsoft.dasheng.appBase.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author Sing
 * @since 2021-04-21
 */
@TableName("common_media")
public class Media implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 媒体ID
     */
      @TableId(value = "media_id", type = IdType.ASSIGN_ID)
    private Long mediaId;

    /**
     * 文件路径
     */
    @TableField("path")
    private String path;

    /**
     * OSS储存区
     */
    @TableField("endpoint")
    private String endpoint;

    /**
     * OSS储存块
     */
    @TableField("bucket")
    private String bucket;

    public String getFiledName() {
        return filedName;
    }

    public void setFiledName(String filedName) {
        this.filedName = filedName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    /**
     *
     */
    @TableField("filed_name")
    private String filedName;

    /**
     *
     */
    @TableField("type")
    private String type;

    /**
     * 上传状态
     */
    @TableField("status")
    private Integer status;

    /**
     * 用户ID
     */
    @TableField("user_id")
    private Long userId;

      @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

      @TableField(value = "create_user", fill = FieldFill.INSERT)
    private Long createUser;

      @TableField(value = "update_user", fill = FieldFill.UPDATE)
    private Long updateUser;

      @TableField(value = "update_time", fill = FieldFill.UPDATE)
    private Date updateTime;


    public Long getMediaId() {
        return mediaId;
    }

    public void setMediaId(Long mediaId) {
        this.mediaId = mediaId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
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

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "ImMedia{" +
        "mediaId=" + mediaId +
        ", path=" + path +
        ", endpoint=" + endpoint +
        ", bucket=" + bucket +
        ", status=" + status +
        ", userId=" + userId +
        ", createTime=" + createTime +
        ", createUser=" + createUser +
        ", updateUser=" + updateUser +
        ", updateTime=" + updateTime +
        "}";
    }
}
