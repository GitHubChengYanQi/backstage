package cn.atsoft.dasheng.app.model.params;

import com.baomidou.mybatisplus.annotation.TableField;
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
    private String inkind;
    private Integer stage;
    private String iname;
    private String pname;
    @ApiModelProperty("库位id")
    private Long storehouseId;
    List<Long> brandIds;
    private Long outStockOrderId;
    private Long number;
    @ApiModelProperty("二维码id")
    private Long qrCodeid;
    @ApiModelProperty("实物id")
    private Long inkindId;
    @ApiModelProperty("物料名称")
    private String skuName;

    //    库位集合
    @ApiModelProperty("库位集合")
    private List<Long> positionIds;
    //不需要养护的实物id
    @ApiModelProperty("不需要养护的实物id")
    private List<Long> notNeedMaintenanceInkindIds;
    //物料ids
    @ApiModelProperty("物料ids")
    private List<Long> skuIds;

    private Long customerId;
    /**
     * 库位id
     */
    private Long storehousePositionsId;
    /**
     * skuId
     */
    private Long skuId;
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
    @ApiModelProperty("产品id")
    private Long itemId;

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
    private Long deptId;

    @Override
    public String checkParam() {
        return null;
    }

}
