package cn.atsoft.dasheng.production.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 工序分类表
 * </p>
 *
 * @author song
 * @since 2022-02-10
 */
@TableName("daoxin_ship_setp_class")
public class ShipSetpClass implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 工序分类id
     */
      @TableId(value = "ship_setp_class_id", type = IdType.ID_WORKER)
    private Long shipSetpClassId;

    /**
     * 工序分类名称
     */
    @TableField("ship_setp_class_name")
    private String shipSetpClassName;

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
     * 部门id
     */
    @TableField(value = "deptId" , fill = FieldFill.INSERT)
    private Long deptId;


    public Long getShipSetpClassId() {
        return shipSetpClassId;
    }

    public void setShipSetpClassId(Long shipSetpClassId) {
        this.shipSetpClassId = shipSetpClassId;
    }

    public String getShipSetpClassName() {
        return shipSetpClassName;
    }

    public void setShipSetpClassName(String shipSetpClassName) {
        this.shipSetpClassName = shipSetpClassName;
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

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    @Override
    public String toString() {
        return "DaoxinShipSetpClass{" +
        "shipSetpClassId=" + shipSetpClassId +
        ", shipSetpClassName=" + shipSetpClassName +
        ", createUser=" + createUser +
        ", updateUser=" + updateUser +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        ", display=" + display +
        ", deptId=" + deptId +
        "}";
    }
}
