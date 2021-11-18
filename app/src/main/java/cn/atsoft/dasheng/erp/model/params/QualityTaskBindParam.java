package cn.atsoft.dasheng.erp.model.params;

import lombok.Data;
import cn.atsoft.dasheng.model.validator.BaseValidatingParam;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 
 * </p>
 *
 * @author song
 * @since 2021-11-17
 */
@Data
@ApiModel
public class QualityTaskBindParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;


    /**
     * 质检任务绑定id
     */
    @ApiModelProperty("质检任务绑定id")
    private Long bingId;

    /**
     * 质检任务id
     */
    @ApiModelProperty("质检任务id")
    private Long qualityTaskId;

    /**
     * 实物id
     */
    @ApiModelProperty("实物id")
    private Long inkindId;

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
