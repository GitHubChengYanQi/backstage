package cn.atsoft.dasheng.crm.model.params;

import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import cn.atsoft.dasheng.model.validator.BaseValidatingParam;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
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
public class PaymentParam implements Serializable, BaseValidatingParam {

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


    private static final long serialVersionUID = 1L;

    private List<PaymentDetailParam> detailParams;
    @NotNull
    private Double money;

    private Integer status;

    private Integer type;
    /**
     * 付款信息id
     */
    @ApiModelProperty("付款信息id")
    private Long paymentId;

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
    @NotNull
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

    @Override
    public String checkParam() {
        return null;
    }


    public Long getFloatingAmount() {
        if(ToolUtil.isNotEmpty(floatingAmount)){
            return BigDecimal.valueOf(floatingAmount).multiply(BigDecimal.valueOf(100)).longValue();
        }
        return null;
    }
    public Long getTotalAmount() {
        if(ToolUtil.isNotEmpty(totalAmount)){
            return  BigDecimal.valueOf(totalAmount).multiply(BigDecimal.valueOf(100)).longValue();
        }
        return null;
    }
    public Long getMoney() {
        if(ToolUtil.isNotEmpty(money)){
            return  BigDecimal.valueOf(money).multiply(BigDecimal.valueOf(100)).longValue();
        }
        return null;
    }



}
