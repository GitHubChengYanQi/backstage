package cn.atsoft.dasheng.crm.model.result;

import cn.atsoft.dasheng.core.util.ToolUtil;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * <p>
 * 付款详情
 * </p>
 *
 * @author song
 * @since 2022-02-23
 */
@Data
@ApiModel
public class PaymentDetailResult implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 付款详情主见
     */
    @ApiModelProperty("付款详情主见")
    private Long detailId;

    private Integer status;


    private Integer realPay;
    /**
     * 付款信息id
     */
    @ApiModelProperty("付款信息id")
    private Long paymentId;

    /**
     * 金额
     */
    @ApiModelProperty("金额")
    private Integer money;

    /**
     * 百分比
     */
    @ApiModelProperty("百分比")
    private Integer percentum;

    /**
     * 付款类型
     */
    @ApiModelProperty("付款类型")
    private Integer payType;

    /**
     * 付款日期
     */
    @ApiModelProperty("付款日期")
    private Date payTime;

    /**
     * 日期方式
     */
    @ApiModelProperty("日期方式")
    private Integer dateWay;

    /**
     * 日期数
     */
    @ApiModelProperty("日期数")
    private Long dateNumber;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remark;
    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;

}
