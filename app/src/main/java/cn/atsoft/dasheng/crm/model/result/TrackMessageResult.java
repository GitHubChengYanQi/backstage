package cn.atsoft.dasheng.crm.model.result;

import cn.atsoft.dasheng.app.model.result.BusinessTrackResult;
import cn.atsoft.dasheng.sys.modular.system.model.result.UserResult;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

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
public class TrackMessageResult implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long businessId;

    private BusinessTrackResult businessTrackResult;
    /**
     * 纬度
     */
    private UserResult userResult;

    private BigDecimal latitude;

    private Integer state;

    private String type;
    /**
     * 经度
     */
    private BigDecimal longitude;

    private Long userId;

    private Long customerId;

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

    private Date time;

    private String note;
    private String image;

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
}
