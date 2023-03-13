package cn.atsoft.dasheng.crm.model.result;

import lombok.Data;
import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
/**
 * <p>
 * 付款记录
 * </p>
 *
 * @author song
 * @since 2022-03-01
 */
@Data
@ApiModel
public class PaymentRecordResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private OrderResult orderResult;

    /**
     * 付款记录id
     */
    @ApiModelProperty("付款记录id")
    private Long recordId;

    /**
     * 付款详情id
     */
    @ApiModelProperty("付款详情id")
    private Long detailId;

    /**
     * 付款主表id
     */
    @ApiModelProperty("付款主表id")
    private Long paymentId;

    /**
     * 付款金额
     */
    @ApiModelProperty("付款金额")
    private Integer paymentAmount;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remark;

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

    /**
     * 部门编号
     */
    @ApiModelProperty("部门编号")
    private Long deptId;

    /**
     * 订单id
     */
    @ApiModelProperty("订单id")
    private Long orderId;

    /**
     * 状态
     */
    @ApiModelProperty("状态")
    private Long status;

    /**
     * 付款日期
     */
    @ApiModelProperty("付款日期")
    private Date paymentDate;

    /**
     * 编码
     */
    @ApiModelProperty("编码")
    private String Coding;

    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
}
