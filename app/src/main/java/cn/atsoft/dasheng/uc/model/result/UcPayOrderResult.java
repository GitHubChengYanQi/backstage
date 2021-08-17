package cn.atsoft.dasheng.uc.model.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 
 * </p>
 *
 * @author Sing
 * @since 2021-03-21
 */
@Data
@ApiModel
public class UcPayOrderResult implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 支付编码
     */
    @ApiModelProperty("支付编码")
    private Long payId;

    /**
     * 支付配置，余额对应币种的可用额度，在线支付额度，支付单号，支付完成通知地址等配置信息
     */
    @ApiModelProperty("支付配置，余额对应币种的可用额度，在线支付额度，支付单号，支付完成通知地址等配置信息")
    private String payDetails;

    /**
     * 商户订单号
     */
    @ApiModelProperty("商户订单号")
    private String outTradeNo;

    /**
     * 异步通知地址
     */
    @ApiModelProperty("异步通知地址")
    private String notifyUrl;

    /**
     * 支付金额
     */
    @ApiModelProperty("支付金额")
    private Integer totalAmount;

    @ApiModelProperty("")
    private String payType;

    @ApiModelProperty("")
    private Integer status;

    @ApiModelProperty(hidden = true)
    private Date createTime;

    @ApiModelProperty(hidden = true)
    private Date updateTime;
    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
}
