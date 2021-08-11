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
    private int price;
    private String contactsName;
    private String location;
    private String name;
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
     * 地址id
     */
    @ApiModelProperty("地址id")
    private String adressId;

    /**
     * 出库id
     */
    @ApiModelProperty("出库id")
    private Long outstockId;

    /**
     * 联系id
     */
    @ApiModelProperty("联系id")
    private Long contactsId;

    /**
     * 联系人电话
     */
    @ApiModelProperty("联系人电话")
    private String phone;

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
