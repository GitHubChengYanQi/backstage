package cn.atsoft.dasheng.form.model.result;

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
 * @author song
 * @since 2022-08-16
 */
@Data
@ApiModel
public class TaskParticipantResult implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 任务参与人 
     */
    @ApiModelProperty("任务参与人 ")
    private Long participantId;

    /**
     * 任务id
     */
    @ApiModelProperty("任务id")
    private Long processTaskId;

    /**
     * 参与人
     */
    @ApiModelProperty("参与人")
    private Long userId;

    /**
     * 删除状态
     */
    @ApiModelProperty("删除状态")
    private Integer display;

    @ApiModelProperty(hidden = true)
    private Long createUser;

    @ApiModelProperty(hidden = true)
    private Long updateUser;

    @ApiModelProperty(hidden = true)
    private Date createTime;

    @ApiModelProperty(hidden = true)
    private Date updateTime;
    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
}
