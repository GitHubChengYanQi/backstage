package cn.atsoft.dasheng.audit.model.params;

import lombok.Data;
import cn.atsoft.dasheng.model.validator.BaseValidatingParam;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 流程日志人员表
 * </p>
 *
 * @author Captain_Jazz
 * @since 2023-02-16
 */
@Data
@ApiModel
public class ActivitiProcessLogDetailParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;


    /**
     * 主键
     */
    @ApiModelProperty("主键")
    private Long logDetailId;

    /**
     * 主表id
     */
    @ApiModelProperty("主表id")
    private Long logId;

    /**
     * 流程Id
     */
    @ApiModelProperty("流程Id")
    private Long peocessId;

    /**
     * 步骤Id
     */
    @ApiModelProperty("步骤Id")
    private Long setpsId;

    /**
     * 任务id
     */
    @ApiModelProperty("任务id")
    private Long taskId;

    /**
     * 审批人id	
     */
    @ApiModelProperty("审批人id	")
    private Long userId;

    /**
     * 0（拒绝），1（通过） -1（创建)
     */
    @ApiModelProperty("0（拒绝），1（通过） -1（创建)")
    private Integer status;

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
