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
 * 报价表
 * </p>
 *
 * @author cheng
 * @since 2021-07-19
 */
@TableName("daoxin_crm_track")
public class Track implements Serializable {

    private static final long serialVersionUID = 1L;

    private  String time;
    /**
     * 报价编号
     */
    @TableId(value = "track_id", type = IdType.ID_WORKER)
    private Long trackId;

    /**
     * 商机编号
     */
    @TableField("business_id")
    private Long businessId;

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    /**
     * 商品名称
     */
    @TableField("stock_id")
    private Long stockId;

    private String note;

    /**
     * 报价金额
     */
    @TableField("money")
    private Integer money;

    /**
     * 数量
     */
    @TableField("number")
    private Long number;

    /**
     * 报价阶段
     */
    @TableField("stage")
    private Integer stage;

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
      private Boolean display;

  public String getTime() {
    return time;
  }

  public void setTime(String time) {
    this.time = time;
  }

  public Long getTrackId() {
    return trackId;
  }

  public void setTrackId(Long trackId) {
    this.trackId = trackId;
  }

  public Long getBusinessId() {
    return businessId;
  }

  public void setBusinessId(Long businessId) {
    this.businessId = businessId;
  }

  public Long getStockId() {
    return stockId;
  }

  public void setStockId(Long stockId) {
    this.stockId = stockId;
  }

  public Integer getMoney() {
    return money;
  }

  public void setMoney(Integer money) {
    this.money = money;
  }

  public Long getNumber() {
    return number;
  }

  public void setNumber(Long number) {
    this.number = number;
  }

  public Integer getStage() {
    return stage;
  }

  public void setStage(Integer stage) {
    this.stage = stage;
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

  public Boolean getDisplay() {
    return display;
  }

  public void setDisplay(Boolean display) {
    this.display = display;
  }

  @Override
  public String toString() {
    return "Track{" +
      "time='" + time + '\'' +
      ", trackId=" + trackId +
      ", businessId=" + businessId +
      ", stockId=" + stockId +
      ", note='" + note + '\'' +
      ", money=" + money +
      ", number=" + number +
      ", stage=" + stage +
      ", createTime=" + createTime +
      ", createUser=" + createUser +
      ", updateTime=" + updateTime +
      ", updateUser=" + updateUser +
      ", display=" + display +
      '}';
  }
}
