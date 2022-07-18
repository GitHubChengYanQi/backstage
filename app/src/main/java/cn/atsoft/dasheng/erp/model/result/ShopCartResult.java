package cn.atsoft.dasheng.erp.model.result;

import cn.atsoft.dasheng.app.entity.Customer;
import cn.atsoft.dasheng.app.model.result.BrandResult;
import cn.atsoft.dasheng.erp.entity.StorehousePositions;
import cn.atsoft.dasheng.erp.pojo.PositionNum;
import lombok.Data;

import java.util.Date;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * <p>
 * 购物车
 * </p>
 *
 * @author song
 * @since 2022-06-06
 */
@Data
@ApiModel
public class ShopCartResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private SkuResult skuResult;

    private BrandResult brandResult;

    private String storehousePositionsId;

    private Long storehouseId;

    private List<StorehousePositionsResult> storehousePositions;

    private Customer customer;

    private Long formId;

    private Long errorNumber;

    private List<PositionNum> positionNums;


    private Long otherNumber;

    private AnomalyResult anomalyResult;

    @ApiModelProperty("")
    private Long cartId;

    /**
     * 类型
     */
    @ApiModelProperty("类型")
    private String type;

    @ApiModelProperty("")
    private Long skuId;

    @ApiModelProperty("")
    private Long brandId;

    @ApiModelProperty("")
    private Long customerId;

    /**
     * 状态
     */
    @ApiModelProperty("状态")
    private Integer status;

    /**
     * 数量
     */
    @ApiModelProperty("数量")
    private Long number;

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
    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
}
