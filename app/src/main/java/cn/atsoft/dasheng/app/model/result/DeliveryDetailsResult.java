package cn.atsoft.dasheng.app.model.result;

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
 * @author
 * @since 2021-08-20
 */
@Data
@ApiModel
public class DeliveryDetailsResult implements Serializable {

    private static final long serialVersionUID = 1L;
    private DeliveryResult deliveryResult;
    private Integer stage;
    private  ItemsResult itemsResult;
    private  CustomerResult customerResult;

    /**
     * 发货详情id
     */
    @ApiModelProperty("发货详情id")
    private Long deliveryDetailsId;

    /**
     * 产品id
     */
    @ApiModelProperty("产品id")
    private Long itemId;

    /**
     * 客户id
     */
    @ApiModelProperty("客户id")
    private Long customerId;

    /**
     * 地址id
     */
    @ApiModelProperty("地址id")
    private Long adressId;

    /**
     * 联系人id
     */
    @ApiModelProperty("联系人id")
    private Long contactsId;

    /**
     * 电话id
     */
    @ApiModelProperty("电话id")
    private Long phoneId;

    /**
     * 出库明细id
     */
    @ApiModelProperty("出库明细id")
    private Long stockItemId;

    /**
     * 出库单
     */
    @ApiModelProperty("出库单")
    private Long deliveryId;

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
    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
}
