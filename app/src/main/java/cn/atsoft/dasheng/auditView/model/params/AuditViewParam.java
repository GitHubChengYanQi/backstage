package cn.atsoft.dasheng.auditView.model.params;

import lombok.Data;
import cn.atsoft.dasheng.model.validator.BaseValidatingParam;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 所有审核
 * </p>
 *
 * @author song
 * @since 2021-12-16
 */
@Data
@ApiModel
public class AuditViewParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;


    /**
     * 审核id
     */
    @ApiModelProperty("审核id")
    private Long auditViewId;

    @ApiModelProperty("")
    private Long userId;

    /**
     * 任务类型
     */
    @ApiModelProperty("任务类型")
    private String taskType;

    /**
     * 流程id
     */
    @ApiModelProperty("流程id")
    private Long processId;

    /**
     * 审核logid
     */
    @ApiModelProperty("审核logid")
    private Long auditLogId;

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

    @Override
    public String checkParam() {
        return null;
    }

}
