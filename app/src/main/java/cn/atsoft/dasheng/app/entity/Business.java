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
 * 商机表
 * </p>
 *
 * @author cheng
 * @since 2021-07-19
 */
@TableName("daoxin_crm_business")
public class Business implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 商机id
     */
    @TableId(value = "business_id", type = IdType.ID_WORKER)
    private Long businessId;

    /**
     * 客户id
     */
    @TableField("customer_id")
    private Long customerId;


  /**
   * 物品名称
   */
  /**
   * 物品名称
   */

    @TableField("stock_id")
    private  Long stockId;

    /**
     * 机会来源
     */
    @TableField("origin_id")
    private long originId;

    /**
     * 立项日期
     */
    @TableField("time")
    private String time;

    /**
     * 商机状态
     */
    @TableField("state")
    private String state;

    /**
     * 商机阶段
     */
    @TableField("stage")
    private String stage;

    /**
     * 负责人
     */
    @TableField("person")
    private Long person;

  /**
   * 部门编号
   */
  @TableField("deptId")
  private Long deptId;

  /**
   * 商机名称
   */
  @TableField("business_name")
  private String businessName ;

  /**
   * 结单日期
   */
  @TableField("statement_time")
  private Date statementTime ;

  /**
   * 销售流程id
   */
  @TableField("sales_id")
  private Long salesId ;

  /**
   * 赢率id
   */
  @TableField("sales_process_id")
  private Long salesProcessId ;

  /**
   * 阶段变更时间
   */
  @TableField("change_time")
  private Date changeTime ;

  /**
   * 商机金额
   */
  @TableField("opportunity_amount")
  private int opportunityAmount ;

  /**
   * 产品合计
   */
  @TableField("total_products")
  private Double totalProducts ;

  /**
   * 整单折扣
   */
  @TableField("order_discount")
  private String orderDiscount ;


  /**
   *输单原因
   */
  @TableField("reason")
  private String reason ;

  /**
   *主线索
   */
  @TableField("main_cable")
  private String mainCable ;


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
  /**
   * 部门编号
   */
  @TableField("deptId")
  private Long deptId;

  /**
   * 商机名称
   */
  @TableField("business_name")
  private String businessName;

  /**
   * 结单日期
   */
  @TableField("statement_time")
  private Date statementTime;

  /**
   * 销售流程id
   */
  @TableField("sales_id")
  private Long salesId;

  /**
   * 赢率id
   */
  @TableField("sales_process_id")
  private Long salesProcessId;

  /**
   * 阶段变更时间
   */
  @TableField("change_time")
  private Date changeTime;

  /**
   * 商机金额
   */
  @TableField("opportunity_amount")
  private int opportunityAmount;

  /**
   * 产品合计
   */
  @TableField("total_products")
  private Double totalProducts;

  /**
   * 整单折扣
   */
  @TableField("order_discount")
  private String orderDiscount;

  /**
   * 输单原因
   */
  @TableField("reason")
  private String reason;

  /**
   * 主线索
   */
  @TableField("main_cable")
  private String mainCable;

  public Long getBusinessId() {
    return businessId;
  }

  public void setBusinessId(Long businessId) {
    this.businessId = businessId;
  }

  public Long getCustomerId() {
    return customerId;
  }

  public void setCustomerId(Long customerId) {
    this.customerId = customerId;
  }

  public Long getStockId() {
    return stockId;
  }

  public void setStockId(Long stockId) {
    this.stockId = stockId;
  }

  public long getOriginId() {
    return originId;
  }

  public void setOriginId(long originId) {
    this.originId = originId;
  }

  public String getTime() {
    return time;
  }

  public void setTime(String time) {
    this.time = time;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public String getStage() {
    return stage;
  }

  public void setStage(String stage) {
    this.stage = stage;
  }

  public Long getPerson() {
    return person;
  }

  public void setPerson(Long person) {
    this.person = person;
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


  public Long getBusinessId() {
    return businessId;
  }

  public void setBusinessId(Long businessId) {
    this.businessId = businessId;
  }

  public Long getCustomerId() {
    return customerId;
  }

  public void setCustomerId(Long customerId) {
    this.customerId = customerId;
  }

  public Long getStockId() {
    return stockId;
  }

  public void setStockId(Long stockId) {
    this.stockId = stockId;
  }

  public long getOriginId() {
    return originId;
  }

  public void setOriginId(long originId) {
    this.originId = originId;
  }

  public String getTime() {
    return time;
  }

  public void setTime(String time) {
    this.time = time;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public String getStage() {
    return stage;
  }

  public void setStage(String stage) {
    this.stage = stage;
  }

  public Long getPerson() {
    return person;
  }

  public void setPerson(Long person) {
    this.person = person;
  }

  public Long getDeptId() {
    return deptId;
  }

  public void setDeptId(Long deptId) {
    this.deptId = deptId;
  }

  public String getBusinessName() {
    return businessName;
  }

  public void setBusinessName(String businessName) {
    this.businessName = businessName;
  }

  public Date getStatementTime() {
    return statementTime;
  }

  public void setStatementTime(Date statementTime) {
    this.statementTime = statementTime;
  }

  public Long getSalesId() {
    return salesId;
  }

  public void setSalesId(Long salesId) {
    this.salesId = salesId;
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

  public String getBusinessName() {
    return businessName;
  }

  public void setBusinessName(String businessName) {
    this.businessName = businessName;
  }

  public Date getStatementTime() {
    return statementTime;
  }

  public void setStatementTime(Date statementTime) {
    this.statementTime = statementTime;
  }

  public Long getSalesId() {
    return salesId;
  }

  public void setSalesId(Long salesId) {
    this.salesId = salesId;
  }

  public Long getSalesProcessId() {
    return salesProcessId;
  }

  public void setSalesProcessId(Long salesProcessId) {
    this.salesProcessId = salesProcessId;
  }

  public Date getChangeTime() {
    return changeTime;
  }

  public void setChangeTime(Date changeTime) {
    this.changeTime = changeTime;
  }

  public int getOpportunityAmount() {
    return opportunityAmount;
  }

  public void setOpportunityAmount(int opportunityAmount) {
    this.opportunityAmount = opportunityAmount;
  }

  public Double getTotalProducts() {
    return totalProducts;
  }

  public void setTotalProducts(Double totalProducts) {
    this.totalProducts = totalProducts;
  }

  public String getOrderDiscount() {
    return orderDiscount;
  }

  public void setOrderDiscount(String orderDiscount) {
    this.orderDiscount = orderDiscount;
  }

  public String getReason() {
    return reason;
  }

  public void setReason(String reason) {
    this.reason = reason;
  }

  public String getMainCable() {
    return mainCable;
  }

  public void setMainCable(String mainCable) {
    this.mainCable = mainCable;
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
    return "Business{" +
      "businessId=" + businessId +
      ", customerId=" + customerId +
      ", stockId=" + stockId +
      ", originId=" + originId +
      ", time='" + time + '\'' +
      ", state='" + state + '\'' +
      ", stage='" + stage + '\'' +
      ", person=" + person +
      ", createTime=" + createTime +
      ", updateTime=" + updateTime +
      ", createUser=" + createUser +
      ", updateUser=" + updateUser +
      ", display=" + display +
      ", deptId=" + deptId +
      ", businessName='" + businessName + '\'' +
      ", statementTime=" + statementTime +
      ", salesId=" + salesId +
      ", salesProcessId=" + salesProcessId +
      ", changeTime=" + changeTime +
      ", opportunityAmount=" + opportunityAmount +
      ", totalProducts=" + totalProducts +
      ", orderDiscount='" + orderDiscount + '\'' +
      ", reason='" + reason + '\'' +
      ", mainCable='" + mainCable + '\'' +
      ", createTime=" + createTime +
      ", updateTime=" + updateTime +
      ", createUser=" + createUser +
      ", updateUser=" + updateUser +
      ", display=" + display +
      '}';
  }
}
