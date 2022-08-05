package cn.atsoft.dasheng.form.model.params;

import lombok.Data;
import cn.atsoft.dasheng.model.validator.BaseValidatingParam;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
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
public class RemarksParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;

    private Long pid;

    private String source;

    private List<String> types;
    /**
     * 备注id
     */
    @ApiModelProperty("备注id")
    private Long remarksId;

    private String photoId;

    @ApiModelProperty("")
    private Long logId;

    private Long taskId;

    private String type;

    private String userIds;

    private Integer status;

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

    @Override
    public String checkParam() {
        return null;
    }

}
