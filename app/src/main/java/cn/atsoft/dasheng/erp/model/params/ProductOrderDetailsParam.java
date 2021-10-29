package cn.atsoft.dasheng.erp.model.params;

import cn.atsoft.dasheng.app.model.params.Spu;
import cn.atsoft.dasheng.app.model.params.SpuAttribute;
import lombok.Data;
import cn.atsoft.dasheng.model.validator.BaseValidatingParam;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 产品订单详情
 * </p>
 *
 * @author song
 * @since 2021-10-20
 */
@Data
@ApiModel
public class ProductOrderDetailsParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;


    /**
     * 产品订单详情id
     */
    @ApiModelProperty("产品订单详情id")
    private Long productOrderDetailsId;

    /**
     * 产品订单id
     */
    @ApiModelProperty("产品订单id")
    private Long productOrderId;

    /**
     * spuId
     */
    @ApiModelProperty("spuId")
    private Long spuId;

    /**
     * skuId
     */
    @ApiModelProperty("sku")
    private List<Spu> sku;

    /**
     * 数量
     */
    @ApiModelProperty("数量")
    private Long number;

    /**
     * 金额
     */
    @ApiModelProperty("金额")
    private Integer money;

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
