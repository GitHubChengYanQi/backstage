package cn.atsoft.dasheng.erp.model.params;

import lombok.Data;
import cn.atsoft.dasheng.model.validator.BaseValidatingParam;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 产品订单
 * </p>
 *
 * @author song
 * @since 2021-10-20
 */
@Data
@ApiModel
public class ProductOrderParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;

    private List<ProductOrderDetailsParam> orderDetail;
    /**
     * 产品订单id
     */
    @ApiModelProperty("产品订单id")
    private Long productOrderId;

    /**
     * 数量
     */
    @ApiModelProperty("数量")
    private Long number;

    /**
     * 总金额
     */
    @ApiModelProperty("总金额")
    private Integer money;

    /**
     * 订购客户Id
     */
    @ApiModelProperty("订购客户Id")
    private Long customerId;

    /**
     * 创建时间
     */
    @ApiModelProperty(hidden = true)
    private Date createTime;

    /**
     * 创建者
     */
    @ApiModelProperty(hidden = true)
    private Long createUser;

    /**
     * 修改时间
     */
    @ApiModelProperty(hidden = true)
    private Date updateTime;

    /**
     * 修改者
     */
    @ApiModelProperty(hidden = true)
    private Long updateUser;

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

    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;

    @Override
    public String checkParam() {
        return null;
    }

}
