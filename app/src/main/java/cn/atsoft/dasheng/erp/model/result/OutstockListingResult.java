package cn.atsoft.dasheng.erp.model.result;

import cn.atsoft.dasheng.app.model.result.BrandResult;
import cn.atsoft.dasheng.app.model.result.ItemsResult;
import cn.atsoft.dasheng.erp.entity.Sku;
import lombok.Data;

import java.util.Date;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * <p>
 * 出库清单
 * </p>
 *
 * @author cheng
 * @since 2021-09-15
 */
@Data
@ApiModel
public class OutstockListingResult implements Serializable {

    private static final long serialVersionUID = 1L;
    private ItemsResult itemsResult;
    private BrandResult brandResult;
    private Long deliveryId;
    private Long pickListsId;
    private String coding;
    private SkuResult skuResult;
    private List<BackSku> backSkus;
    private SpuResult spuResult;
    private List<StorehousePositionsResult> positionsResults;
    private Long customerId;
    /**
     * 出库数量
     */
    private Long delivery;

    private Sku sku;
    /**
     * 出库清单id
     */
    @ApiModelProperty("出库清单id")
    private Long outstockListingId;
    /**
     * skuId
     */
    private Long skuId;

    /**
     * 出库时间
     */
    @ApiModelProperty("出库时间")
    private Date time;

    /**
     * 出库数量
     */
    @ApiModelProperty("出库数量")
    private Long number;

    /**
     * 出库价格
     */
    @ApiModelProperty("出库价格")
    private Integer price;

    /**
     * 品牌id
     */
    @ApiModelProperty("品牌id")
    private Long brandId;

    /**
     * 部门编号
     */
    @ApiModelProperty("部门编号")
    private Long deptId;

    /**
     * 创建时间
     */
    @ApiModelProperty(hidden = true)
    private Date createTime;

    /**
     * 创建者
     */
    @ApiModelProperty(hidden = true)
    private Long createUser;

    /**
     * 修改时间
     */
    @ApiModelProperty(hidden = true)
    private Date updateTime;

    /**
     * 修改者
     */
    @ApiModelProperty(hidden = true)
    private Long updateUser;

    /**
     * 状态
     */
    @ApiModelProperty("状态")
    private Integer display;

    /**
     * 产品id
     */
    @ApiModelProperty("产品id")
    private Long itemId;

    /**
     * 出库状态
     */
    @ApiModelProperty("出库状态")
    private Boolean state;

    /**
     * 出库单号
     */
    @ApiModelProperty("出库单号")
    private Long outstockOrderId;


    /**
     * 发货申请
     */
    @ApiModelProperty("发货申请")
    private Long outstockApplyId;
    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
}
