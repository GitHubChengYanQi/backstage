package cn.atsoft.dasheng.erp.model.result;

import cn.atsoft.dasheng.app.model.result.BrandResult;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import lombok.Data;

import java.util.Date;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * <p>
 * 库存盘点处理
 * </p>
 *
 * @author song
 * @since 2022-07-15
 */
@Data
@ApiModel
public class InventoryStockResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer errorNum;

    private Long inkindId;

    private SkuResult skuResult;

    private BrandResult brandResult;

    private StorehousePositionsResult positionsResult;

    private User User;

    @ApiModelProperty("")
    private Long inventoryStockId;

    /**
     * 盘点主表id
     */
    @ApiModelProperty("盘点主表id")
    private Long inventoryId;

    /**
     * 盘点任务详情id
     */
    @ApiModelProperty("盘点任务详情id")
    private Long detailId;

    @ApiModelProperty("")
    private Long skuId;

    @ApiModelProperty("")
    private Long brandId;

    @ApiModelProperty("")
    private Long customerId;

    /**
     * 库位id
     */
    @ApiModelProperty("库位id")
    private Long positionId;

    @ApiModelProperty("")
    private Long anomalyId;

    /**
     * 数量
     */
    @ApiModelProperty("数量")
    private Long number;

    /**
     * 锁
     */
    @ApiModelProperty("锁")
    private Integer lockStatus;

    /**
     * 实际数量
     */
    @ApiModelProperty("实际数量")
    private Long realNumber;

    /**
     * 默认 0
     */
    @ApiModelProperty("默认 0")
    private Integer status;

    @ApiModelProperty(hidden = true)
    private Long createUser;

    @ApiModelProperty(hidden = true)
    private Long updateUser;

    @ApiModelProperty(hidden = true)
    private Date createTime;

    @ApiModelProperty(hidden = true)
    private Date updateTime;

    /**
     * 部门id
     */
    @ApiModelProperty("部门id")
    private Long deptId;

    @ApiModelProperty("")
    private Integer display;
    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
}
