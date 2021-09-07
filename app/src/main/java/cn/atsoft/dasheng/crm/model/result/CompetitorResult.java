package cn.atsoft.dasheng.crm.model.result;

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
 * @since 2021-09-07
 */
@Data
@ApiModel
public class CompetitorResult implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 竞争对手id
     */
    @ApiModelProperty("竞争对手id")
    private Long competitorId;

    private String adress;

    private List<CompetitorQuoteResult>competitorQuoteResults;

    /**
     * 报价id
     */
    @ApiModelProperty("报价id")
    private Long competitorsQuoteId;

    /**
     * 竞争对手企业名称
     */
    @ApiModelProperty("竞争对手企业名称")
    private String name;

    /**
     * 联系电话
     */
    @ApiModelProperty("联系电话")
    private Integer phone;

    /**
     * 网址 
     */
    @ApiModelProperty("网址 ")
    private String url;

    /**
     * 创立日期
     */
    @ApiModelProperty("创立日期")
    private Date creationDate;

    /**
     * 邮箱
     */
    @ApiModelProperty("邮箱")
    private String email;

    /**
     * 员工规模
     */
    @ApiModelProperty("员工规模")
    private Long staffSize;

    /**
     * 公司所有制
     */
    @ApiModelProperty("公司所有制")
    private Integer ownership;

    /**
     * 地区
     */
    @ApiModelProperty("地区")
    private String region;

    /**
     * 竞争级别
     */
    @ApiModelProperty("竞争级别")
    private Integer competitionLevel;

    /**
     * 年销售
     */
    @ApiModelProperty("年销售")
    private Integer annualSales;

    /**
     * 公司简介
     */
    @ApiModelProperty("公司简介")
    private String companyProfile;

    /**
     * 对手优势
     */
    @ApiModelProperty("对手优势")
    private String rivalAdvantage;

    /**
     * 对手劣势
     */
    @ApiModelProperty("对手劣势")
    private String opponentsWeaknesses;

    /**
     * 采取对策
     */
    @ApiModelProperty("采取对策")
    private String takeCountermeasures;

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
