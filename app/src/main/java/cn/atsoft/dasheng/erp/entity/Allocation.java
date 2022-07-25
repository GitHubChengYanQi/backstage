package cn.atsoft.dasheng.erp.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 调拨主表
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-07-13
 */
@TableName("daoxin_erp_allocation")
public class Allocation implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 调拨id
     */
      @TableId(value = "allocation_id", type = IdType.ID_WORKER)
    private Long allocationId;

    /**
     * 编码
     */
    @TableField("coding")
    private String coding;

    @TableField("allocation_name")
    private String allocationName;

    /**
     * 仓库id
     */
    @TableField("storehouse_id")
    private Long storehouseId;

    /**
     * 负责人
     */
    @TableField("user_id")
    private Long userId;

    @TableField("reason")
    private String reason;

    @TableField("remark")
    private String remark;

    @TableField("note")
    private String note;

    /**
     * 附件
     */
    @TableField("enclosure")
    private String enclosure;

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
     * 调入/调出
     */
    @TableField("allocation_type")
    private Integer allocationType;

    /**
     * 部门id
     */
    @TableField("deptId")
    private Long deptId;

    /**
     * 状态
     */
    @TableField("status")
    private Long status;

    /**
     * 主题
     */
    @TableField("theme")
    private String theme;

    /**
     * 来源Json字符串
     */
    @TableField("origin")
    private String origin;

    /**
     * 库间调拨  仓库调拨
     */
    @TableField("type")
    private String type;


    public Long getAllocationId() {
        return allocationId;
    }

    public void setAllocationId(Long allocationId) {
        this.allocationId = allocationId;
    }

    public String getCoding() {
        return coding;
    }

    public void setCoding(String coding) {
        this.coding = coding;
    }

    public String getAllocationName() {
        return allocationName;
    }

    public void setAllocationName(String allocationName) {
        this.allocationName = allocationName;
    }

    public Long getStorehouseId() {
        return storehouseId;
    }

    public void setStorehouseId(Long storehouseId) {
        this.storehouseId = storehouseId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getEnclosure() {
        return enclosure;
    }

    public void setEnclosure(String enclosure) {
        this.enclosure = enclosure;
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

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

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

    public Integer getAllocationType() {
        return allocationType;
    }

    public void setAllocationType(Integer allocationType) {
        this.allocationType = allocationType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Allocation{" +
        "allocationId=" + allocationId +
        ", coding=" + coding +
        ", allocationName=" + allocationName +
        ", storehouseId=" + storehouseId +
        ", userId=" + userId +
        ", reason=" + reason +
        ", remark=" + remark +
        ", note=" + note +
        ", enclosure=" + enclosure +
        ", createUser=" + createUser +
        ", updateUser=" + updateUser +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        ", display=" + display +
        ", deptId=" + deptId +
        ", status=" + status +
        ", theme=" + theme +
        ", origin=" + origin +
        ", type=" + type +
        "}";
    }
}
