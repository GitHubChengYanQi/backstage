package cn.atsoft.dasheng.app.model.result;

import cn.atsoft.dasheng.erp.entity.Sku;
import cn.atsoft.dasheng.erp.entity.StorehousePositions;
import cn.atsoft.dasheng.erp.model.result.*;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.Date;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * <p>
 * 仓库物品明细表
 * </p>
 *
 * @author
 * @since 2021-07-15
 */
@Data
@ApiModel
public class StockDetailsResult implements Serializable {

    private MaintenanceLogDetailResult maintenanceLogDetailResult;
    private StorehousePositionsResult storehousePositionsResult;
    private static final long serialVersionUID = 1L;
    private Long storehouseId;
    private StorehouseResult storehouseResult;
    private BrandResult brandResult;
    private List<BackSku> backSkus;

    private SkuSimpleResult skuResult;
    private Long number;
    private Long qrCodeId;
    private List<StockDetailRequest> stockDetailRequests;
    private Long inkindId;
    private InkindResult inkindResult;
    private CustomerResult customerResult;
    private StorehousePositionsResult positionsResult;
    private User user;
    private String spuClassName;
    private String month;
    private Long num;
    private Long spuClassificationId;
    private Integer anomaly;
    /**
     * 库位id
     */
    private Long storehousePositionsId;

    /**
     * skuId
     */
    private Long skuId;

    private Long customerId;
    /**
     * 明细id
     */
    @ApiModelProperty("明细id")
    private Long stockItemId;

    /**
     * 仓库id
     */
    @ApiModelProperty("仓库id")
    private Long stockId;

    /**
     * 产品id
     */
    @JSONField(serialize = false)
    @ApiModelProperty("产品id")
    private Long itemId;

    /**
     * 价格
     */
    @ApiModelProperty("价格")
    @JSONField(serialize = false)
    private Integer price;

    /**
     * 入库时间
     */
    @ApiModelProperty("入库时间")
    @JSONField(serialize = false)
    private Date storageTime;

    /**
     * 品牌id
     */
    @ApiModelProperty("品牌id")
    private Long brandId;

    /**
     * 条形码
     */
    @ApiModelProperty("条形码")
    @JSONField(serialize = false)
    private Long barcode;

    /**
     * 创建者
     */
    @ApiModelProperty(hidden = true)
    @JSONField(serialize = false)
    private Long createUser;

    /**
     * 创建时间
     */
    @ApiModelProperty(hidden = true)
    @JSONField(serialize = false)
    private Date createTime;

    /**
     * 修改者
     */
    @ApiModelProperty(hidden = true)
    @JSONField(serialize = false)
    private Long updateUser;

    /**
     * 修改时间
     */
    @JSONField(serialize = false)
    @ApiModelProperty(hidden = true)
    private Date updateTime;

    /**
     * 状态
     */
    @ApiModelProperty("状态")
    @JSONField(serialize = false)
    private Integer display;
    @ApiModelProperty("父ID顺序数组")
    @JSONField(serialize = false)
    private List<String> pidValue;
    @JSONField(serialize = false)
    private Long deptId;
}
