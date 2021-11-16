package cn.atsoft.dasheng.erp.model.result;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Date;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * <p>
 * 质检方案
 * </p>
 *
 * @author Captain_Jazz
 * @since 2021-10-28
 */
@Data
@ApiModel
public class QualityPlanResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<QualityPlanDetailResult> qualityPlanDetailParams;
    /**
     * 质检方案id
     */
    @ApiModelProperty("质检方案id")
    private Long qualityPlanId;

    /**
     * 编号
     */
    @ApiModelProperty("编号")
    private String planCoding;

    /**
     * 抽检类型
     */
    @ApiModelProperty("抽检类型")
    private String testingType;

    /**
     * 方案名称
     */
    @ApiModelProperty("方案名称")
    private String planName;

    /**
     * 状态
     */
    @ApiModelProperty("状态")
    private Integer planStatus;

    /**
     * 质检类型
     */
    @ApiModelProperty("质检类型")
    private String planType;

    /**
     * 特别提醒
     */
    @ApiModelProperty("特别提醒")
    private String attentionPlease;

    /**
     * 附件
     */
    @ApiModelProperty("附件")
    private String planAdjunct;

    /**
     * 创建时间
     */
    @JSONField(serialize = false)

    @ApiModelProperty(hidden = true)
    private Date createTime;

    /**
     * 修改时间
     */

    @JSONField(serialize = false)
    @ApiModelProperty(hidden = true)
    private Date updateTime;

    /**
     * 创建者
     */

    @JSONField(serialize = false)
    @ApiModelProperty(hidden = true)
    private Long createUser;

    /**
     * 修改者
     */
    @ApiModelProperty(hidden = true)
    private Long updateUser;

    /**
     * 状态
     */
    @JSONField(serialize = false)

    @ApiModelProperty("状态")
    private Integer display;

    /**
     * 部门编号
     */
    @JSONField(serialize = false)

    @ApiModelProperty("部门编号")
    private Long deptId;
    @JSONField(serialize = false)
    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
}
