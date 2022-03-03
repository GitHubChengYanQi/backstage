package cn.atsoft.dasheng.production.model.params;

import lombok.Data;
import cn.atsoft.dasheng.model.validator.BaseValidatingParam;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 生产任务
 * </p>
 *
 * @author 
 * @since 2022-02-28
 */
@Data
@ApiModel
public class ProductionTaskParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;


    /**
     * 生产任务id
     */
    @ApiModelProperty("生产任务id")
    private Long productionTaskId;

    /**
     * 编码
     */
    @ApiModelProperty("编码")
    private String coding;

    /**
     * 生产任务名称
     */
    @ApiModelProperty("生产任务名称")
    private String productionTaskName;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remark;

    /**
     * 负责人
     */
    @ApiModelProperty("负责人")
    private Long userId;

    /**
     * 工单id
     */
    @ApiModelProperty("工单id")
    private Long workOrderId;

    /**
     * 工序id
     */
    @ApiModelProperty("工序id")
    private Long shipSetpId;

    /**
     * 生产时间
     */
    @ApiModelProperty("生产时间")
    private Date productionTime;

    /**
     * 创建者
     */
    @ApiModelProperty(hidden = true)
    private Long createUser;

    /**
     * 部门编号
     */
    @ApiModelProperty("部门编号")
    private Long deptId;

    /**
     * 状态
     */
    @ApiModelProperty("状态")
    private Integer display;

    /**
     * 修改时间
     */
    @ApiModelProperty(hidden = true)
    private Date updateTime;

    /**
     * 创建时间
     */
    @ApiModelProperty(hidden = true)
    private Date createTime;

    /**
     * 修改者
     */
    @ApiModelProperty(hidden = true)
    private Long updateUser;

    /**
     * 主题
     */
    @ApiModelProperty("主题")
    private String theme;

    /**
     * 来源Json字符串
     */
    @ApiModelProperty("来源Json字符串")
    private String origin;

    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;

    @Override
    public String checkParam() {
        return null;
    }

}