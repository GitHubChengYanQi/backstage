package cn.atsoft.dasheng.form.model.params;

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
 * 流程任务表
 * </p>
 *
 * @author Jazz
 * @since 2021-11-19
 */
@Data
@ApiModel
public class ActivitiProcessTaskParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;

    private List<String> types;

    //人员id
    private Long userId;
    //我的审批  等
    private Integer queryType;
    //任务节点状态
    private Integer status;

    private String skuName;

    private Long deptId;
    @ApiModelProperty("")
    private Long processTaskId;

    private Long sourceId;

    private String source;

    @ApiModelProperty("")
    private Long processId;

    private String taskName;

    private String deptIds;

    private String userIds;

    private String type;

    private String remark;

    private String cause;



    @ApiModelProperty("主题")
    private String theme;


    @ApiModelProperty("来源")
    private String origin;

    @ApiModelProperty("主任务id")
    private Long mainTaskId;


    @ApiModelProperty("上级任务id")
    private Long pid;


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

    @Override
    public String checkParam() {
        return null;
    }

}
