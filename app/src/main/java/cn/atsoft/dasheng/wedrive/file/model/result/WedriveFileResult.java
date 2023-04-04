package cn.atsoft.dasheng.wedrive.file.model.result;

import lombok.Data;
import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
/**
 * <p>
 * 微信微盘文件管理
 * </p>
 *
 * @author Captain_Jazz
 * @since 2023-04-01
 */
@Data
@ApiModel
public class WedriveFileResult implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 文件id
     */
    @ApiModelProperty("文件id")
    private String fileId;

    /**
     * 文件名称
     */
    @ApiModelProperty("文件名称")
    private String fileName;

    /**
     * 空间id
     */
    @ApiModelProperty("空间id")
    private String spaceId;

    /**
     * 父目录fileid,在根目录时为空间id
     */
    @ApiModelProperty("父目录fileid,在根目录时为空间id")
    private String fatherId;

    /**
     * 分类
     */
    @ApiModelProperty("分类")
    private String type;

    /**
     * 创建者
     */
    @ApiModelProperty(hidden = true)
    private Long createUser;

    /**
     * 部门编号
     */
    @ApiModelProperty("部门编号")
    private Long deptId;

    /**
     * 状态
     */
    @ApiModelProperty("状态")
    private Integer display;

    /**
     * 修改时间
     */
    @ApiModelProperty(hidden = true)
    private Date updateTime;

    /**
     * 创建时间
     */
    @ApiModelProperty(hidden = true)
    private Date createTime;

    /**
     * 修改者
     */
    @ApiModelProperty(hidden = true)
    private Long updateUser;
    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
}
