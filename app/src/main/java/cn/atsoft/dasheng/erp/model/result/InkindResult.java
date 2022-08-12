package cn.atsoft.dasheng.erp.model.result;

import cn.atsoft.dasheng.app.entity.Brand;
import cn.atsoft.dasheng.app.model.result.BrandResult;
import cn.atsoft.dasheng.erp.entity.QualityTaskDetail;
import cn.atsoft.dasheng.printTemplate.model.result.PrintTemplateResult;

import cn.atsoft.dasheng.sys.modular.system.entity.User;
import lombok.Data;

import java.util.Date;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 实物表
 * </p>
 *
 * @author song
 * @since 2021-11-01
 */
@Data
@ApiModel
public class InkindResult implements Serializable {

    private User user;

    private Long qrcode;

    private MaintenanceLogDetailResult maintenanceLogResult;

    private static final long serialVersionUID = 1L;

    private SkuSimpleResult skuSimpleResult;

    private StorehousePositionsResult positionsResult;

    private Integer anomaly;

    private Long positionId;

    private Long pid;

    private String batchNumber;

    private Integer serialNumber;

    private Date productionTime;

    private String source;

    private Brand brand;

    private BrandResult brandResult;

    private Long sourceId;

    private Long outstockOrderId;

    private QualityTaskDetail taskDetail;

    private List<BackSku> backSku;

    private SkuResult skuResult;

    private Long number;

    private Map<String, Object> inkindDetail;

    private PrintTemplateResult printTemplateResult;

    private Boolean inNotStock;

    private Long customerId;
    /**
     * 品牌
     */
    private Long brandId;
    /**
     * 库位
     */
    private Long storehousePositionsId;
    /**
     * 入库单
     */
    private Long instockOrderId;
    /**
     * 成本价格
     */
    private Integer costPrice;
    /**
     * 出售价格
     */
    private Integer sellingPrice;

    private Long spuId;
    /**
     * 实物id
     */
    @ApiModelProperty("实物id")
    private Long inkindId;

    /**
     * 类型
     */
    @ApiModelProperty("类型")
    private String type;

    /**
     * skuId
     */
    @ApiModelProperty("skuId")
    private Long skuId;

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

    /**
     * 部门id
     */
    @ApiModelProperty("部门id")
    private Long deptId;
    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
}
