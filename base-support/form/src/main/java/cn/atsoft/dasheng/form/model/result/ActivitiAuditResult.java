package cn.atsoft.dasheng.form.model.result;

import lombok.Data;
import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
/**
 * <p>
 * 审批配置表
 * </p>
 *
 * @author Sing
 * @since 2021-11-10
 */
@Data
@ApiModel
public class ActivitiAuditResult implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 主键
     */
    @ApiModelProperty("主键")
    private Long auditId;

    /**
     * 步骤Id
     */
    @ApiModelProperty("步骤Id")
    private Long setpsId;

    /**
     * person（指定人），supervisor（主管），optional（自主选择）
     */
    @ApiModelProperty("person（指定人），supervisor（主管），optional（自主选择）")
    private String type;

    /**
     * 审批规则
     */
    @ApiModelProperty("审批规则")
    private String rule;

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