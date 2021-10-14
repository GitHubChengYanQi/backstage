package cn.atsoft.dasheng.app.model.result;

import lombok.Data;

import java.util.Date;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * <p>
 * 发货表
 * </p>
 *
 * @author
 * @since 2021-08-20
 */
@Data
@ApiModel
public class DeliveryResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private ItemsResult itemsResult;
    private CustomerResult customerResult;

    private AdressResult adressResult;

    private ContactsResult contactsResult;

    private PhoneResult phoneResult;


    /**
     * 车牌号
     */
    private String licensePlate;
    /**
     * 司机电话
     */
    private Long driverPhone;
    /**
     * 司机姓名
     */
    private String driverName;
    /**
     * 物流单号
     */
    private String logisticsNumber;
    /**
     * 物流公司
     */
    private String logisticsCompany;
    /**
     * 发货方式
     */
    private Integer deliveryWay;

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
     * 发货id
     */
    @ApiModelProperty("发货id")
    private Long deliveryId;

    /**
     * 出库单id
     */
    @ApiModelProperty("出库单id")
    private Long outstockOrderId;

    /**
     * 发货时间
     */
    @ApiModelProperty("发货时间")
    private Date outTime;

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
    private Long deptId;
}
