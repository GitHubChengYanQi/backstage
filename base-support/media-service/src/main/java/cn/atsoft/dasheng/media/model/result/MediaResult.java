package cn.atsoft.dasheng.media.model.result;

import com.alibaba.fastjson.annotation.JSONField;
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
public class MediaResult implements Serializable {

    private static final long serialVersionUID = 1L;
    private String url;

    /**
     * 媒体ID
     */
    @ApiModelProperty("媒体ID")
    private Long mediaId;
    /**
      * 媒体ID
    */
    @ApiModelProperty("媒体名称")
    private String filedName;
    /**
      * 媒体ID
    */
    @ApiModelProperty("缩略图")
    private String ThumbUrl;
    /**
     * 媒体ID
     */
    @ApiModelProperty("媒体类型")
    private String type;

    /**
     * 文件路径
     */
    @JSONField(serialize = false)
    @ApiModelProperty("文件路径")
    private String path;

    /**
     * OSS储存区
     */
    @JSONField(serialize = false)
    @ApiModelProperty("OSS储存区")
    private String endpoint;

    /**
     * OSS储存块
     */
    @JSONField(serialize = false)
    @ApiModelProperty("OSS储存块")
    private String bucket;

    /**
     * 上传状态
     */
    @JSONField(serialize = false)
    @ApiModelProperty("上传状态")
    private Integer status;

    /**
     * 用户ID
     */
    @JSONField(serialize = false)
    @ApiModelProperty("用户ID")
    private Long userId;
    @JSONField(serialize = false)
    @ApiModelProperty(hidden = true)
    private Date createTime;
    @JSONField(serialize = false)
    @ApiModelProperty(hidden = true)
    private Long createUser;
    @JSONField(serialize = false)
    @ApiModelProperty(hidden = true)
    private Long updateUser;
    @JSONField(serialize = false)
    @ApiModelProperty(hidden = true)
    private Date updateTime;
    @JSONField(serialize = false)
    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
}
