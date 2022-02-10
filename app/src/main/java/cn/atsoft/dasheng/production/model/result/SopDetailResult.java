package cn.atsoft.dasheng.production.model.result;

import lombok.Data;

import java.util.Date;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * <p>
 * sop 详情
 * </p>
 *
 * @author song
 * @since 2022-02-10
 */
@Data
@ApiModel
public class SopDetailResult implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 详情id
     */
    @ApiModelProperty("详情id")
    private Long sopDetailId;

    private String mediaUrl;

    /**
     * sop
     */
    @ApiModelProperty("sop")
    private Long sopId;

    /**
     * 步骤名
     */
    @ApiModelProperty("步骤名")
    private String stepName;

    /**
     * 示例图
     */
    @ApiModelProperty("示例图")
    private String img;

    /**
     * 操作说明
     */
    @ApiModelProperty("操作说明")
    private String instructions;

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
     * 部门编号
     */
    @ApiModelProperty("部门编号")
    private Long deptId;

    /**
     * 排序
     */
    @ApiModelProperty("排序")
    private Integer sort;
    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
}
