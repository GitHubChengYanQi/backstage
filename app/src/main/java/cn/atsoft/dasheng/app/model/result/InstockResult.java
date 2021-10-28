package cn.atsoft.dasheng.app.model.result;

import lombok.Data;

import java.util.Date;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * <p>
 * 入库表
 * </p>
 *
 * @author song
 * @since 2021-07-17
 */
@Data
@ApiModel
public class InstockResult implements Serializable {

    private static final long serialVersionUID = 1L;
    private BrandResult brandResult;
    private ItemsResult itemsResult;
    private StorehouseResult storehouseResult;


    /**
     * skuId
     */
    private Long skuId;
    /**
     * 出库单
     */
    private Long instockOrderId;

    /**
     * 物品编号
     */
    @ApiModelProperty("物品编号")
    private Long instockId;

    @ApiModelProperty("地点id")
    private Long storeHouseId;

    /**
     * 物品名称
     */
    @ApiModelProperty("物品名称")
    private Long itemId;

    /**
     * 登记时间
     */
    @ApiModelProperty("登记时间")
    private Date registerTime;

    /**
     * 入库数量
     */
    @ApiModelProperty("入库数量")
    private Long number;

    /**
     * 价格
     */
    @ApiModelProperty("成本价格")
    private Integer sellingPrice;

    @ApiModelProperty("出售价格")
    private Integer costPrice;
    /**
     * 品牌
     */
    @ApiModelProperty("品牌")
    private long brandId;

    /**
     * 入库状态
     */
    @ApiModelProperty("入库状态")
    private Integer state;

    /**
     * 条形码
     */
    @ApiModelProperty("条形码")
    private Integer barcode;

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
    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
    private Long deptId;
}
