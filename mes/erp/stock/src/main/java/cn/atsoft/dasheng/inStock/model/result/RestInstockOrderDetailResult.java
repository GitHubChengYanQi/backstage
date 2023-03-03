package cn.atsoft.dasheng.inStock.model.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 入库清单
 * </p>
 *
 * @author song
 * @since 2021-10-06
 */
@Data
@ApiModel
public class RestInstockOrderDetailResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long anomalyId;
    /**
     * 库位id
     */
    private Map<String, Object> stockDetails;
    private Long storehousePositionsId;
    private Long instockNumber;
//    private CustomerResult customerResult;
//    private BrandResult brandResult;
//    private StorehouseResult storehouseResult;
//    private SkuResult skuResult;
//    private Sku sku;
    private Integer positionNum;
    private Long inkindId;
    private String anomalyHandle;
    private Long notNumber;

    /**
     * 供应商
     */
    private Long customerId;
    /**
     * 实际数量
     */
    private Long realNumber;
    /**
     * skuId
     */
    private Long skuId;

    private Integer costPrice;

    private Integer sellingPrice;
    /**
     * 入库清单
     */
    @ApiModelProperty("入库清单")
    private Long instockListId;


    private Long storeHouseId;

    /**
     * 品牌id
     */
    @ApiModelProperty("品牌id")
    private Long brandId;

    /**
     * 产品id
     */
    @ApiModelProperty("产品id")
    private Long status;

    /**
     * 数量
     */
    @ApiModelProperty("数量")
    private Long number;

    /**
     * 到货时间
     */
    @ApiModelProperty("到货时间")
    private Date receivedDate;

    /**
     * 有效日期
     */
    @ApiModelProperty("有效日期")
    private Date effectiveDate;

    /**
     * 生产日期
     */
    @ApiModelProperty("生产日期")
    private Date manufactureDate;

    /**
     * 批号
     */
    @ApiModelProperty("批号")
    private String lotNumber;

    /**
     * 流水号
     */
    @ApiModelProperty("流水号")
    private String serialNumber;


    /**
     * 入库单id
     */
    @ApiModelProperty("入库单id")
    private Long instockOrderId;

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
