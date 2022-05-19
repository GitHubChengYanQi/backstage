package cn.atsoft.dasheng.form.model.params;

import lombok.Data;
import cn.atsoft.dasheng.model.validator.BaseValidatingParam;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
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
public class ActivitiProcessLogParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;

    private String source;

    /**
     * 主键
     */
    @ApiModelProperty("主键")
    private Long logId;

    private Long taskId;

    private String url;

    private Long formId;

    private String actionStatus;

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
     * 0（拒绝），1（通过）
     */
    @ApiModelProperty("0（拒绝），1（通过）")
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
