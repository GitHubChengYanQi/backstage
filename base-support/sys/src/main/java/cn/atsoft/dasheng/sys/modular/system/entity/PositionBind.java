package cn.atsoft.dasheng.sys.modular.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 租户用户位置绑定表
 * </p>
 *
 * @author Captain_Jazz
 * @since 2023-05-09
 */
@TableName("sys_position_bind")
public class PositionBind implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "position_bind_id", type = IdType.ID_WORKER)
    private Long positionBindId;

    @TableField("position_id")
    private Long positionId;

    @TableField("user_id")
    private Long userId;

      @TableField(value = "tenant_id", fill = FieldFill.INSERT)
    private Long tenantId;


    public Long getPositionBindId() {
        return positionBindId;
    }

    public void setPositionBindId(Long positionBindId) {
        this.positionBindId = positionBindId;
    }

    public Long getPositionId() {
        return positionId;
    }

    public void setPositionId(Long positionId) {
        this.positionId = positionId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    @Override
    public String toString() {
        return "PositionBind{" +
        "positionBindId=" + positionBindId +
        ", positionId=" + positionId +
        ", userId=" + userId +
        ", tenantId=" + tenantId +
        "}";
    }
}
