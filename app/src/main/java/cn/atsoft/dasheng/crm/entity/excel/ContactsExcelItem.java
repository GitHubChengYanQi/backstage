package cn.atsoft.dasheng.crm.entity.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
@TableName("daoxin_crm_contacts")
public class ContactsExcelItem implements Serializable{

    /**
     * <p>
     * 联系人表
     * </p>
     *
     * @author
     * @since 2021-07-23
     */
        private static final long serialVersionUID = 1L;
        /**
         * 联系人id
         */
        private Long contactsId;
        /**
         * 公司角色
         */
        @Excel(name = "公司角色")
        @TableField("company_role_id")
        private Long companyRole;

        /**
         * 联系人姓名
         */
        @Excel(name = "联系人姓名")
        @TableField("contacts_name")
        private String contactsName;

        /**
         * 职务
         */
        @Excel(name = "职务")
        @TableField("job")
        private String job;

        /**
         * 联系电话
         */
        @Excel(name = "联系电话")
        @TableField("phone")
        private Long phone;

        /**
         * 创建者
         */
        private Long createUser;

        /**
         * 修改者
         */

        private Long updateUser;

        /**
         * 创建时间
         */
        private Date createTime;

        /**
         * 修改时间
         */

        private Date updateTime;

        /**
         * 状态
         */
        @TableField("display")
        private Integer display;

        /**
         * 部门编号
         */
        @TableField(value = "deptId",fill =FieldFill.INSERT)
        private Long deptId;



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
