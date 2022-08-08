package cn.atsoft.dasheng.erp.model.result;

import cn.atsoft.dasheng.app.model.result.BrandResult;
import cn.atsoft.dasheng.app.model.result.StorehouseResult;
import lombok.Data;
import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
/**
 * <p>
 * 调拨子表
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-07-25
 */
@Data
@ApiModel
public class AllocationCartResult implements Serializable {
    private SkuSimpleResult skuResult;
    private BrandResult brandResult;
    private StorehousePositionsResult positionsResult;
    private StorehousePositionsResult toPositionsResult;
    private StorehouseResult storehouseResult;
    private StorehouseResult toStorehouseResult;
    private static final long serialVersionUID = 1L;
    private Integer doneNumber;

    @ApiModelProperty("")
    private Long allocationCartId;

    @ApiModelProperty("")
    private Long allocationDetailId;
    @ApiModelProperty("")
    private String type;

    /**
     * 调拨id
     */
    @ApiModelProperty("调拨id")
    private Long allocationId;

    @ApiModelProperty("")
    private Long skuId;

    @ApiModelProperty("")
    private Integer number;

    /**
     * 仓库库位id
     */
    @ApiModelProperty("仓库库位id")
    private Long storehousePositionsId;

    /**
     * 仓库id
     */
    @ApiModelProperty("仓库id")
    private Long storehouseId;

    @ApiModelProperty("")
    private Long brandId;

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
     * 调拨目标位置仓库库位id
     */
    @ApiModelProperty("调拨目标位置仓库库位id")
    private Long toStorehousePositionsId;

    /**
     * 调拨目标位置仓库id
     */
    @ApiModelProperty("调拨目标位置仓库id")
    private Long toStorehouseId;

    @ApiModelProperty("")
    private Integer status;
    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
}
