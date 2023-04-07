package cn.atsoft.dasheng.production.entity;

import com.baomidou.mybatisplus.annotation.*;
import org.apache.ibatis.type.JdbcType;

import java.util.Date;

import java.io.Serializable;

/**
 * <p>
 * sop 主表
 * </p>
 *
 * @author song
 * @since 2022-02-10
 */
@TableName("daoxin_production_sop")
public class Sop implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * sopId
     */
    @TableId(value = "sop_id", type = IdType.ID_WORKER)
    private Long sopId;

    @TableField("pid")
    private Long pid;

    /**
     * 修改原因
     */
    @TableField("alter_why")
    private String alterWhy;
    /**
     * 编号
     */
    @TableField("coding")
    private String coding;

    /**
     * 工序
     */
    @TableField(value = "ship_setp_id", updateStrategy= FieldStrategy.IGNORED)
//    @TableField()
    private Long shipSetpId;

    /**
     * 名称
     */
    @TableField("name")
    private String name;

    /**
     * 版本号
     */
    @TableField("version_number")
    private String versionNumber;

    /**
     * 成品图
     */
    @TableField("finished_picture")
    private String finishedPicture;

    /**
     * 注意事项
     */
    @TableField("note")
    private String note;

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
@TableField(value = "deptId" , fill = FieldFill.INSERT)
    private Long deptId;
    /**
     * 租户编号
     */
    @TableField(value = "tenant_id" , fill = FieldFill.INSERT)
    private Long tenantId;

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getAlterWhy() {
        return alterWhy;
    }

    public void setAlterWhy(String alterWhy) {
        this.alterWhy = alterWhy;
    }

    public Long getSopId() {
        return sopId;
    }

    public void setSopId(Long sopId) {
        this.sopId = sopId;
    }

    public String getCoding() {
        return coding;
    }

    public void setCoding(String coding) {
        this.coding = coding;
    }

    public Long getShipSetpId() {
        return shipSetpId;
    }

    public void setShipSetpId(Long shipSetpId) {
        this.shipSetpId = shipSetpId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(String versionNumber) {
        this.versionNumber = versionNumber;
    }

    public String getFinishedPicture() {
        return finishedPicture;
    }

    public void setFinishedPicture(String finishedPicture) {
        this.finishedPicture = finishedPicture;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
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

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    @Override
    public String toString() {
        return "Sop{" +
                "sopId=" + sopId +
                ", coding=" + coding +
                ", shipSetpId=" + shipSetpId +
                ", name=" + name +
                ", versionNumber=" + versionNumber +
                ", finishedPicture=" + finishedPicture +
                ", note=" + note +
                ", createUser=" + createUser +
                ", updateUser=" + updateUser +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", display=" + display +
                ", deptId=" + deptId +
                "}";
    }
}
