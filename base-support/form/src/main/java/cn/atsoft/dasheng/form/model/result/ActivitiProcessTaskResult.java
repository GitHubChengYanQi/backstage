package cn.atsoft.dasheng.form.model.result;

import cn.atsoft.dasheng.form.pojo.AuditRule;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import cn.atsoft.dasheng.sys.modular.system.model.result.UserResult;
import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.util.Date;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

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
public class ActivitiProcessTaskResult implements Serializable {

    private static final long serialVersionUID = 1L;


    private Long userId;

    private Object receipts;

    private ActivitiStepsResult stepsResult;
    private String coding;
    //单据负责人
    private UserResult userResult;

    private List<Long> processUserIds;   //执行人

    private List<UserResult> processUsers;

    private UserResult user;

    private List<Object> remarks;

    private Object object;

    @ApiModelProperty("")
    private Long processTaskId;

    private Object themeAndOrigin;

    private Integer version;

    @ApiModelProperty("")
    private Long processId;

    private String taskName;

    private Boolean permissions;

    private String deptIds;

    private String userIds;

    private String type;

    private String remark;

    private String cause;

    private Long formId;
    private String source;
    private Long sourceId;

    private String url;

    private String createName;

    private Integer status;


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
}
