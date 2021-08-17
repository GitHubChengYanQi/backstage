package cn.atsoft.dasheng.uc.model.params;

import cn.atsoft.dasheng.portal.model.validator.BaseValidatingParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

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
public class UcPayOrderParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;


    /**
     * 支付编码
     */
    @ApiModelProperty(value = "支付编码",hidden = true)
    private Long payId;

    @ApiModelProperty("支付标题")
    private String title;

    @ApiModelProperty("支付描述")
    private String body;

    /**
     * 支付配置，余额对应币种的可用额度，在线支付额度，支付单号，支付完成通知地址等配置信息
     */
    @ApiModelProperty("支付配置，余额对应币种的可用额度，在线支付额度，支付单号，支付完成通知地址等配置信息")
    private PayDetails payDetails;

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

    @ApiModelProperty("支付类型")
    private String payType;

    @ApiModelProperty(value = "状态",hidden = true)
    private Integer status;

    @ApiModelProperty(hidden = true)
    private Date createTime;

    @ApiModelProperty(hidden = true)
    private Date updateTime;

    @Override
    public String checkParam() {
        return null;
    }

}
