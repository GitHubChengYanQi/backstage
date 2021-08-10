package cn.atsoft.dasheng.app.model.result;

import lombok.Data;

import java.util.Date;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * <p>
 * 付款信息表
 * </p>
 *
 * @author ta
 * @since 2021-07-26
 */
@Data
@ApiModel
public class CrmPaymentResult implements Serializable {

    private static final long serialVersionUID = 1L;
    private ItemsResult itemsResult;
    private ErpOrderResult orderResult;
    private OutstockResult outstockResult;

    /**
     * 出库
     */
    @ApiModelProperty("出库")
    private Long outstockId;
    /**
     * 付款信息id
     */
    @ApiModelProperty("付款信息id")
    private Long paymentId;

    /**
     * 物品
     */
    private Long itemId;
    /**
     * 订单编号
     */
    @ApiModelProperty("订单编号")
    private Long orderId;

    /**
     * 付款时间
     */
    @ApiModelProperty("付款时间")
    private Date payTime;

    /**
     * 支付方式
     */
    @ApiModelProperty("支付方式")
    private String payType;

    /**
     * 运费
     */
    @ApiModelProperty("运费")
    private Long freight;

    /**
     * 部门编号
     */
    @ApiModelProperty("部门编号")
    private Long deptId;

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

    /**
     * 出库id
     */
    @ApiModelProperty("出库id")
    private Long deliveryId;
    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
}
