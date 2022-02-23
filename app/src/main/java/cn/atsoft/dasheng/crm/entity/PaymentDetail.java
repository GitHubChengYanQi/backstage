package cn.atsoft.dasheng.crm.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 付款详情
 * </p>
 *
 * @author song
 * @since 2022-02-23
 */
@TableName("daoxin_crm_payment_detail")
public class PaymentDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 付款详情主见
     */
      @TableId(value = "detail_id", type = IdType.ID_WORKER)
    private Long detailId;

    /**
     * 付款信息id
     */
    @TableField("payment_id")
    private Long paymentId;

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


    public Long getDetailId() {
        return detailId;
    }

    public void setDetailId(Long detailId) {
        this.detailId = detailId;
    }

    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
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

    @Override
    public String toString() {
        return "PaymentDetail{" +
        "detailId=" + detailId +
        ", paymentId=" + paymentId +
        ", money=" + money +
        ", percentum=" + percentum +
        ", payType=" + payType +
        ", payTime=" + payTime +
        ", dateWay=" + dateWay +
        ", dateNumber=" + dateNumber +
        ", remark=" + remark +
        "}";
    }
}
