package cn.atsoft.dasheng.erp.model.params;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import cn.atsoft.dasheng.model.validator.BaseValidatingParam;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 质检方案详情
 * </p>
 *
 * @author Captain_Jazz
 * @since 2021-10-28
 */
@Data
@ApiModel
public class QualityPlanDetailParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;

    /**
     * 分类id
     */
    private Long qualityPlanClassId;

    @ApiModelProperty("")
    private Long planDetailId;

    /**
     * 关联质检方案表主键id
     */
    @ApiModelProperty("关联质检方案表主键id")
    private Long planId;

    /**
     * 是否必填
     */
    @ApiModelProperty("是否必填")
    private Long isNull;

    /**
     * 排序
     */
    @ApiModelProperty("排序")
    private Long sort;


    /**
     * 质检项id
     */
    @ApiModelProperty("质检项id")
    private Long qualityCheckId;

    /**
     * 运算符
     */
    @ApiModelProperty("运算符")
    private Long operator;

    /**
     * 标准值
     */
    @ApiModelProperty("标准值")
    private String standardValue;

    /**
     * 抽检类型
     */
    @ApiModelProperty("抽检类型")
    private String testingType;

    /**
     * 质检数量
     */
    @ApiModelProperty("质检数量")
    private Long qualityAmount;

    /**
     * 质检比例
     */
    @ApiModelProperty("质检比例")
    private Long qualityProportion;

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
