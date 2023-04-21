package cn.atsoft.dasheng.portal.remind.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;

/**
 * <p>
 * 提醒表
 * </p>
 *
 * @author cheng
 * @since 2021-08-26
 */
@TableName("daoxin_portal_remind")
public class Remind implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 提醒id
     */
      @TableId(value = "remind_id", type = IdType.AUTO)
    private Long remindId;

    public String getTemplateType() {
        return templateType;
    }

    public void setTemplateType(String templateType) {
        this.templateType = templateType;
    }

    @TableField("template_type")
    private  String templateType;

    /**
     * 提醒类型
     */
    @TableField("type")
    private Long type;

    /**
     * 提醒人
     */
    @TableField("user_id")
    private Long userId;


    public Long getRemindId() {
        return remindId;
    }

    public void setRemindId(Long remindId) {
        this.remindId = remindId;
    }

    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
    /**
     * 部门id
     */
        @TableField(value = "deptId",fill = FieldFill.INSERT)
    private Long deptId;

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }
    @Override
    public String toString() {
        return "Remind{" +
        "remindId=" + remindId +
        ", type=" + type +
        ", userId=" + userId +
        "}";
    }
}
