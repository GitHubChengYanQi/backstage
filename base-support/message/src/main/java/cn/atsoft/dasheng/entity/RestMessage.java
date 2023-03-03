package cn.atsoft.dasheng.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 消息提醒
 * </p>
 *
 * @author
 * @since 2021-08-03
 */
@TableName("daoxin_message")
public class RestMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("view")
    private Integer view;

    @TableField("promoter")
    private Long promoter;

    /**
     * 消息提醒id
     */
    @TableId(value = "message_id", type = IdType.ID_WORKER)
    private Long messageId;



    /**
     * 路径
     */
    @TableField("url")
    private String url;

    /**
     * 提醒时间
     */
    @TableField("time")
    private Date time;

    /**
     * 提醒状态
     */
    @TableField("state")
    private Integer state;

    /**
     * 提醒标题
     */
    @TableField("title")
    private String title;

    /**
     * 提醒内容
     */
    @TableField("content")
    private String content;

    /**
     * 提醒类型
     */
    @TableField("type")
    private Integer type;

    /**
     * 关联人id
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 来源
     */
    @TableField("source")
    private String source;

    /**
     * 来源id
     */
    @TableField("source_id")
    private Long sourceId;

    /**
     * 创建者
     */
    @TableField(value = "create_user", fill = FieldFill.INSERT)
    private Long createUser;

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
     * 状态
     */
    @TableField("display")
    private Integer display;

    /**
     * 排序
     */
    @TableField("sort")
    private Long sort;

    @TableField(value = "deptId", fill = FieldFill.INSERT_UPDATE)
    private Long deptId;

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Long getSourceId() {
        return sourceId;
    }

    public void setSourceId(Long sourceId) {
        this.sourceId = sourceId;
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

    public Integer getDisplay() {
        return display;
    }

    public void setDisplay(Integer display) {
        this.display = display;
    }

    public Long getSort() {
        return sort;
    }

    public void setSort(Long sort) {
        this.sort = sort;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public Integer getView() {
        return view;
    }

    public void setView(Integer view) {
        this.view = view;
    }

    public Long getPromoter() {
        return promoter;
    }

    public void setPromoter(Long promoter) {
        this.promoter = promoter;
    }

    @Override
    public String toString() {
        return "Message{" +
                "messageId=" + messageId +
                ", time=" + time +
                ", state=" + state +
                ", title=" + title +
                ", content=" + content +
                ", type=" + type +
                ", createUser=" + createUser +
                ", updateUser=" + updateUser +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", display=" + display +
                ", sort=" + sort +
                "}";
    }
}
