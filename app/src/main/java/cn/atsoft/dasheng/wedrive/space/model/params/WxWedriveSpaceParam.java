package cn.atsoft.dasheng.wedrive.space.model.params;

import cn.atsoft.dasheng.wedrive.space.model.enums.TypeEnum;
import lombok.Data;
import cn.atsoft.dasheng.model.validator.BaseValidatingParam;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 微信微盘空间
 * </p>
 *
 * @author Captain_Jazz
 * @since 2023-03-31
 */
@Data
@ApiModel
public class WxWedriveSpaceParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;

    List<Long> userIds;


    /**
     * 空间id
     */
    @ApiModelProperty("空间id")
    private String spaceId;

    /**
     * 空间id
     */
    @ApiModelProperty("类型")
    private TypeEnum type;

    /**
     * 空间名称
     */
    @ApiModelProperty("空间名称")
    private String spaceName;

    /**
     * 权限
     */
    @ApiModelProperty("权限")
    private String authList;

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

    @Override
    public String checkParam() {
        return null;
    }

}
