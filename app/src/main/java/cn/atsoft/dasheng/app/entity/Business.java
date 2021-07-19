package cn.atsoft.dasheng.app.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 商机表
 * </p>
 *
 * @author cheng
 * @since 2021-07-19
 */
@TableName("daoxin_business")
public class Business implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 商机id
     */
      @TableId(value = "business_id", type = IdType.ID_WORKER)
    private Long businessId;

    /**
     * 客户id
     */
    @TableField("cliten_id")
    private Long clitenId;

    /**
     * 机会来源
     */
    @TableField("source")
    private String source;

    /**
     * 立项日期
     */
    @TableField("time")
    private Date time;

    /**
     * 商机状态
     */
    @TableField("state")
    private Integer state;

    /**
     * 商机阶段
     */
    @TableField("stage")
    private String stage;

    /**
     * 负责人
     */
    @TableField("person")
    private String person;

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
     * 创建用户
     */
      @TableField(value = "create_user", fill = FieldFill.INSERT)
    private Long createUser;

    /**
     * 修改用户
     */
      @TableField(value = "update_user", fill = FieldFill.UPDATE)
    private Long updateUser;

    /**
     * 状态
     */
    @TableField("display")
    private Integer display;


    public Long getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Long businessId) {
        this.businessId = businessId;
    }

    public Long getClitenId() {
        return clitenId;
    }

    public void setClitenId(Long clitenId) {
        this.clitenId = clitenId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
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

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
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

    public Integer getDisplay() {
        return display;
    }

    public void setDisplay(Integer display) {
        this.display = display;
    }

    @Override
    public String toString() {
        return "Business{" +
        "businessId=" + businessId +
        ", clitenId=" + clitenId +
        ", source=" + source +
        ", time=" + time +
        ", state=" + state +
        ", stage=" + stage +
        ", person=" + person +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        ", createUser=" + createUser +
        ", updateUser=" + updateUser +
        ", display=" + display +
        "}";
    }
}
