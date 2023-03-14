package cn.atsoft.dasheng.crm.model.params;

import cn.atsoft.dasheng.core.util.ToolUtil;
import lombok.Data;
import cn.atsoft.dasheng.model.validator.BaseValidatingParam;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 发票
 * </p>
 *
 * @author song
 * @since 2022-04-22
 */
@Data
@ApiModel
public class InvoiceBillParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;


    /**
     * 发票id
     */
    @ApiModelProperty("发票id")
    private Long invoiceBillId;

    /**
     * 金额
     */
    @ApiModelProperty("金额")
    private Double money;

    /**
     * 附件
     */
    @ApiModelProperty("附件")
    private String enclosureId;

    /**
     * 发票名称
     */
    @ApiModelProperty("发票名称")
    private String name;

    /**
     * 发票日期
     */
    @ApiModelProperty("发票日期")
    private Date invoiceDate;

    @ApiModelProperty(hidden = true)
    private Date createTime;

    @ApiModelProperty(hidden = true)
    private Long createUser;

    @ApiModelProperty(hidden = true)
    private Date updateTime;

    @ApiModelProperty(hidden = true)
    private Long updateUser;

    @ApiModelProperty("")
    private Integer display;

    /**
     * 部门编号
     */
    @ApiModelProperty("部门编号")
    private Long deptId;

    /**
     * 关联订单
     */
    @ApiModelProperty("关联订单")
    private Long orderId;


    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;

    public Long getMoney() {
        if (ToolUtil.isEmpty(money)){
            return null;
        }
        return BigDecimal.valueOf(money).multiply(BigDecimal.valueOf(100)).longValue();
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    @Override
    public String checkParam() {
        return null;
    }

}
