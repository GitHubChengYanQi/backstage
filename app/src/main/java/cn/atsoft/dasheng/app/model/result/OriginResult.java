package cn.atsoft.dasheng.app.model.result;

import lombok.Data;
import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
/**
 * <p>
 * 来源表
 * </p>
 *
 * @author
 * @since 2021-07-19
 */
@Data
@ApiModel
public class OriginResult implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 来源id
     */
    @ApiModelProperty("来源id")
    private Long originId;

    /**
     * 来源名称
     */
    @ApiModelProperty("来源名称")
    private String originName;

    /**
     * 创建者
     */
    @ApiModelProperty(hidden = true)
    private Long createUser;

    /**
     * 创建时间
     */
    @ApiModelProperty(hidden = true)
    private Date createTime;

    /**
     * 修改者
     */
    @ApiModelProperty(hidden = true)
    private Long updateUser;

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
