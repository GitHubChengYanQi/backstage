package cn.atsoft.dasheng.uc.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author Sing
 * @since 2021-03-16
 */
@TableName("uc_member")
public class UcMember implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "member_id")
    private Long memberId;

    @TableField("phone")
    private String phone;

    @TableField("status")
    private Integer status;

    @TableField("display")
    private Integer display;

      @TableField(value = "create_user", fill = FieldFill.INSERT)
    private Long createUser;

      @TableField(value = "update_user", fill = FieldFill.UPDATE)
    private Long updateUser;

      @TableField(value = "update_time", fill = FieldFill.UPDATE)
    private Date updateTime;

      @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;


    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "UcMember{" +
        "memberId=" + memberId +
        ", phone=" + phone +
        ", status=" + status +
        ", display=" + display +
        ", createUser=" + createUser +
        ", updateUser=" + updateUser +
        ", updateTime=" + updateTime +
        ", createTime=" + createTime +
        "}";
    }
}
