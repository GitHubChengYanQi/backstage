package cn.atsoft.dasheng.app.model.params;

import cn.atsoft.dasheng.crm.model.params.CompetitorQuoteParam;
import lombok.Data;
import cn.atsoft.dasheng.model.validator.BaseValidatingParam;

import java.util.Date;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * <p>
 * 商机跟踪表
 * </p>
 *
 * @author
 * @since 2021-08-04
 */
@Data
@ApiModel
public class CrmBusinessTrackParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;
    private List<CompetitorQuoteParam> competitorQuoteParam;
    private Long competitorsQuoteId;
    private Integer campType;
    /**
     * 商机跟踪id
     */
    private Long competitionId;
    private String name;
    private Date time;

    private Integer offer;
    private String note;
    private String type;

    private Integer money;
    @ApiModelProperty("商机跟踪id")
    private Long trackId;

    private Long businessId;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private Long noteId;

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
