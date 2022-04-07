package cn.atsoft.dasheng.erp.model.result;

import cn.atsoft.dasheng.app.entity.StockDetails;
import cn.atsoft.dasheng.app.model.result.StockDetailsResult;
import cn.atsoft.dasheng.app.model.result.StorehouseResult;
import cn.atsoft.dasheng.printTemplate.model.result.PrintTemplateResult;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import lombok.Data;

import java.util.Date;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * <p>
 * 仓库库位表
 * </p>
 *
 * @author song
 * @since 2021-10-29
 */
@Data
@ApiModel
public class StorehousePositionsResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private StorehouseResult storehouseResult;
    @JSONField(serialize = false)
    private StorehousePositionsResult storehousePositionsResult;

    private StorehousePositionsResult supper;
    @JSONField(serialize = false)
    private SkuResult skuResult;
    @JSONField(serialize = false)
    private Integer skuNumber;


    private PrintTemplateResult printTemplateResult;

    private List<StockDetailsResult> detailsResults;
    @JSONField(serialize = false)
    private List<SkuSimpleResult> skuResults;

    private List<String> skuIds;
    private List<StorehousePositionsResult> storehousePositionsResults;


    /**
     * 仓库库位id
     */
    @ApiModelProperty("仓库库位id")
    @JSONField(serializeUsing= ToStringSerializer.class)
    private Long storehousePositionsId;

    /**
     * 仓库id
     */
    @ApiModelProperty("仓库id")
    @JSONField(serializeUsing= ToStringSerializer.class)
    private Long storehouseId;

    /**
     * skuId
     */
    @JSONField(serialize = false,serializeUsing= ToStringSerializer.class)
    @ApiModelProperty("skuId")
    private Long skuId;

    /**
     * 库位名称
     */
    @ApiModelProperty("库位名称")
    private String name;

    private String note;

    /**
     * 数量
     */
    @ApiModelProperty("数量")
    @JSONField(serialize = false,serializeUsing= ToStringSerializer.class)
    private Long number;

    /**
     * 创建者
     */
    @JSONField(serialize = false)
    @ApiModelProperty(hidden = true)
    private Long createUser;

    /**
     * 修改者
     */
    @JSONField(serialize = false)
    @ApiModelProperty(hidden = true)
    private Long updateUser;

    /**
     * 创建时间
     */
    @JSONField(serialize = false)
    @ApiModelProperty(hidden = true)
    private Date createTime;

    /**
     * 修改时间
     */
    @JSONField(serialize = false)
    @ApiModelProperty(hidden = true)
    private Date updateTime;

    /**
     * 状态
     */
    @JSONField(serialize = false)
    @ApiModelProperty("状态")
    private Integer display;

    /**
     * 部门id
     */
    @JSONField(serialize = false)
    @ApiModelProperty("部门id")
    private Long deptId;

    /**
     * 上级
     */

    @ApiModelProperty("上级")
    private Long pid;

    /**
     * 排序
     */
    @ApiModelProperty("排序")
    private Integer sort;

    @JSONField(serialize = false)
    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
}
