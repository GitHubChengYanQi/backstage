package cn.atsoft.dasheng.app.model.result;

import lombok.Data;
import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
/**
 * <p>
 * 机床合同表
 * </p>
 *
 * @author 
 * @since 2021-07-20
 */
@Data
@ApiModel
public class ContractMachineResult implements Serializable {

    private static final long serialVersionUID = 1L;
 private  String contractName;
 private Date creationTime;
 private  String note;
   private  String signingSite;
    /**
     * 合同
     */
    @ApiModelProperty("合同")
    private Long contractId;

    /**
     * 购货单位
     */
    @ApiModelProperty("购货单位")
    private Long purchaseUnitId;

    /**
     * 供货单位
     */
    @ApiModelProperty("供货单位")
    private Long supplyUnitId;

    /**
     * 出售设备
     */
    @ApiModelProperty("出售设备")
    private String equipment;

    /**
     * 项目名称
     */
    @ApiModelProperty("项目名称")
    private String projectName;

    /**
     * 合同总金额
     */
    @ApiModelProperty("合同总金额")
    private Long contractAmount;

    /**
     * 付款方式
     */
    @ApiModelProperty("付款方式")
    private String payment;

    /**
     * 合同生效日期天数
     */
    @ApiModelProperty("合同生效日期天数")
    private Long effectiveDate;

    /**
     * 合同总额
     */
    @ApiModelProperty("合同总额")
    private Long amountOne;

    /**
     * 安装一周内付款总额
     */
    @ApiModelProperty("安装一周内付款总额")
    private Long amountTwo;

    /**
     * 运行日期
     */
    @ApiModelProperty("运行日期")
    private Long runDate;

    /**
     * 运行一周内付款总额
     */
    @ApiModelProperty("运行一周内付款总额")
    private Long amountThree;

    /**
     * 交货地点
     */
    @ApiModelProperty("交货地点")
    private String paceDelivery;

    /**
     * 收货人名称
     */
    @ApiModelProperty("收货人名称")
    private String delivererName;

    /**
     * 收货人地址
     */
    @ApiModelProperty("收货人地址")
    private String deliveryAddress;

    /**
     * 运输方式
     */
    @ApiModelProperty("运输方式")
    private String transportationMode;

    /**
     * 安装调试地点
     */
    @ApiModelProperty("安装调试地点")
    private String installationLocation;

    /**
     * 验收时间
     */
    @ApiModelProperty("验收时间")
    private Integer acceptanceTimeOne;

    @ApiModelProperty("")
    private Integer acceptanceTimeTwo;

    @ApiModelProperty("")
    private Integer acceptanceTimeThree;

    /**
     * 收地点
     */
    @ApiModelProperty("收地点")
    private String receiveLocation;

    /**
     * 收费标准
     */
    @ApiModelProperty("收费标准")
    private String chargeStandard;

    /**
     * 保修期限
     */
    @ApiModelProperty("保修期限")
    private Integer warrantyPeriodOne;

    @ApiModelProperty("")
    private Integer warrantyPeriodTwo;

    @ApiModelProperty("")
    private Integer warrantyPeriodThree;

    @ApiModelProperty("")
    private Integer warrantyPeriodFour;

    @ApiModelProperty("")
    private Integer warrantyPeriodFive;

    /**
     * 甲方代表人
     */
    @ApiModelProperty("甲方代表人")
    private String partyAName;

    /**
     * 甲方日期
     */
    @ApiModelProperty("甲方日期")
    private Date partyATime;

    /**
     * 乙方代表人
     */
    @ApiModelProperty("乙方代表人")
    private String partyBName;

    /**
     * 乙方日期
     */
    @ApiModelProperty("乙方日期")
    private Date partyBTime;
    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
}
