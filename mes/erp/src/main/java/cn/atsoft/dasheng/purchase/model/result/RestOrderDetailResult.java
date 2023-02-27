package cn.atsoft.dasheng.purchase.model.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 订单明细表
 * </p>
 *
 * @author song
 * @since 2022-02-23
 */
@Data
@ApiModel
public class RestOrderDetailResult implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 订单id
     */
    @ApiModelProperty("订单id")
    private Long orderId;

    @ApiModelProperty("")
    private Long skuId;

    @ApiModelProperty("")
    private Long brandId;

    @ApiModelProperty("")
    private Long customerId;

    /**
     * 预购数量
     */
    @ApiModelProperty("预购数量")
    private Long preordeNumber;

    /**
     * 采购数量
     */
    @ApiModelProperty("采购数量")
    private Integer purchaseNumber;

    /**
     * 单位id
     */
    @ApiModelProperty("单位id")
    private Long unitId;

    /**
     * 单价
     */
    @ApiModelProperty("单价")
    private Integer onePrice;

    /**
     * 总价
     */
    @ApiModelProperty("总价")
    private Integer totalPrice;

    /**
     * 票据类型
     */
    @ApiModelProperty("票据类型")
    private Integer paperType;

    /**
     * 锐率
     */
    @ApiModelProperty("锐率")
    private Long rate;

    /**
     * 交货日期
     */
    @ApiModelProperty("交货日期")
    private Integer deliveryDate;

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
     * 部门id
     */
    @ApiModelProperty("部门id")
    private Long deptId;

    /**
     * 到货数量
     */
    @ApiModelProperty("到货数量")
    private Integer arrivalNumber;

    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
}
