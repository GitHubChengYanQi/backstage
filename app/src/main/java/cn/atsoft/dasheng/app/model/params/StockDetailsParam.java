package cn.atsoft.dasheng.app.model.params;

import lombok.Data;
import cn.atsoft.dasheng.model.validator.BaseValidatingParam;

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
public class StockDetailsParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;

    private String iname;
    private String pname;

    private Long storehouseId;
    /**
     * 明细id
     */
    @ApiModelProperty("明细id")
    private Long stockItemId;

    /**
     * 仓库id
     */
    @ApiModelProperty("仓库id")
    private  Long stockId;

    /**
     * 产品id
     */
    @ApiModelProperty("产品id")
    private  Long itemsId;

    /**
     * 价格
     */
    @ApiModelProperty("价格")
    private Integer price;

    /**
     * 入库时间
     */
    @ApiModelProperty("入库时间")
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
    private Long barcode;

    /**
     * 创建者
     */
    @ApiModelProperty(hidden = true)
    private Long createUser;

    /**
     * 创建时间
     */
    @ApiModelProperty(hidden = true)
    private Date createTime;

    /**
     * 修改者
     */
    @ApiModelProperty(hidden = true)
    private Long updateUser;

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

    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;

    @Override
    public String checkParam() {
        return null;
    }

}
