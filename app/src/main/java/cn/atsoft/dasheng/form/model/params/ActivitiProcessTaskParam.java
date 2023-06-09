package cn.atsoft.dasheng.form.model.params;

import cn.atsoft.dasheng.purchase.pojo.ThemeAndOrigin;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import cn.atsoft.dasheng.model.validator.BaseValidatingParam;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
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

    private String noticeId;
    private Date startTime;

    private Long orderId;
    private Long productionPlan;

    private Date endTime;

    //通过物料查询任务
    private Long skuId;

    private String auditRule;

    private Long participantUser;

    private String outTime;
    private String keyWords;
    private Long customerId;
    private Long pickUserId;

    private List<String> types;

    private String auditType;
    private String skuName;
    private Integer queryType;

    private List<Long> taskIds = new ArrayList<>();

    private List<Long> timeOutTaskIds;

    private Long setpsId;

    private Long deptId;
    @ApiModelProperty("")
    private Long processTaskId;

    @ApiModelProperty("")
    private Long processId;

    private String taskName;

    private String deptIds;

    private String userIds;

    private Long userId;

    private String type;

    private List<String> statusList;

    private String remark;

    private String cause;

    private Long formId;

    private String url;

    private Long qTaskId;

    private Integer status;
    private Long sourceId;
    private String source;
    private ThemeAndOrigin themeAndOrigin;

    @ApiModelProperty("主题")
    private String theme;


    @ApiModelProperty("来源")
    private String origin;

    @ApiModelProperty("主任务id")
    private Long mainTaskId;


    @ApiModelProperty("上级任务id")
    private Long pid;
    @ApiModelProperty("租户id")
    private Long tenantId;


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
