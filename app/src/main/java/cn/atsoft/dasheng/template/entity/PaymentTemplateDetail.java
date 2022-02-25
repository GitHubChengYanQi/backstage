package cn.atsoft.dasheng.template.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 付款模板详情
 * </p>
 *
 * @author song
 * @since 2022-02-24
 */
@TableName("daoxin_payment_template_detail")
public class PaymentTemplateDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 付款详情主见
     */
      @TableId(value = "detail_id", type = IdType.ID_WORKER)
    private Long detailId;

    /**
     * 模板id
     */
    @TableField("template_id")
    private Long templateId;

    /**
     * 金额
     */
    @TableField("money")
    private Integer money;

    /**
     * 百分比
     */
    @TableField("percentum")
    private Integer percentum;

    /**
     * 付款类型
     */
    @TableField("pay_type")
    private Integer payType;

    /**
     * 付款日期
     */
    @TableField("pay_time")
    private Date payTime;

    /**
     * 日期方式
     */
    @TableField("date_way")
    private Integer dateWay;

    /**
     * 日期数
     */
    @TableField("date_number")
    private Long dateNumber;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

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
    @TableField("deptId")
    private Long deptId;


    public Long getDetailId() {
        return detailId;
    }

    public void setDetailId(Long detailId) {
        this.detailId = detailId;
    }

    public Long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }

    public Integer getMoney() {
        return money;
    }

    public void setMoney(Integer money) {
        this.money = money;
    }

    public Integer getPercentum() {
        return percentum;
    }

    public void setPercentum(Integer percentum) {
        this.percentum = percentum;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public Integer getDateWay() {
        return dateWay;
    }

    public void setDateWay(Integer dateWay) {
        this.dateWay = dateWay;
    }

    public Long getDateNumber() {
        return dateNumber;
    }

    public void setDateNumber(Long dateNumber) {
        this.dateNumber = dateNumber;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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
        return "PaymentTemplateDetail{" +
        "detailId=" + detailId +
        ", templateId=" + templateId +
        ", money=" + money +
        ", percentum=" + percentum +
        ", payType=" + payType +
        ", payTime=" + payTime +
        ", dateWay=" + dateWay +
        ", dateNumber=" + dateNumber +
        ", remark=" + remark +
        ", createUser=" + createUser +
        ", updateUser=" + updateUser +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        ", display=" + display +
        ", deptId=" + deptId +
        "}";
    }
}
