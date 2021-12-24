package cn.atsoft.dasheng.form.model.result;

import cn.atsoft.dasheng.form.pojo.ProcessEnum;
import cn.atsoft.dasheng.form.pojo.ProcessModuleEnum;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.util.Date;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * <p>
 * 流程主表
 * </p>
 *
 * @author Sing
 * @since 2021-11-10
 */
@Data
@ApiModel
public class ActivitiProcessResult implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 模块
     */
    private ProcessModuleEnum module;
    /**
     * 启用状态
     */
    private Integer status;

    /**
     * 流程ID，主键
     */
    @ApiModelProperty("流程ID，主键")
    private Long processId;

    /**
     * 名称
     */
    @ApiModelProperty("名称")
    private String processName;

    /**
     * 分类Id
     */
    @ApiModelProperty("分类Id")
    private Long categoryId;

    /**
     * 类型：ship（工艺），audit（审核）
     */
    @ApiModelProperty("类型：ship（工艺），audit（审核）")
    private ProcessEnum type;

    private String url;

    /**
     * 工艺表Id或表单Id
     */
    @ApiModelProperty("工艺表Id或表单Id")
    private Long formId;

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
