package cn.atsoft.dasheng.erp.model.result;

import cn.atsoft.dasheng.app.entity.Customer;
import cn.atsoft.dasheng.app.model.result.BrandResult;
import cn.atsoft.dasheng.erp.entity.StorehousePositions;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import lombok.Data;

import java.util.Date;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-04-14
 */
@Data
@ApiModel
public class InstockLogDetailResult implements Serializable {

    private static final long serialVersionUID = 1L;
    private SkuSimpleResult skuResult;
    private Integer listNumber;
    private Customer customer;
    private User user;

    @ApiModelProperty("")
    private Long instockLogDetailId;

    private BrandResult brandResult;

    private List<StorehousePositionsResult> positionsResults;

    private StorehousePositionsResult storehousePositionsResult;

    @ApiModelProperty("")
    private Long instockLogId;

    private Long customerId;

    private String type;
    /**
     * 实物id
     */
    @ApiModelProperty("实物id")
    private Long inkindId;

    /**
     * sku_id
     */
    @ApiModelProperty("sku_id")
    private Long skuId;

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
     * 入库数量
     */
    @ApiModelProperty("入库数量")
    private Long instockNumber;

    /**
     * 入库单id
     */
    @ApiModelProperty("入库单id")
    private Long instockOrderId;

    /**
     * 批号
     */
    @ApiModelProperty("批号")
    private String lotNumber;

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
     * 序列号
     */
    @ApiModelProperty("序列号")
    private String serialNumber;

    /**
     * 到货日期
     */
    @ApiModelProperty("到货日期")
    private Date receivedDate;

    /**
     * 地点id
     */
    @ApiModelProperty("地点id")
    private Long storehouseId;

    /**
     * 出售价格
     */
    @ApiModelProperty("出售价格")
    private Integer sellingPrice;

    /**
     * 成本价格
     */
    @ApiModelProperty("成本价格")
    private Integer costPrice;

    /**
     * 仓库库位id
     */
    @ApiModelProperty("仓库库位id")
    private Long storehousePositionsId;

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
