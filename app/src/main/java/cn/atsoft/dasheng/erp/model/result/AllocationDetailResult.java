package cn.atsoft.dasheng.erp.model.result;

import cn.atsoft.dasheng.app.model.result.BrandResult;
import cn.atsoft.dasheng.app.model.result.StorehouseResult;
import com.alibaba.fastjson.annotation.JSONField;
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
 * @since 2022-07-13
 */
@Data
@ApiModel
public class AllocationDetailResult implements Serializable {

    private static final long serialVersionUID = 1L;
    private SkuSimpleResult skuResult;
    private BrandResult brandResult;
    private StorehousePositionsResult positionsResult;
    private StorehousePositionsResult toPositionsResult;
    private StorehouseResult storehouseResult;
    private StorehouseResult toStorehouseResult;

    @ApiModelProperty("")
    private Long allocationDetailId;

    private String params;

    private Integer haveBrand;

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
    /**
     * 仓库id
     */
    @ApiModelProperty("状态")
    private Integer status;

    @ApiModelProperty("")
    private Long brandId;

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
     * 部门id
     */
    @ApiModelProperty("部门id")
    @JSONField(serialize = false)
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
    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
}
