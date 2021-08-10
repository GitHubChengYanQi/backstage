package cn.atsoft.dasheng.app.model.result;

import lombok.Data;
import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
/**
 * <p>
 * 仓库总表
 * </p>
 *
 * @author 
 * @since 2021-07-15
 */
@Data
@ApiModel
public class StockResult implements Serializable {

    private static final long serialVersionUID = 1L;
    private ItemsResult itemsResult;
    private StorehouseResult storehouseResult;
    private  BrandResult brandResult;
    /**
     * 仓库id
     */
    @ApiModelProperty("仓库id")
    private Long stockId;

    /**
     * 地点id
     */
    @ApiModelProperty("地点id")
    private Long storehouseId;

    /**
     * 物品id
     */
    @ApiModelProperty("物品id")
    private Long itemId;

    /**
     * 品牌id
     */
    @ApiModelProperty("品牌id")
    private Long brandId;

    /**
     * 数量
     */
    @ApiModelProperty("数量")
    private Long inventory;

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

    private String  iname;
    private  String pname;
    private  String bname;
    /**
     * 状态
     */
    @ApiModelProperty("状态")
    private Integer display;
    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
}
