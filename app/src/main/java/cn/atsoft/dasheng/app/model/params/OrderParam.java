package cn.atsoft.dasheng.app.model.params;

import lombok.Data;
import cn.atsoft.dasheng.model.validator.BaseValidatingParam;
import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;

/**
 * <p>
 * 订单表
 * </p>
 *
 * @author ta
 * @since 2021-07-20
 */
@Data
@ApiModel
public class OrderParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;


    /**
     * id
     */
    @ApiModelProperty("id")
    private Long id;


    /**
     * 订单编号
     */
    @ApiModelProperty("订单编号")
    private Long orderId;

    /**
     * 订单人姓名
     */
    @ApiModelProperty("订单人姓名")
    private String name;


    /**
     * 订单地址
     */
    @ApiModelProperty("订单地址")
    private String adressId;

    /**
     * 订单数量
     */
    @ApiModelProperty("订单数量")
    private Long numbers;

    /**
     * 订单状态
     */
    @ApiModelProperty("订单状态")
    private String state;

    /**
     * 联系电话
     */
    @ApiModelProperty("联系电话")
    private Long phone;

    /**
     * 订单时间
     */
    @ApiModelProperty("订单时间")
    private Date orderTime;

    /**
     * 付款时间
     */
    @ApiModelProperty("付款时间")
    private Date payTime;

    /**
     * 发货时间
     */
    @ApiModelProperty("发货时间")
    private Date deliveryTime;

    /**
     * 订单总金额
     */
    @ApiModelProperty("订单总金额")
    private Integer total;

  private Double longitude;
  private Double latitude;
  private Date delivery_time;
  private Date pay_time;
  private Date order_time;
  private String name1;
  private Long order_id;
  private Long adress_id;
  private Long client_id;
  private Long contacts_id;
  private Long tel;
  private String  location;
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

    @Override
    public String checkParam() {
        return null;
    }

}
