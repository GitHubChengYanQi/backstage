package cn.atsoft.dasheng.binding.wxUser.model.result;

import cn.atsoft.dasheng.sys.modular.system.model.result.UserResult;
import lombok.Data;
import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
/**
 * <p>
 * 用户 小程序 关联
 * </p>
 *
 * @author 
 * @since 2021-08-24
 */
@Data
@ApiModel
public class WxuserInfoResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private UserResult userResult;

    private Long memberId;
    /**
     * 用户和openid关联
     */
    @ApiModelProperty("用户和openid关联")
    private Long userInfoId;

    /**
     * 用户id
     */
    @ApiModelProperty("用户id")
    private Long userId;

    /**
     * 用户第三方系统的唯一id
     */
    @ApiModelProperty("用户第三方系统的唯一id")
    private String uuid;

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
     * 部门id
     */
    @ApiModelProperty("部门Id")
    private Long deptId;

    /**
     * 状态
     */
    @ApiModelProperty("状态")
    private Integer display;
    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
}
