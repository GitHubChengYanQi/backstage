package cn.atsoft.dasheng.erp.model.result;

import cn.atsoft.dasheng.erp.service.SpuClassificationService;
import cn.atsoft.dasheng.sys.modular.system.model.result.UserResult;
import lombok.Data;
import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
/**
 * <p>
 * 库存预警设置
 * </p>
 *
 * @author sjl
 * @since 2022-12-05
 */
@Data
@ApiModel
public class StockForewarnResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long skuId;

    private Long classificationId;

    private Long positionId;
    private List<Long> classIds;

    private SkuSimpleResult skuResult;
    private Integer number;

    private SpuClassificationResult spuClassificationResult;

    private StorehousePositionsResult storehousePositionsResult;

    private UserResult createUserResult;


    /**
     * 预警序号
     */
    @ApiModelProperty("预警序号")
    private Long forewarnId;

    /**
     * 类型
     */
    @ApiModelProperty("类型")
    private String type;

    /**
     * 预警内容
     */
    @ApiModelProperty("预警内容")
    private Long formId;

    /**
     * 库存下限
     */
    @ApiModelProperty("库存下限")
    private Integer inventoryFloor;

    /**
     * 库存上限
     */
    @ApiModelProperty("库存上限")
    private Integer inventoryCeiling;

    /**
     * 删除状态
     */
    @ApiModelProperty("删除状态")
    private Integer display;

    @ApiModelProperty(hidden = true)
    private Long createUser;

    @ApiModelProperty(hidden = true)
    private Long updateUser;

    @ApiModelProperty(hidden = true)
    private Date createTime;

    @ApiModelProperty(hidden = true)
    private Date updateTime;
    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
}
