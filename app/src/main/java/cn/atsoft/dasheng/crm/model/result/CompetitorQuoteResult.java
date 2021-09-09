package cn.atsoft.dasheng.crm.model.result;

import cn.atsoft.dasheng.app.model.result.CrmBusinessResult;
import lombok.Data;
import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
/**
 * <p>
 * 竞争对手报价
 * </p>
 *
 * @author 
 * @since 2021-09-07
 */
@Data
@ApiModel
public class CompetitorQuoteResult implements Serializable {

    private static final long serialVersionUID = 1L;
//        private List<CompetitorQuoteResult> list;
        private   CompetitorResult competitorResult;
        private CrmBusinessResult crmBusinessResult;

    /**
     * 报价id
     */
    @ApiModelProperty("报价id")
    private Long quoteId;

    private Long businessId;
    /**
     * 竞争对手id
     */
    @ApiModelProperty("竞争对手id")
    private Long competitorId;

    /**
     * 报价金额
     */
    @ApiModelProperty("报价金额")
    private Integer competitorsQuote;

    private Integer campType;

    /**
     * 报价状态
     */
    @ApiModelProperty("报价状态")
    private Integer quoteStatus;

    /**
     * 报价分类
     */
    @ApiModelProperty("报价分类")
    private Integer quoteType;

    /**
     * 报价日期
     */
    @ApiModelProperty("报价日期")
    private Date quoteDate;

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
