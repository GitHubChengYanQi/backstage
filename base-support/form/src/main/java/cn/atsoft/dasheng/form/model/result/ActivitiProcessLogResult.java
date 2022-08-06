package cn.atsoft.dasheng.form.model.result;

import cn.atsoft.dasheng.form.entity.ActivitiAudit;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.Date;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * <p>
 * 流程日志表
 * </p>
 *
 * @author Sing
 * @since 2021-11-10
 */
@Data
@ApiModel
public class ActivitiProcessLogResult implements Serializable {

    private static final long serialVersionUID = 1L;

    @JSONField(serialize = false)
    private ActivitiStepsResult stepsResult;

    private User user;

    private ActivitiAudit activitiAudit;

    private ActivitiProcessTaskResult taskResult;

    private List<DocumentsActionResult> actionResults;


    private String actionStatus;
    /**
     * 主键
     */
    @ApiModelProperty("主键")
    private Long logId;


    private Long taskId;

    /**
     * 流程Id
     */
    @ApiModelProperty("流程Id")
    @JSONField(serialize = false)
    private Long peocessId;

    /**
     * 步骤Id
     */
    @ApiModelProperty("步骤Id")
    @JSONField(serialize = false)
    private Long setpsId;

    /**
     * 0（拒绝），1（通过）
     */
    @ApiModelProperty("0（拒绝），1（通过）")
    private Integer status;

    /**
     * 删除状态
     */
    @ApiModelProperty("删除状态")
    @JSONField(serialize = false)
    private Integer display;

    @ApiModelProperty(hidden = true)
    private Long createUser;

    @ApiModelProperty(hidden = true)
    @JSONField(serialize = false)
    private Long updateUser;

    @ApiModelProperty(hidden = true)
    private Date createTime;

    @ApiModelProperty(hidden = true)
    @JSONField(serialize = false)
    private Date updateTime;

    @JSONField(serialize = false)
    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
}
