package cn.atsoft.dasheng.sys.modular.rest.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;

/**
 * <p>
 * 用户职位关联表
 * </p>
 *
 * @author stylefeng
 * @since 2019-06-28
 */
@TableName("sys_user_pos")
public class RestUserPos implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "user_pos_id", type = IdType.ID_WORKER)
    private Long userPosId;

    /**
     * 用户id
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 职位id
     */
    @TableField("pos_id")
    private Long posId;

    @TableField(value = "tenant_id" , fill = FieldFill.INSERT)
    private Long tenantId;

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Long getUserPosId() {
        return userPosId;
    }

    public void setUserPosId(Long userPosId) {
        this.userPosId = userPosId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getPosId() {
        return posId;
    }

    public void setPosId(Long posId) {
        this.posId = posId;
    }

    @Override
    public String toString() {
        return "UserPos{" +
                "userPosId=" + userPosId +
                ", userId=" + userId +
                ", posId=" + posId +
                "}";
    }
}
