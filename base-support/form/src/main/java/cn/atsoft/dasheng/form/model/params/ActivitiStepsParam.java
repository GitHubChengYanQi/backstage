package cn.atsoft.dasheng.form.model.params;

import cn.atsoft.dasheng.form.entity.ActivitiSteps;
import lombok.Data;
import cn.atsoft.dasheng.model.validator.BaseValidatingParam;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 流程步骤表
 * </p>
 *
 * @author Sing
 * @since 2021-11-10
 */
@Data
@ApiModel
public class ActivitiStepsParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;

    private List<ActivitiStepsParam> conditionNodeList;

    private ActivitiSteps childNode;
    /**
     * 步骤Id，主键
     */
    @ApiModelProperty("步骤Id，主键")
    private Long setpsId;

    private String conditionNodes;

    private String nodeName;

    /**
     * 流程ID
     */
    @ApiModelProperty("流程ID")
    private Long processId;

    /**
     * 步骤类型：ship（工艺），setp（工序），audit（审核），audit_process（审核流程）
     */
    @ApiModelProperty("步骤类型：ship（工艺），setp（工序），audit（审核），audit_process（审核流程）")
    private String type;

    /**
     * 分表Id
     */
    @ApiModelProperty("分表Id")
    private Long formId;

    /**
     * 上一步
     */
    @ApiModelProperty("上一步")
    private Long supper;

    /**
     * 下级步骤
     */
    @ApiModelProperty("下级步骤")
    private String children;

    /**
     * 所有后续步骤
     */
    @ApiModelProperty("所有后续步骤")
    private String childrens;

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
