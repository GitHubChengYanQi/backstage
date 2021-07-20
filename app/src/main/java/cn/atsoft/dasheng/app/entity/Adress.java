package cn.atsoft.dasheng.app.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 客户地址表
 * </p>
 *
 * @author ta
 * @since 2021-07-19
 */
@TableName("daoxin_adress")
public class Adress implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 地址id
     */
      @TableId(value = "adress_id", type = IdType.ID_WORKER)
    private Long adressId;
  /**
   * 客户id
   */
  @TableField("client_id")
  private Long client_id;

    /**
     * 客户名称
     */
    @TableField("name")
    private String name;

    /**
     * 地址
     */
    @TableField("location")
    private String location;

    /**
     * 经度
     */
    @TableField("longitude")
    private Double longitude;

    /**
     * 纬度
     */
    @TableField("latitude")
    private Double latitude;

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


  public Long getAdressId() {
    return adressId;
  }

  public void setAdressId(Long adressId) {
    this.adressId = adressId;
  }

  public Long getClient_id() {
    return client_id;
  }

  public void setClient_id(Long client_id) {
    this.client_id = client_id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public Double getLongitude() {
    return longitude;
  }

  public void setLongitude(Double longitude) {
    this.longitude = longitude;
  }

  public Double getLatitude() {
    return latitude;
  }

  public void setLatitude(Double latitude) {
    this.latitude = latitude;
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


  @Override
  public String toString() {
    return "Adress{" +
      "adressId=" + adressId +
      ", client_id=" + client_id +
      ", name='" + name + '\'' +
      ", location='" + location + '\'' +
      ", longitude=" + longitude +
      ", latitude=" + latitude +
      ", createUser=" + createUser +
      ", updateUser=" + updateUser +
      ", createTime=" + createTime +
      ", updateTime=" + updateTime +
      ", display=" + display +
      '}';
  }
}
