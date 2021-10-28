package cn.atsoft.dasheng.erp.model.params;

import lombok.Data;
import cn.atsoft.dasheng.model.validator.BaseValidatingParam;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
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
public class QualityPlanParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;


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
    @ApiModelProperty(hidden = true)
    private Date createTime;

    /**
     * 修改时间
     */
    @ApiModelProperty(hidden = true)
    private Date updateTime;

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
     * 状态
     */
    @ApiModelProperty("状态")
    private Integer display;

    /**
     * 部门编号
     */
    @ApiModelProperty("部门编号")
    private Long deptId;

    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;

    @Override
    public String checkParam() {
        return null;
    }

}
