package cn.atsoft.dasheng.app.model.result;

import lombok.Data;

import java.util.Date;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * <p>
 * 订单表
 * </p>
 *
 * @author ta
 * @since 2021-07-26
 */
@Data
@ApiModel
public class ErpOrderResult implements Serializable {

    private static final long serialVersionUID = 1L;
    private OutstockResult outstockResult;
    private ContactsResult contactsResult;
    private ItemsResult itemsResult;
    private CustomerResult customerResult;
    private Long customerId;
    /**
     * 订单编号
     */
    @ApiModelProperty("订单编号")
    private Long orderId;

    /**
     * 合同id
     */
    @ApiModelProperty("合同id")
    private Long contractId;

    /**
     * 合同名称
     */
    @ApiModelProperty("合同名称")
    private String contractName;

    /**
     * 甲方id
     */
    @ApiModelProperty("甲方id")
    private Long partyA;

    /**
     * 乙方id
     */
    @ApiModelProperty("乙方id")
    private Long partyB;

    /**
     * 甲方联系人id
     */
    @ApiModelProperty("甲方联系人id")
    private Long partyAContactsId;

    /**
     * 乙方联系人id
     */
    @ApiModelProperty("乙方联系人id")
    private Long partyBContactsId;

    /**
     * 甲方联系地址
     */
    @ApiModelProperty("甲方联系地址")
    private Long partyAAdressId;

    /**
     * 乙方联系地址
     */
    @ApiModelProperty("乙方联系地址")
    private Long partyBAdressId;

    /**
     * 甲方联系人电话
     */
    @ApiModelProperty("甲方联系人电话")
    private Long partyAPhone;

    /**
     * 乙方联系人电话
     */
    @ApiModelProperty("乙方联系人电话")
    private Long partyBPhone;

    /**
     * 物品id
     */
    @ApiModelProperty("物品id")
    private Long itemId;

    /**
     * 订单状态
     */
    @ApiModelProperty("订单状态")
    private String state;

    /**
     * 订单数量
     */
    @ApiModelProperty("订单数量")
    private Long number;

    /**
     * 订单金额
     */
    @ApiModelProperty("订单金额")
    private Long price;

    /**
     * 下单时间
     */
    @ApiModelProperty("下单时间")
    private Date orderTime;

    /**
     * 部门编号
     */
    @ApiModelProperty("部门编号")
    private Long deptId;

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
