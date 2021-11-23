package cn.atsoft.dasheng.form.model.result;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
/**
 * <p>
 * 流程任务表	
 * </p>
 *
 * @author Jazz
 * @since 2021-11-19
 */
@Data
@ApiModel
public class ActivitiProcessTaskResult implements Serializable {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty("")
    private Long processTaskId;

    @ApiModelProperty("")
    private Long processId;

    private String taskName;

    private String deptIds;

    private String userIds;

    private String type;

    private String remark;

    private String cause;
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
