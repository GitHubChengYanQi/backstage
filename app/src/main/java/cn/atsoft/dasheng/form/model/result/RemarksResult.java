package cn.atsoft.dasheng.form.model.result;

import cn.atsoft.dasheng.sys.modular.system.entity.User;
import lombok.Data;

import java.util.Date;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * <p>
 * log备注
 * </p>
 *
 * @author song
 * @since 2021-12-16
 */
@Data
@ApiModel
public class RemarksResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private ActivitiProcessTaskResult taskResult;

    private List<RemarksResult> childrens;

    private User user;

    private Long sourceId;

    private String source;



    private Long pid;
    /**
     * 备注id
     */
    @ApiModelProperty("备注id")
    private Long remarksId;

    @ApiModelProperty("")
    private Long logId;

    private String photoId;

    private Integer status;


    private String userIds;
    private Long taskId;
    private String type;
    /**
     * 内容
     */
    @ApiModelProperty("内容")
    private String content;

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
