package cn.atsoft.dasheng.crm.model.result;

import cn.atsoft.dasheng.core.util.ToolUtil;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.math.BigDecimal;
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

    /**
     * 浮动金额
     */

    private Double floatingAmount;

    /**
     * 总金额
     */
    private Double totalAmount;

    /**
     * 票据类型
     */
    private Integer paperType;

    /**
     * 税率
     */
    private Long rate;

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
    @JSONField(serialize = false)
    private Long createUser;

    /**
     * 修改者
     */
    @ApiModelProperty(hidden = true)
    @JSONField(serialize = false)
    private Long updateUser;

    /**
     * 创建时间
     */
    @ApiModelProperty(hidden = true)
    @JSONField(serialize = false)
    private Date createTime;

    /**
     * 修改时间
     */
    @ApiModelProperty(hidden = true)
    @JSONField(serialize = false)
    private Date updateTime;

    /**
     * 状态
     */
    @ApiModelProperty("状态")
    @JSONField(serialize = false)
    private Integer display;

    /**
     * 部门编号
     */
    @ApiModelProperty("部门编号")
    @JSONField(serialize = false)
    private Long deptId;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remark;
    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;


    public Double getFloatingAmount() {
        if(ToolUtil.isNotEmpty(floatingAmount)){
            return BigDecimal.valueOf(floatingAmount).divide(BigDecimal.valueOf(100),2).doubleValue();

        }
        return null;
    }
    public Double getTotalAmount() {
        if(ToolUtil.isNotEmpty(totalAmount)){
            return BigDecimal.valueOf(totalAmount).divide(BigDecimal.valueOf(100),2).doubleValue();
        }
        return null;
    }
}
