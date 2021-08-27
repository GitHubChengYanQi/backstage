package cn.atsoft.dasheng.portal.remind.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
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

    @Override
    public String toString() {
        return "Remind{" +
        "remindId=" + remindId +
        ", type=" + type +
        ", userId=" + userId +
        "}";
    }
}
