package cn.atsoft.dasheng.crm.model.result;

import lombok.Data;
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
    private  Long businessId;

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
