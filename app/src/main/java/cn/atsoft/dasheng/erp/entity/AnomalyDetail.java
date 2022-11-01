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
 *
 * </p>
 *
 * @author song
 * @since 2022-05-27
 */
@TableName("daoxin_anomaly_detail")
public class AnomalyDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "detail_id", type = IdType.ID_WORKER)
    private Long detailId;

    @TableField("anomaly_id")
    private Long anomalyId;

    @TableField("number")
    private Long number;

    /**
     * 实物id
     */
    @TableField("inkind_id")
    private Long inkindId;

    /**
     * 状态
     */
    @TableField("stauts")
    private Long stauts;

    /**
     * 异常原因
     */
    @TableField("remark")
    private String remark;

    /**
     * 原因图片
     */
    @TableField("reason_img")
    private String reasonImg;

    /**
     * 处理人
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 处理意见
     */
    @TableField("opinion")
    private String opinion;

    /**
     * 意见图片
     */
    @TableField("opinion_img")
    private String opinionImg;

    /**
     * 异常表述
     */
    @TableField("description")
    private String description;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(value = "create_user", fill = FieldFill.INSERT)
    private Long createUser;

    @TableField(value = "update_time", fill = FieldFill.UPDATE)
    private Date updateTime;

    @TableField(value = "update_user", fill = FieldFill.UPDATE)
    private Long updateUser;

    @TableField("display")
    private Integer display;

    @TableField("deptId")
    private Long deptId;


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public Long getDetailId() {
        return detailId;
    }

    public void setDetailId(Long detailId) {
        this.detailId = detailId;
    }

    public Long getAnomalyId() {
        return anomalyId;
    }

    public void setAnomalyId(Long anomalyId) {
        this.anomalyId = anomalyId;
    }

    public Long getInkindId() {
        return inkindId;
    }

    public void setInkindId(Long inkindId) {
        this.inkindId = inkindId;
    }

    public Long getStauts() {
        return stauts;
    }

    public void setStauts(Long stauts) {
        this.stauts = stauts;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getReasonImg() {
        return reasonImg;
    }

    public void setReasonImg(String reasonImg) {
        this.reasonImg = reasonImg;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getOpinion() {
        return opinion;
    }

    public void setOpinion(String opinion) {
        this.opinion = opinion;
    }

    public String getOpinionImg() {
        return opinionImg;
    }

    public void setOpinionImg(String opinionImg) {
        this.opinionImg = opinionImg;
    }

    public String getdescription() {
        return description;
    }

    public void setdescription(String description) {
        this.description = description;
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

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }



    @Override
    public String toString() {
        return "AnomalyDetail{" +
                "detailId=" + detailId +
                ", anomalyId=" + anomalyId +
                ", inkindId=" + inkindId +
                ", stauts=" + stauts +
                ", remark=" + remark +
                ", reasonImg=" + reasonImg +
                ", userId=" + userId +
                ", opinion=" + opinion +
                ", opinionImg=" + opinionImg +
                ", description=" + description +
                ", createTime=" + createTime +
                ", createUser=" + createUser +
                ", updateTime=" + updateTime +
                ", updateUser=" + updateUser +
                ", display=" + display +
                ", deptId=" + deptId +
                "}";
    }
}
