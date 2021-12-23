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
 * 询价任务
 * </p>
 *
 * @author Captain_Jazz
 * @since 2021-12-23
 */
@TableName("daoxin_inquiry_task")
public class InquiryTask implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 询价任务id
     */
    @TableId(value = "inquiry_task_id", type = IdType.ID_WORKER)
    private Long inquiryTaskId;

    /**
     * 询价任务名称
     */
    @TableField("inquiry_task_name")
    private String inquiryTaskName;

    /**
     * 负责人id
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 截至日期
     */
    @TableField("deadline")
    private Date deadline;

    /**
     * 供应商等级
     */
    @TableField("supplier_level")
    private Long supplierLevel;

    /**
     * 是否供应商物料
     */
    @TableField("isSupplier")
    private Integer isSupplier;

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
    private Long display;

    /**
     * 部门id
     */
    @TableField("deptId")
    private Long deptId;


    public Long getInquiryTaskId() {
        return inquiryTaskId;
    }

    public void setInquiryTaskId(Long inquiryTaskId) {
        this.inquiryTaskId = inquiryTaskId;
    }

    public String getInquiryTaskName() {
        return inquiryTaskName;
    }

    public void setInquiryTaskName(String inquiryTaskName) {
        this.inquiryTaskName = inquiryTaskName;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public Long getSupplierLevel() {
        return supplierLevel;
    }

    public void setSupplierLevel(Long supplierLevel) {
        this.supplierLevel = supplierLevel;
    }

    public Integer getIsSupplier() {
        return isSupplier;
    }

    public void setIsSupplier(Integer isSupplier) {
        this.isSupplier = isSupplier;
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

    public Long getDisplay() {
        return display;
    }

    public void setDisplay(Long display) {
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
        return "InquiryTask{" +
                "inquiryTaskId=" + inquiryTaskId +
                ", inquiryTaskName=" + inquiryTaskName +
                ", userId=" + userId +
                ", deadline=" + deadline +
                ", supplierLevel=" + supplierLevel +
                ", isSupplier=" + isSupplier +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", createUser=" + createUser +
                ", updateUser=" + updateUser +
                ", display=" + display +
                ", deptId=" + deptId +
                "}";
    }
}
