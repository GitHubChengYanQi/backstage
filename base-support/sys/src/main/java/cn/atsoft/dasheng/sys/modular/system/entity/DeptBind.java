package cn.atsoft.dasheng.sys.modular.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 各租户内 部门绑定表
 * </p>
 *
 * @author Captain_Jazz
 * @since 2023-05-04
 */
@TableName("sys_dept_bind")
public class DeptBind implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "dept_bind_id", type = IdType.ID_WORKER)
    private Long deptBindId;

    @TableField("deptId")
    private Long deptId;

    @TableField("user_id")
    private Long userId;
    @TableField("main_dept")
    private Integer mainDept;

    @TableField("main_dept")
    private Integer mainDept;

      @TableField(value = "tenant_id", fill = FieldFill.INSERT)
    private Long tenantId;


    public Integer getMainDept() {
        return mainDept;
    }

    public void setMainDept(Integer mainDept) {
        this.mainDept = mainDept;
    }

    public Long getDeptBindId() {
        return deptBindId;
    }

    public void setDeptBindId(Long deptBindId) {
        this.deptBindId = deptBindId;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
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
        return "DeptBind{" +
        "deptBindId=" + deptBindId +
        ", deptId=" + deptId +
        ", userId=" + userId +
        ", tenantId=" + tenantId +
        "}";
    }
}
