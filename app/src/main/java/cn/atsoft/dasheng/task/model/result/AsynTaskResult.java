package cn.atsoft.dasheng.task.model.result;

import cn.atsoft.dasheng.app.pojo.AllBomResult;
import lombok.Data;

import java.util.Date;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * <p>
 * 等待任务表
 * </p>
 *
 * @author song
 * @since 2022-04-01
 */
@Data
@ApiModel
public class AsynTaskResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private AllBomResult allBomResult;
    /**
     * 任务id
     */
    @ApiModelProperty("任务id")
    private Long taskId;

    /**
     * 总数
     */
    @ApiModelProperty("总数")
    private Integer allCount;

    /**
     * 当前数
     */
    @ApiModelProperty("当前数")
    private Integer count;

    /**
     * 类型
     */
    @ApiModelProperty("类型")
    private String type;

    /**
     * json 内容
     */
    @ApiModelProperty("json 内容")
    private String content;

    /**
     * 状态
     */
    @ApiModelProperty("状态")
    private Integer status;

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
    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
}
