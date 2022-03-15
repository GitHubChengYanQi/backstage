package cn.atsoft.dasheng.appBase.model.params;

import cn.atsoft.dasheng.model.validator.BaseValidatingParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 
 * </p>
 *
 * @author Sing
 * @since 2021-04-21
 */
@Data
@ApiModel
public class MediaParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;
    private List<String> types;

    /**
     * 媒体ID
     */
    @ApiModelProperty("媒体ID")
    private Long mediaId;

    /**
     * 文件路径
     */
    @ApiModelProperty("文件路径")
    private String path;

    /**
     * OSS储存区
     */
    @ApiModelProperty("OSS储存区")
    private String endpoint;

    /**
     * OSS储存块
     */
    @ApiModelProperty("OSS储存块")
    private String bucket;

    /**
     * 上传状态
     */
    @ApiModelProperty("上传状态")
    private Integer status;

    /**
     * 用户ID
     */
    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty(hidden = true)
    private Date createTime;

    @ApiModelProperty(hidden = true)
    private Long createUser;

    @ApiModelProperty(hidden = true)
    private Long updateUser;

    @ApiModelProperty(hidden = true)
    private Date updateTime;

    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;

    @Override
    public String checkParam() {
        return null;
    }

}
