package cn.atsoft.dasheng.purchase.model.params;

import lombok.Data;
import cn.atsoft.dasheng.model.validator.BaseValidatingParam;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.sql.Time;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 采购清单
 * </p>
 *
 * @author song
 * @since 2021-12-15
 */
@Data
@ApiModel
public class PurchaseListingParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;

    private Long brandId;

    private Time deliveryTime;

    private Integer type;

    private String askCoding;
    /**
     * 采购清单id
     */
    @ApiModelProperty("采购清单id")
    private Long purchaseListingId;

    private Integer status;
    /**
     * 采购申请id
     */
    @ApiModelProperty("采购申请id")
    private Long purchaseAskId;

    @ApiModelProperty("")
    private Long skuId;

    /**
     * 申请数量
     */
    @ApiModelProperty("申请数量")
    private Long applyNumber;

    /**
     * 可用数量
     */
    @ApiModelProperty("可用数量")
    private Long availableNumber;

    /**
     * 交付日期
     */
    @ApiModelProperty("交付日期")
    private Date deliveryDate;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String note;

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
     * 创建用户
     */
    @ApiModelProperty(hidden = true)
    private Long createUser;

    /**
     * 修改用户
     */
    @ApiModelProperty(hidden = true)
    private Long updateUser;

    /**
     * 状态
     */
    @ApiModelProperty("状态")
    private Long display;

    /**
     * 部门id
     */
    @ApiModelProperty("部门id")
    private Long deptId;

    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;

    @Override
    public String checkParam() {
        return null;
    }

}
