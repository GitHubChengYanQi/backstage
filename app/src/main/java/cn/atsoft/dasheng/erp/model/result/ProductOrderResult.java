package cn.atsoft.dasheng.erp.model.result;

import cn.atsoft.dasheng.app.model.result.AdressResult;
import cn.atsoft.dasheng.app.model.result.ContactsResult;
import cn.atsoft.dasheng.app.model.result.CustomerResult;
import cn.atsoft.dasheng.app.model.result.PhoneResult;
import cn.atsoft.dasheng.erp.entity.ProductOrderDetails;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import lombok.Data;

import java.util.Date;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * <p>
 * 产品订单
 * </p>
 *
 * @author song
 * @since 2021-10-20
 */
@Data
@ApiModel
public class ProductOrderResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private AdressResult adressResult;

    private PhoneResult phoneResult;

    private User user;

    private ContactsResult contactsResult;

    private Long contactsId;

    private Long adressId;

    private Long phoneId;

    /**
     * 客户信息
     */
    private String customer;
    /**
     * 状态
     */
    private Integer state;

    private List<ProductOrderDetails> orderDetail;

    private CustomerResult customerResult;
    /**
     * 产品订单id
     */
    @ApiModelProperty("产品订单id")
    private Long productOrderId;

    /**
     * 数量
     */
    @ApiModelProperty("数量")
    private Long number;

    /**
     * 总金额
     */
    @ApiModelProperty("总金额")
    private Integer money;

    /**
     * 订购客户Id
     */
    @ApiModelProperty("订购客户Id")
    private Long customerId;

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
     * 部门编号
     */
    @ApiModelProperty("部门编号")
    private Long deptId;
    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
}
