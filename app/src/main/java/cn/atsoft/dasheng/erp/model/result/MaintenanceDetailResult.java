package cn.atsoft.dasheng.erp.model.result;

import cn.atsoft.dasheng.app.model.result.BrandResult;
import lombok.Data;
import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
/**
 * <p>
 * 养护申请子表
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-06-28
 */
@Data
@ApiModel
public class MaintenanceDetailResult implements Serializable {

    private static final long serialVersionUID = 1L;
    private SkuSimpleResult skuResult;
    private StorehousePositionsResult storehousePositionsResult;
    private BrandResult brandResult;

    @ApiModelProperty("")
    private Long maintenanceDetailId;

    @ApiModelProperty("")
    private Long maintenanceId;

    @ApiModelProperty("")
    private Long inkindId;

    @ApiModelProperty("")
    private Long skuId;

    @ApiModelProperty("")
    private Integer number;
    private Integer doneNumber;

    private Long storehousePositionsId;

    /**
     * 品牌id
     */
    @ApiModelProperty("品牌id")
    private Long brandId;

    /**
     * 创建者
     */
    @ApiModelProperty(hidden = true)
    private Long createUser;

    /**
     * 创建时间
     */
    @ApiModelProperty(hidden = true)
    private Date createTime;

    /**
     * 修改者
     */
    @ApiModelProperty(hidden = true)
    private Long updateUser;

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
     * 状态
     */
    @ApiModelProperty("状态")
    private Integer status;
    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
}
