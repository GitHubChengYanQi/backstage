package cn.atsoft.dasheng.erp.model.params;

import cn.atsoft.dasheng.erp.model.result.OutstockApplyResult;
import lombok.Data;
import cn.atsoft.dasheng.model.validator.BaseValidatingParam;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 出库申请
 * </p>
 *
 * @author song
 * @since 2021-09-14
 */
@Data
@ApiModel
public class OutstockApplyParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;

    private List<ApplyDetailsParam> applyDetails;

    /**
     * 车牌号
     */
    private String licensePlate;
    /**
     * 司机电话
     */
    private Integer driverPhone;
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
    //------------------------------------------------------------------------------------------------------------
    /**
     * 出库申请
     */
    @ApiModelProperty("出库申请")
    private Long outstockApplyId;
    private  Long stockId;

    @ApiModelProperty("收货地址id")
    private Long adressId;

    @ApiModelProperty("联系人id")
    private Long contactsId;

    @ApiModelProperty("电话id")
    private Long phoneId;

    @ApiModelProperty("预计到达时间")
    private Date time;

    /**
     * 负责人
     */
    @ApiModelProperty("负责人")
    private Long userId;

    /**
     * 客户id
     */
    @ApiModelProperty("客户id")
    private Long customerId;
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
     * 申请状态
     */
    @ApiModelProperty("申请状态")
    private Integer applyState;


    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
    /**
     * 部门id
     */
    @ApiModelProperty("部门Id")
    private Long deptId;

    @Override
    public String checkParam() {
        return null;
    }

}
