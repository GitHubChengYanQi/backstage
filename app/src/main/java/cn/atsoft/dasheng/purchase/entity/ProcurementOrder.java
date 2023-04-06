package cn.atsoft.dasheng.purchase.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;

/**
 * <p>
 * 采购单
 * </p>
 *
 * @author song
 * @since 2022-01-13
 */
@TableName("daoxin_procurement_order")
public class ProcurementOrder implements Serializable {


    private static final long serialVersionUID = 1L;
    /**
     * 总价格
     */
    @TableField("money")
    private Integer money;

    @TableField("adress_id")
    private Long adressId;

    /**
     * 编码
     */
    @TableField("coding")
    private String coding;

    /**
     * 采购单
     */
    @TableId(value = "procurement_order_id", type = IdType.ID_WORKER)
    private Long procurementOrderId;

    /**
     * 采购计划id
     */
    @TableField("procurement_plan_id")
    private Long procurementPlanId;

    /**
     * 状态
     */
    @TableField("status")
    private Long status;

    /**
     * z
     */
    @TableField("note")
    private String note;
    /**
     * 主题
     */
    @TableField("theme")
    private String theme;

    /**
     * 来源
     */
    @TableField("origin")
    private String origin;

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    /**
     * 删除状态
     */
    @TableField("display")
    private Integer display;

    @TableField(value = "create_user", fill = FieldFill.INSERT)
    private Long createUser;

    @TableField(value = "update_user", fill = FieldFill.UPDATE)
    private Long updateUser;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(value = "update_time", fill = FieldFill.UPDATE)
    private Date updateTime;

    /**
     * 部门id
     */
    @TableField(value = "deptId" , fill = FieldFill.INSERT)
    private Long deptId;

    public Long getAdressId() {
        return adressId;
    }

    public void setAdressId(Long adressId) {
        this.adressId = adressId;
    }

    public Long getProcurementOrderId() {
        return procurementOrderId;
    }

    public void setProcurementOrderId(Long procurementOrderId) {
        this.procurementOrderId = procurementOrderId;
    }

    public Long getProcurementPlanId() {
        return procurementPlanId;
    }

    public void setProcurementPlanId(Long procurementPlanId) {
        this.procurementPlanId = procurementPlanId;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
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

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public Integer getMoney() {
        return money;
    }

    public void setMoney(Integer money) {
        this.money = money;
    }

    public String getCoding() {
        return coding;
    }

    public void setCoding(String coding) {
        this.coding = coding;
    }

    @Override
    public String toString() {
        return "ProcurementOrder{" +
                "procurementOrderId=" + procurementOrderId +
                ", procurementPlanId=" + procurementPlanId +
                ", status=" + status +
                ", note=" + note +
                ", display=" + display +
                ", createUser=" + createUser +
                ", updateUser=" + updateUser +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", deptId=" + deptId +
                "}";
    }
}
