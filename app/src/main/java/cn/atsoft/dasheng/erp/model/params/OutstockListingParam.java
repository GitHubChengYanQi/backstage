package cn.atsoft.dasheng.erp.model.params;

import cn.atsoft.dasheng.base.dict.AbstractDictMap;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import cn.atsoft.dasheng.model.validator.BaseValidatingParam;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 出库清单
 * </p>
 *
 * @author cheng
 * @since 2021-09-15
 */
@Data
@ApiModel
public class OutstockListingParam extends AbstractDictMap implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;

    private Long customerId;
    @NotNull
    private Long positionsId;

    private Long deliveryId;

    private String inkindIds;
    private Long inkindId;
    /**
     * skuId
     */
    private Long skuId;

    /**
     * 出库数量
     */
    private Long delivery;

    /**
     * 出库清单id
     */
    @ApiModelProperty("出库清单id")
    private Long outstockListingId;

    /**
     * 出库时间
     */
    @ApiModelProperty("出库时间")
    private Date time;

    /**
     * 出库数量
     */
    @ApiModelProperty("出库数量")
    private Long number;

    /**
     * 出库价格
     */
    @ApiModelProperty("出库价格")
    private Integer price;

    /**
     * 品牌id
     */
    @ApiModelProperty("品牌id")
    private Long brandId;

    /**
     * 部门编号
     */
    @ApiModelProperty("部门编号")
    private Long deptId;

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

    /**
     * 状态
     */
    @ApiModelProperty("状态")
    private Integer display;

    /**
     * 产品id
     */
    @ApiModelProperty("产品id")
    private Long itemId;

    /**
     * 出库状态
     */
    @ApiModelProperty("出库状态")
    private Boolean state;

    /**
     * 出库单号
     */
    @ApiModelProperty("出库单号")
    private Long outstockOrderId;

    /**
     * 发货申请
     */
    @ApiModelProperty("发货申请")
    private Long outstockApplyId;

    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;


    @Override
    public String checkParam() {
        return null;
    }

    @Override
    public void init() {

    }

    @Override
    protected void initBeWrapped() {

    }
}
