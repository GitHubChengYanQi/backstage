package cn.atsoft.dasheng.coderule.model.result;


import cn.atsoft.dasheng.coderule.model.RestCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 编码规则
 * </p>
 *
 * @author song
 * @since 2021-10-22
 */
@Data
@ApiModel
public class RestCodingRulesResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private RestCodingRulesCategoryResult codingRulesClassificationResult;


    private List<RestCode> codings;

    @ApiModelProperty("描述")
    private String note;


    /**
     * 流水号
     */
    private String serial;

    /**
     * 状态
     */
    private Integer state;
    private Integer module;
    /**
     * 排序
     */
    private Long sort;

    /**
     * 编码规则id
     */
    @ApiModelProperty("编码规则id")
    private Long codingRulesId;

    /**
     * 编码规则分类id
     */
    @ApiModelProperty("编码规则分类id")
    private Long codingRulesClassificationId;

    /**
     * 编码规则名称
     */
    @ApiModelProperty("编码规则名称")
    private String name;

    /**
     * 编码规则
     */
    @ApiModelProperty("编码规则")
    private String codingRules;

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

    /**
     * 部门id
     */
    @ApiModelProperty("部门id")
    private Long deptId;
    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
}
