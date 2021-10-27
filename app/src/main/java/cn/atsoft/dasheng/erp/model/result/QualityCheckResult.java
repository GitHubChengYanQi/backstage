package cn.atsoft.dasheng.erp.model.result;

import cn.atsoft.dasheng.erp.entity.Tool;
import lombok.Data;

import java.util.Date;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * <p>
 * 质检表
 * </p>
 *
 * @author song
 * @since 2021-10-27
 */
@Data
@ApiModel
public class QualityCheckResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<ToolResult> tools;
    /**
     * 质检id
     */
    @ApiModelProperty("质检id")
    private Long qualityCheckId;

    /**
     * 名称
     */
    @ApiModelProperty("名称")
    private String name;

    /**
     * 简称
     */
    @ApiModelProperty("简称")
    private String simpleName;

    /**
     * 质检分类
     */
    @ApiModelProperty("质检分类")
    private Long qualityCheckClassificationId;

    /**
     * 工具id
     */
    @ApiModelProperty("工具id")
    private String tool;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String note;

    /**
     * 规范
     */
    @ApiModelProperty("规范")
    private String norm;

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

    /**
     * 附件
     */
    @ApiModelProperty("附件")
    private String attachment;

    /**
     * 编码
     */
    @ApiModelProperty("编码")
    private String coding;
    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
}
