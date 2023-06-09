package cn.atsoft.dasheng.erp.model.result;

import cn.atsoft.dasheng.app.model.result.BrandResult;
import cn.atsoft.dasheng.app.model.result.CustomerResult;
import lombok.Data;
import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
/**
 * <p>
 * 库存操作记录主表
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-12-22
 */
@Data
@ApiModel
public class StockLogResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private BrandResult brandResult;
    private SkuSimpleResult skuResult;


    private CustomerResult customerResult;
    /**
     * 库存操作记录
     */
    @ApiModelProperty("库存操作记录")
    private Long stockLogId;

    /**
     * sku_id
     */
    @ApiModelProperty("sku_id")
    private Long skuId;

    /**
     * 数量
     */
    @ApiModelProperty("数量")
    private Integer number;

    /**
     * 操作之前的库存数量
     */
    @ApiModelProperty("操作之前的库存数量")
    private Integer beforeNumber;

    /**
     * 操作之后数量
     */
    @ApiModelProperty("操作之后数量")
    private Integer afterNumber;

    /**
     * 地点id
     */
    @ApiModelProperty("地点id")
    private Long storehouseId;

    /**
     * 仓库库位id
     */
    @ApiModelProperty("仓库库位id")
    private Long storehousePositionsId;

    @ApiModelProperty("")
    private Long customerId;

    /**
     * 品牌id
     */
    @ApiModelProperty("品牌id")
    private Long brandId;

    /**
     * 类型(increase增加/dwindle减少)
     */
    @ApiModelProperty("类型(increase增加/dwindle减少)")
    private String type;

    @ApiModelProperty("")
    private String source;

    @ApiModelProperty("")
    private Long sourceId;

    @ApiModelProperty(hidden = true)
    private Date createTime;

    @ApiModelProperty(hidden = true)
    private Long createUser;

    @ApiModelProperty(hidden = true)
    private Date updateTime;

    @ApiModelProperty(hidden = true)
    private Long updateUser;

    @ApiModelProperty("")
    private Integer display;

    /**
     * 部门编号
     */
    @ApiModelProperty("部门编号")
    private Long deptId;
    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
}
