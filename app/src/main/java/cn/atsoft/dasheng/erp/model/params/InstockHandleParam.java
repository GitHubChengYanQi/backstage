package cn.atsoft.dasheng.erp.model.params;

import lombok.Data;
import cn.atsoft.dasheng.model.validator.BaseValidatingParam;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 入库操作结果
 * </p>
 *
 * @author song
 * @since 2022-07-08
 */
@Data
@ApiModel
public class InstockHandleParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;

    private Long instockListId;
    /**
     * 入库处理
     */
    @ApiModelProperty("入库处理")
    private Long instockHandleId;

    private String type;

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
     * 供应商id
     */
    @ApiModelProperty("供应商id")
    private Long customerId;

    /**
     * 入库单id
     */
    @ApiModelProperty("入库单id")
    private Long instockOrderId;

    /**
     * 地点id
     */
    @ApiModelProperty("地点id")
    private Long storehouseId;

    /**
     * 仓库库位id
     */
    @ApiModelProperty("仓库库位id")
    private Long storehousePositionsId;

    /**
     * 数量
     */
    @ApiModelProperty("数量")
    private Long number;

    /**
     * 实物id
     */
    @ApiModelProperty("实物id")
    private Long inkindId;

    /**
     * 状态
     */
    @ApiModelProperty("状态")
    private Long status;

    /**
     * 入库数量
     */
    @ApiModelProperty("入库数量")
    private Long instockNumber;

    @ApiModelProperty("")
    private Long realNumber;

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

    @Override
    public String checkParam() {
        return null;
    }

}
