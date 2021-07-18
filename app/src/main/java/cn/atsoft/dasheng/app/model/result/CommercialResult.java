package cn.atsoft.dasheng.app.model.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 商机表
 * </p>
 *
 * @author
 * @since 2021-07-16
 */
@Data
@ApiModel
public class CommercialResult implements Serializable {
  private static final long serialVersionUID = 1L;
  /**
   * 商机id
   */
  @ApiModelProperty("商机id")
  private Long commercial_id;
  /**
   * 客户名称
   */
  @ApiModelProperty("客户名称")
  private String name;
  /**
   * 报价编号
   */
  @ApiModelProperty("报价编号")
  private String quote_id;
   /**
   * 机会阶段
   */
  @ApiModelProperty("机会阶段")
  private String phases;
  /**
   *机会来源
   */
  @ApiModelProperty("机会来源")
  private Long source;
  /**
   * 机会状态
   */
  @ApiModelProperty("机会状态")
  private Long state;
  /**
   * 负责人
   */
  @ApiModelProperty("负责人")
  private Long main;
  /**
   * 创建者
   */
  @ApiModelProperty(hidden = true)
  private Long createUser;

  /**
   * 修改者
   */
  @ApiModelProperty(hidden = true)
  private Long updateUser;

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
   * 状态
   */
  @ApiModelProperty("状态")
  private Integer display;
  @ApiModelProperty("父ID顺序数组")
  private List<String> pidValue;
}
