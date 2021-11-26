package cn.atsoft.dasheng.erp.model.result;

import cn.atsoft.dasheng.erp.entity.QualityTaskDetail;
import cn.atsoft.dasheng.form.model.result.FormDataResult;
import lombok.Data;

import java.util.Date;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * <p>
 * 质检任务
 * </p>
 *
 * @author
 * @since 2021-11-16
 */
@Data
@ApiModel
public class QualityTaskResult implements Serializable {

    private static final long serialVersionUID = 1L;
    private List<QualityTaskDetailResult> details;
    private List<FormDataResult> formDataResults;
    private String userName;
    private String createName;

    private List<TaskCount> taskCounts;

    @ApiModelProperty("")
    private Long qualityTaskId;

    /**
     * 时间
     */
    @ApiModelProperty("时间")
    private Date time;

    /**
     * 负责人
     */
    @ApiModelProperty("负责人")
    private Long userId;

    /**
     * 类型
     */
    @ApiModelProperty("类型")
    private String type;

    /**
     * 编码
     */
    @ApiModelProperty("编码")
    private String coding;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remark;

    /**
     * 创建者
     */
    @ApiModelProperty(hidden = true)
    private Long createUser;

    /**
     * 修改者
     */
    @ApiModelProperty(hidden = true)
    private Long updateUser;

    /**
     * 创建时间
     */
    @ApiModelProperty(hidden = true)
    private Date createTime;

    /**
     * 修改时间
     */
    @ApiModelProperty(hidden = true)
    private Date updateTime;

    /**
     * 状态
     */
    @ApiModelProperty("状态")
    private Integer display;

    private Integer state;

    /**
     * 部门id
     */
    @ApiModelProperty("部门id")
    private Long deptId;
    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
}
