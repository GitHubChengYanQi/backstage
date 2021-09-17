package cn.atsoft.dasheng.crm.model.params;

import cn.atsoft.dasheng.app.model.params.BusinessTrackParam;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import cn.atsoft.dasheng.model.validator.BaseValidatingParam;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 商机跟踪内容
 * </p>
 *
 * @author
 * @since 2021-09-07
 */
@Data
@ApiModel
public class TrackMessageParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;
    private Long businessId;
    //    private List<CompetitorParam>
    private List<BusinessTrackParam> businessTrackParams;
    /**
     * 纬度
     */

    private Integer money;
    private BigDecimal latitude;

    private List<CompetitorQuoteParam> competitorQuoteParam;
    private Integer quoteStatus;


    private Long userId;
    private Long customerId;
    private String type;
    /**
     * 经度
     */

    private BigDecimal longitude;
    private String image;
    /**
     * 商机跟踪内容id
     */
    @ApiModelProperty("商机跟踪内容id")
    private Long trackMessageId;

    /**
     * 跟踪内容
     */
    @ApiModelProperty("跟踪内容")
    private String message;

    private Date time;

    private String note;

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
