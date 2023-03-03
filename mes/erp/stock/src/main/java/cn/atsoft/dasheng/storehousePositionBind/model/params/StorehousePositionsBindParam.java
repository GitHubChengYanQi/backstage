package cn.atsoft.dasheng.storehousePositionBind.model.params;

import cn.atsoft.dasheng.model.validator.BaseValidatingParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 库位绑定物料表
 * </p>
 *
 * @author song
 * @since 2022-01-20
 */
@Data
@ApiModel
public class StorehousePositionsBindParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;
    private List<Long> skuIds;


    private Long SpuId;
    /**
     * id
     */
    @ApiModelProperty("id")
    private Long bindId;

    /**
     * 库位id
     */
    @NotNull
    @ApiModelProperty("库位id")
    private Long positionId;

    /**
     * skuId
     */
    @ApiModelProperty("skuId")
    private Long skuId;

    /**
     * 排序
     */
    @ApiModelProperty("排序")
    private Integer sort;

    /**
     * 供应商id
     */
    @ApiModelProperty("供应商id")
    private Long customerId;

    /**
     * 品牌id
     */
    @ApiModelProperty("品牌id")
    private Long brandId;

    /**
     * 部门id
     */
    @ApiModelProperty("部门id")
    private Long deptId;

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

    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;

    @Override
    public String checkParam() {
        return null;
    }

}
