package cn.atsoft.dasheng.storehousePositionBind.model.result;

import cn.atsoft.dasheng.goods.sku.model.result.RestSkuResult;
import cn.atsoft.dasheng.goods.sku.model.result.SkuListResult;
import cn.atsoft.dasheng.storehousePosition.model.result.RestStorehousePositionsResult;
import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

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
public class StorehousePositionsBindResult implements Serializable {

    private SkuListResult skuResult;
    private RestStorehousePositionsResult storehousePositionsResult;

    private static final long serialVersionUID = 1L;


    /**
     * id
     */
    @ApiModelProperty("id")
    private Long bindId;

    /**
     * 库位id
     */
    @ApiModelProperty("库位id")
    private Long positionId;

    /**
     * skuId
     */
    @ApiModelProperty("skuId")
    @JSONField(serialize = false)
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
    @JSONField(serialize = false)
    private Long customerId;

    /**
     * 品牌id
     */
    @ApiModelProperty("品牌id")
    @JSONField(serialize = false)
    private Long brandId;

    /**
     * 部门id
     */
    @ApiModelProperty("部门id")
    @JSONField(serialize = false)
    private Long deptId;

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
    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
}
