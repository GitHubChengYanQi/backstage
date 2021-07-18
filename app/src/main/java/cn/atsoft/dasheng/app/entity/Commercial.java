package cn.atsoft.dasheng.app.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 商机表
 * </p>
 *
 * @author
 * @since 2021-07-16
 */
@TableName("daoxin_commercial")
public class Commercial implements Serializable {
  private static final long serialVersionUID = 1L;
  /**
   * 商机id
   */
  @TableId(value = "commercial_id", type = IdType.ID_WORKER)
  private Long commercial_id;
  /**
   * 客户名称
   */
  @TableField("name")
  private String name;
  /**
   * 报价编号
   */
  @TableField("quote_id")
  private String quote_id;
  /**
   * 机会阶段
   */
  @TableField("phases")
  private String phases;

  /**
   * 机会来源
   */
  @TableField("source")
  private String source;

  /**
   * 机会状态
   */
  @TableField("state")
  private String state;

  /**
   * 负责人
   */
  @TableField("main")
  private String main;

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
   * 创建用户
   */
  @TableField(value = "create_user", fill = FieldFill.INSERT)
  private Long createUser;

  /**
   * 修改用户
   */
  @TableField(value = "update_user", fill = FieldFill.UPDATE)
  private Long updateUser;

  /**
   * 状态
   */
  @TableField("display")
  private Integer display;


  public Long getCommercial_id() {
    return commercial_id;
  }

  public void setCommercial_id(Long commercial_id) {
    this.commercial_id = commercial_id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getQuote_id() {
    return quote_id;
  }

  public void setQuote_id(String quote_id) {
    this.quote_id = quote_id;
  }

  public String getPhases() {
    return phases;
  }

  public void setPhases(String phases) {
    this.phases = phases;
  }

  public String getSource() {
    return source;
  }

  public void setSource(String source) {
    this.source = source;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public String getMain() {
    return main;
  }

  public void setMain(String main) {
    this.main = main;
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

  public Integer getDisplay() {
    return display;
  }

  public void setDisplay(Integer display) {
    this.display = display;
  }

  @Override
  public String toString() {
    return "Commercial{" +
      "commercial_id=" + commercial_id +
      ", name='" + name + '\'' +
      ", quote_id='" + quote_id + '\'' +
      ", phases='" + phases + '\'' +
      ", source='" + source + '\'' +
      ", state='" + state + '\'' +
      ", main='" + main + '\'' +
      ", createTime=" + createTime +
      ", updateTime=" + updateTime +
      ", createUser=" + createUser +
      ", updateUser=" + updateUser +
      ", display=" + display +
      '}';
  }
}
