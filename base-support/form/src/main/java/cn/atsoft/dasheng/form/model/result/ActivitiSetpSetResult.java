package cn.atsoft.dasheng.form.model.result;

import cn.atsoft.dasheng.form.entity.ActivitiSetpSetDetail;
import lombok.Data;

import java.util.Date;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * <p>
 * 工序步骤设置表
 * </p>
 *
 * @author Sing
 * @since 2021-11-10
 */
@Data
@ApiModel
public class ActivitiSetpSetResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<ActivitiSetpSetDetailResult> setpSetDetails;

    private Object shipSetpResult;

    private String type = "setp";
    /**
     * 主键
     */
    @ApiModelProperty("主键")
    private Long setId;

    private Integer productionType;

    /**
     * 步骤Id
     */
    @ApiModelProperty("步骤Id")
    private Long setpsId;

    /**
     * 工序Id
     */
    @ApiModelProperty("工序Id")
    private Long shipSetpId;

    /**
     * 标准工作时长，按小时计算
     */
    @ApiModelProperty("标准工作时长，按小时计算")
    private Integer length;

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
