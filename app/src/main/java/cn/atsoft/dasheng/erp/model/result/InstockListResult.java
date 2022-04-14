package cn.atsoft.dasheng.erp.model.result;

import cn.atsoft.dasheng.app.model.result.BrandResult;
import cn.atsoft.dasheng.app.model.result.ItemsResult;
import cn.atsoft.dasheng.app.model.result.StorehouseResult;
import cn.atsoft.dasheng.erp.entity.Sku;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.util.Date;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

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
public class InstockListResult implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 库位id
     */
    private Map<String,Object> stockDetails;
    private Long storehousePositionsId;
    private Long instockNumber;
    private List<BackSku> backSkus;
    private SpuResult spuResult;
    private ItemsResult itemsResult;
    private BrandResult brandResult;
    private StorehouseResult storehouseResult;
    private SkuResult skuResult;
    private Sku sku;

    private Long inkindId;


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
    private Long itemId;

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
