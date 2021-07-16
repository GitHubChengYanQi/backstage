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
 * 物流表
 * </p>
 *
 * @author 
 * @since 2021-07-15
 */
@TableName("daoxin_logistics")
public class Logistics implements Serializable {

    private static final long serialVersionUID = 1L;
    private  Date outtime;

    public Date getOuttime() {
        return outtime;
    }

    public void setOuttime(Date outtime) {
        this.outtime = outtime;
    }

    /**
     * 物流id
     */
      @TableId(value = "logistics_id", type = IdType.ID_WORKER)
    private Long logisticsId;

    /**
     * 发货编号
     */
    @TableField("order_id")
    private Long orderId;

    /**
     * 到货地址
     */
    @TableField("client_id")
    private Long clientId;

    /**
     * 当前位置
     */
    @TableField("position")
    private String position;

    /**
     * 到货地址
     */
    @TableField("adress")
    private String adress;

    /**
     * 物流电话
     */
    @TableField("phone")
    private Long phone;

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


    public Long getLogisticsId() {
        return logisticsId;
    }

    public void setLogisticsId(Long logisticsId) {
        this.logisticsId = logisticsId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public Long getPhone() {
        return phone;
    }

    public void setPhone(Long phone) {
        this.phone = phone;
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
        return "Logistics{" +
        "logisticsId=" + logisticsId +
        ", orderId=" + orderId +
        ", clientId=" + clientId +
        ", position=" + position +
        ", adress=" + adress +
                ", outtime=" + outtime +
        ", phone=" + phone +
        ", createTime=" + createTime +
        ", createUser=" + createUser +
        ", updateTime=" + updateTime +
        ", updateUser=" + updateUser +
        ", display=" + display +
        "}";
    }
}
