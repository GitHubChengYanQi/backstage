package cn.atsoft.dasheng.app.model.result;

import cn.atsoft.dasheng.form.model.result.ActivitiProcessTaskResult;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import lombok.Data;

import java.util.Date;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * <p>
 * 消息提醒
 * </p>
 *
 * @author
 * @since 2021-08-03
 */
@Data
@ApiModel
public class MessageResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer view;

    private User user;

    private Long promoter;

    private ActivitiProcessTaskResult processTaskResult;
    /**
     * 路径
     */
    @ApiModelProperty("路径")
    private String url;

    /**
     * 消息提醒id
     */
    @ApiModelProperty("消息提醒id")
    private Long messageId;

    /**
     * 人员id
     */
    @ApiModelProperty("关联人id")
    private Long userId;

    /**
     * 来源
     */
    @ApiModelProperty("来源")
    private String source;

    /**
     * 来源id
     */
    @ApiModelProperty("来源id")
    private Long sourceId;

    /**
     * 提醒时间
     */
    @ApiModelProperty("提醒时间")
    private Date time;

    /**
     * 提醒状态
     */
    @ApiModelProperty("提醒状态")
    private Integer state;

    /**
     * 提醒标题
     */
    @ApiModelProperty("提醒标题")
    private String title;

    /**
     * 提醒内容
     */
    @ApiModelProperty("提醒内容")
    private String content;

    /**
     * 提醒类型
     */
    @ApiModelProperty("提醒类型")
    private Integer type;

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
     * 排序
     */
    @ApiModelProperty("排序")
    private Long sort;
    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
    private Long deptId;
}
