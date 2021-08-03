package cn.atsoft.dasheng.app.model.result;

import lombok.Data;

import java.util.Date;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * <p>
 * 商机表
 * </p>
 *
 * @author cheng
 * @since 2021-07-19
 */
@Data
@ApiModel
public class BusinessResult implements Serializable {

    private static final long serialVersionUID = 1L;
      private String originName;
      private String name;
      private String account;
    private Long originId;
    /**
     * 商机id
     */
    @ApiModelProperty("商机id")
    private Long businessId;

    /**
     * 客户id
     */
    @ApiModelProperty("客户id")
    private Long customerId;

    /**
     * 立项日期
     */
    @ApiModelProperty("立项日期")
    private String time;

    /**
     * 商机状态
     */
    @ApiModelProperty("商机状态")
    private String state;

    /**
     * 商机阶段
     */
    @ApiModelProperty("商机阶段")
    private String stage;

    /**
     * 负责人
     */
    @ApiModelProperty("负责人")
    private Long person;

  /**
   * 部门编号
   */
  @ApiModelProperty("部门编号")
  private Long deptId;

  /**
   * 商机名称
   */
  @ApiModelProperty("商机名称")
  private String businessName;

  /**
   * 结单日期
   */
  @ApiModelProperty("结单日期")
  private Date statementTime;

  /**
   * 销售流程id
   */
  @ApiModelProperty("销售流程id")
  private Long salesId;


  /**
   * 赢率id
   */
  @ApiModelProperty("赢率id")
  private Long salesProcessId;

  /**
   * 阶段变更时间
   */
  @ApiModelProperty("阶段变更时间")
  private Date changeTime;


  /**
   * 商机金额
   */
  @ApiModelProperty("商机金额")
  private int opportunityAmount;


  /**
   * 产品合计
   */
  @ApiModelProperty("产品合计")
  private Double totalProducts;

  /**
   * 整单折扣
   */
  @ApiModelProperty("整单折扣")
  private String orderDiscount;

  /**
   * 输单原因
   */
  @ApiModelProperty("整单折扣")
  private String reason;

  /**
   * 主线索
   */
  @ApiModelProperty("主线索")
  private String mainCable;


    /**
     * 创建时间
     */
    @ApiModelProperty(hidden = true)
    private Date createTime;

    /**
     * 修改时间
     */
    @ApiModelProperty(hidden = true)
    private Date updateTime;

    /**
     * 创建用户
     */
    @ApiModelProperty(hidden = true)
    private Long createUser;

    /**
     * 修改用户
     */
    @ApiModelProperty(hidden = true)
    private Long updateUser;

    /**
     * 状态
     */
    @ApiModelProperty("状态")
    private Integer display;


    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
}
