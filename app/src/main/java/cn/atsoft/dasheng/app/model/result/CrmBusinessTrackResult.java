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
 * 商机跟踪表
 * </p>
 *
 * @author
 * @since 2021-08-04
 */
@Data
@ApiModel
public class CrmBusinessTrackResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private UserResult User;
    private List<CrmBusinessResult> Business;
    private Long competitorsQuoteId;

    /**
     * 商机跟踪id
     */
    @ApiModelProperty("商机跟踪id")
    private Long trackId;
    private String type;
    private String note;
    private Integer money;
    private String time;
    private String name;
    private Integer offer;
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
}
