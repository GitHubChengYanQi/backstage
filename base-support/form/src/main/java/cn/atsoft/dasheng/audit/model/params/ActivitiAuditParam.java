package cn.atsoft.dasheng.audit.model.params;

import cn.atsoft.dasheng.form.pojo.AuditRule;
import lombok.Data;
import cn.atsoft.dasheng.model.validator.BaseValidatingParam;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
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
public class ActivitiAuditParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;


    private Long processId;
    /**
     * 主键
     */
    @ApiModelProperty("步骤id集合")
    private List<Long> stepsIds;
    @ApiModelProperty("主键")
    private Long auditId;

    private String url;

    private Long documentsStatusId;

    private String formType;

    private String action;

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
    private AuditRule rule;

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
