package cn.atsoft.dasheng.crm.model.params;

import lombok.Data;
import cn.atsoft.dasheng.model.validator.BaseValidatingParam;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
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
public class CompetitorQuoteParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;


    /**
     * 报价id
     */
    @ApiModelProperty("报价id")
    private Long quoteId;

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

    /**
     * 报价状态
     */
    @ApiModelProperty("报价状态")
    private Integer quoteStatus;

    private Integer campType;

    /**
     * 关联客户
     */
    @ApiModelProperty("关联客户")
    private String relatedCustomers;

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

    @Override
    public String checkParam() {
        return null;
    }

}
