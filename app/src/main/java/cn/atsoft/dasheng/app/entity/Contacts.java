package cn.atsoft.dasheng.app.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;

/**
 * <p>
 * 联系人表
 * </p>
 *
 * @author
 * @since 2021-07-23
 */
@TableName("daoxin_crm_contacts")
public class Contacts implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 联系人id
     */
    @TableId(value = "contacts_id", type = IdType.ID_WORKER)
    private Long contactsId;
    /**
     * 职位
     */
    @TableField("position_id")
    private Long positionId;

    /**
     * 公司角色
     */
    @TableField("company_role_id")
    private Long companyRole;

    /**
     * 联系人姓名
     */
    @TableField("contacts_name")
    private String contactsName;

    /**
     * 职务
     */
    @TableField("job")
    private String job;

    /**
     * 联系人备注
     */
    @TableField("contacts_remark")
    private String contactsRemark;

    /**
     * 联系电话
     */
    @TableField("phone")
    private Long phone;

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
     * 部门编号
     */
    @TableField(value = "deptId", fill = FieldFill.INSERT)
    private Long deptId;



    @TableField(value = "tenant_id" , fill = FieldFill.INSERT)
    private Long tenantId;

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Long getContactsId() {
        return contactsId;
    }

    public void setContactsId(Long contactsId) {
        this.contactsId = contactsId;
    }

    public String getContactsName() {
        return contactsName;
    }

    public void setContactsName(String contactsName) {
        this.contactsName = contactsName;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public Long getPhone() {
        return phone;
    }

    public void setPhone(Long phone) {
        this.phone = phone;
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

    public String getContactsRemark() {
        return contactsRemark;
    }

    public void setContactsRemark(String contactsRemark) {
        this.contactsRemark = contactsRemark;
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

    public Long getCompanyRole() {
        return companyRole;
    }

    public void setCompanyRole(Long companyRole) {
        this.companyRole = companyRole;
    }

    public Long getPositionId() {
        return positionId;
    }

    public void setPositionId(Long positionId) {
        this.positionId = positionId;
    }

    @Override
    public String toString() {
        return "Contacts{" +
                "contactsId=" + contactsId +
                ", contactsName='" + contactsName + '\'' +
                ", job='" + job + '\'' +
                ", phone=" + phone +
                ", createUser=" + createUser +
                ", updateUser=" + updateUser +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", display=" + display +
                ", deptId=" + deptId +
                '}';
    }
}
