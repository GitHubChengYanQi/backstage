package cn.atsoft.dasheng.crm.model.result;

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
 * @author song
 * @since 2022-02-23
 */
@Data
@ApiModel
public class PaymentResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<PaymentDetailResult> detailResults;

    private Integer money;
    /**
     * 付款信息id
     */
    @ApiModelProperty("付款信息id")
    private Long paymentId;

    private Integer type;

    private Integer status;

    /**
     * 订单编号
     */
    @ApiModelProperty("订单编号")
    private Long orderId;

    /**
     * 结算方式
     */
    @ApiModelProperty("结算方式")
    private String payMethod;

    /**
     * 是否运费
     */
    @ApiModelProperty("是否运费")
    private Integer freight;

    /**
     * 名称
     */
    @ApiModelProperty("名称")
    private String name;

    /**
     * 交货方式
     */
    @ApiModelProperty("交货方式")
    private String deliveryWay;

    /**
     * 交货地址
     */
    @ApiModelProperty("交货地址")
    private Long adressId;

    /**
     * 付款方式
     */
    @ApiModelProperty("付款方式")
    private Long payPlan;

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
     * 部门编号
     */
    @ApiModelProperty("部门编号")
    private Long deptId;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remark;
    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
}
