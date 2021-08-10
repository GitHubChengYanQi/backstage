package cn.atsoft.dasheng.app.model.result;

import cn.atsoft.dasheng.sys.modular.system.model.result.UserResult;
import lombok.Data;

import java.util.Date;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * <p>
 * 商机表
 * </p>
 *
 * @author
 * @since 2021-08-03
 */
@Data
@ApiModel
public class CrmBusinessResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private CustomerResult customer;
    private OriginResult origin;
    private CrmBusinessSalesResult sales;
    private UserResult user;
    private CrmBusinessTrackResult track;
    private CrmBusinessSalesProcessResult process;
    private Long trackId;
    private Long processId;
    private String personName;
   private  int sort;


    /**
     * 商机id
     */
    @ApiModelProperty("商机id")
    private Long businessId;

    /**
     * 商机名称
     */
    @ApiModelProperty("商机名称")
    private String businessName;

    /**
     * 负责人
     */
    @ApiModelProperty("负责人")
    private Long person;

    /**
     * 客户id
     */
    @ApiModelProperty("客户id")
    private Long customerId;

    /**
     * 结单日期
     */
    @ApiModelProperty("结单日期")
    private Date statementTime;

    /**
     * 阶段变更时间
     */
    @ApiModelProperty("阶段变更时间")
    private Date changeTime;

    /**
     * 商机金额
     */
    @ApiModelProperty("商机金额")
    private Integer opportunityAmount;

    /**
     * 阶段状态
     */
    @ApiModelProperty("阶段状态")
    private String state;

    /**
     * 销售流程id
     */
    @ApiModelProperty("销售流程id")
    private Long salesId;

    /**
     * 产品合计
     */
    @ApiModelProperty("产品合计")
    private Double totalProducts;

    /**
     * 机会来源
     */
    @ApiModelProperty("机会来源")
    private Long originId;

    /**
     * 立项日期
     */
    @ApiModelProperty("立项日期")
    private Date time;

    /**
     * 商机阶段
     */
    @ApiModelProperty("商机阶段")
    private String stage;

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
