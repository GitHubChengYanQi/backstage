package cn.atsoft.dasheng.form.model.result;

import cn.atsoft.dasheng.appBase.model.result.MediaResult;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import com.alibaba.fastjson.annotation.JSONField;
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

    private List<MediaResult> mediaResults;

    private MediaResult mediaResult;

    private static final long serialVersionUID = 1L;

    private ActivitiProcessTaskResult taskResult;

    private List<RemarksResult> childrens;

    private User user;

    @JSONField(serialize = false)
    private Long sourceId;

    @JSONField(serialize = false)
    private String source;


    @JSONField(serialize = false)
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
    @JSONField(serialize = false)
    private Long updateUser;

    @ApiModelProperty(hidden = true)
    private Date createTime;

    @ApiModelProperty(hidden = true)
    @JSONField(serialize = false)
    private Date updateTime;

    @ApiModelProperty("父ID顺序数组")
    @JSONField(serialize = false)
    private List<String> pidValue;
}
