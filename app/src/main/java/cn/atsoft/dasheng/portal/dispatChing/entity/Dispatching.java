package cn.atsoft.dasheng.portal.dispatChing.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 派工表
 * </p>
 *
 * @author 
 * @since 2021-08-23
 */
@TableName("daoxin_portal_dispatching")
public class Dispatching implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 派工id
     */
      @TableId(value = "dispatching_id", type = IdType.ID_WORKER)
    private Long dispatchingId;


    /**
     * 姓名
     */
    @TableField("name")
    private Long name;

    /**
     * 手机号
     */
    @TableField("phone")
    private String phone;

    /**
     * 预计到达时间
     */
    @TableField("time")
    private Date time;

    /**
     * 负责区域
     */
    @TableField("address")
    private String address;

    /**
     * 状态
     */
    @TableField("state")
    private Integer state;

    /**
     * 备注
     */
    @TableField("note")
    private String note;

    /**
     * 完成照片
     */
    @TableField("imgUrl")
    private String imgUrl;

    /**
     * 评价
     */
    @TableField("evaluation")
    private String evaluation;

    public String getEvaluations() {
        return evaluations;
    }

    public void setEvaluations(String evaluations) {
        this.evaluations = evaluations;
    }

    @TableField("evaluations")
    private String evaluations;

    /**
     * 报修id
     */
    @TableField("repair_id")
    private Long repairId;

    /**
     * 创建时间
     */
      @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 创建者
     */
      @TableField(value = "create_user", fill = FieldFill.INSERT)
    private Long createUser;

    /**
     * 修改时间
     */
      @TableField(value = "update_time", fill = FieldFill.UPDATE)
    private Date updateTime;

    /**
     * 修改者
     */
      @TableField(value = "update_user", fill = FieldFill.UPDATE)
    private Long updateUser;

    /**
     * 状态
     */
    @TableField("display")
    private Integer display;

    /**
     * 部门id
     */
    @TableField(value = "deptId",fill =FieldFill.INSERT)
    private Long deptId;
    @TableField(value = "tenant_id" , fill = FieldFill.INSERT)
    private Long tenantId;

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }
    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }
    public Long getDispatchingId() {
        return dispatchingId;
    }

    public void setDispatchingId(Long dispatchingId) {
        this.dispatchingId = dispatchingId;
    }

    public Long getName() {
        return name;
    }

    public void setName(Long name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(String evaluation) {
        this.evaluation = evaluation;
    }

    public Long getRepairId() {
        return repairId;
    }

    public void setRepairId(Long repairId) {
        this.repairId = repairId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getCreateUser() {
        return createUser;
    }

    public void setCreateUser(Long createUser) {
        this.createUser = createUser;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Long getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(Long updateUser) {
        this.updateUser = updateUser;
    }

    public Integer getDisplay() {
        return display;
    }

    public void setDisplay(Integer display) {
        this.display = display;
    }


    @Override
    public String toString() {
        return "Dispatching{" +
        "dispatchingId=" + dispatchingId +
        ", name=" + name +
        ", phone=" + phone +
        ", time=" + time +
        ", address=" + address +
        ", state=" + state +
        ", note=" + note +
        ", imgUrl=" + imgUrl +
        ", evaluation=" + evaluation +
        ", repairId=" + repairId +
        ", createTime=" + createTime +
        ", createUser=" + createUser +
        ", updateTime=" + updateTime +
        ", updateUser=" + updateUser +
        ", display=" + display +
        "}";
    }
}
