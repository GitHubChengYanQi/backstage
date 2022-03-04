package cn.atsoft.dasheng.form.model.result;

import cn.atsoft.dasheng.form.pojo.AuditRule;
import cn.atsoft.dasheng.form.pojo.QualityRules;
import cn.atsoft.dasheng.form.pojo.StepsType;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

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
public class ActivitiStepsResult implements Serializable {


    private static final long serialVersionUID = 1L;

    private AuditRule auditRule;

    private ActivitiStepsResult childNode;

    private ActivitiAuditResult serviceAudit;

    private ActivitiProcessLogResult logResult;

    private String stepType;

    private String auditType;

    private Boolean permissions;

    private Object processRoute;

    private ActivitiSetpSetResult setpSet;


    private ActivitiSetpSetResult setpSetResult;
    private List<ActivitiSetpSetDetailResult> setpSetDetailResults;

    private List<ActivitiStepsResult> conditionNodeList;

    private List<ActivitiStepsResult> childrenSteps;


    /**
     * 步骤Id，主键
     */

    @ApiModelProperty("步骤Id，主键")
    private Long setpsId;

    private String conditionNodes;


    /**
     * 流程ID
     */
    @ApiModelProperty("流程ID")
    private Long processId;

    /**
     * 步骤类型：ship（工艺），setp（工序），audit（审核），audit_process（审核流程）
     */

    @ApiModelProperty("步骤类型：ship（工艺），setp（工序），audit（审核），audit_process（审核流程）")
    @JSONField(serialzeFeatures = SerializerFeature.WriteEnumUsingToString)
    private StepsType type;

    /**
     * 分表Id
     */
    @JSONField(serialize = false)
    @ApiModelProperty("分表Id")
    private Long formId;

    /**
     * 上一步
     */
    @JSONField(serialize = false)
    @ApiModelProperty("上一步")
    private Long supper;

    /**
     * 下级步骤
     */
    @JSONField(serialize = false)
    @ApiModelProperty("下级步骤")
    private String children;

    /**
     * 所有后续步骤
     */
    @JSONField(serialize = false)
    @ApiModelProperty("所有后续步骤")
    private String childrens;

    /**
     * 删除状态
     */
    @JSONField(serialize = false)
    @ApiModelProperty("删除状态")
    private Integer display;
    @JSONField(serialize = false)
    @ApiModelProperty(hidden = true)
    private Long createUser;
    @JSONField(serialize = false)
    @ApiModelProperty(hidden = true)
    private Long updateUser;
    @JSONField(serialize = false)
    @ApiModelProperty(hidden = true)
    private Date createTime;
    @JSONField(serialize = false)
    @ApiModelProperty(hidden = true)
    private Date updateTime;
    @JSONField(serialize = false)
    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;


}
