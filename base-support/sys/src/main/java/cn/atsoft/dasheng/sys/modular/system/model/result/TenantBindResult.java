package cn.atsoft.dasheng.sys.modular.system.model.result;

import cn.atsoft.dasheng.sys.modular.system.entity.Dept;
import lombok.Data;
import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 租户用户绑定表
 * </p>
 *
 * @author Captain_Jazz
 * @since 2023-04-19
 */
@Data
@ApiModel
public class TenantBindResult implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty("是否是管理员")
    private Integer isAdmin;
    @ApiModelProperty("状态")
    private Integer status;
    @ApiModelProperty("用户")
    private UserResult userResult;
    @ApiModelProperty("操作人")
    private UserResult updateUserResult;
    @ApiModelProperty("租户")
    private TenantResult tenantResult;
    @ApiModelProperty("部门集合")
    private List<Map<String,Object>> deptList;
    /**
     * 租户绑定id
     */
    @ApiModelProperty("租户绑定id")
    private Long tenantBindId;

    /**
     * 租户id
     */
    @ApiModelProperty("租户id")
    private Long tenantId;

    /**
     * 系统用户id
     */
    @ApiModelProperty("系统用户id")
    private Long userId;

    /**
     * 删除状态
     */
    @ApiModelProperty("删除状态")
    private Integer display;

    /**
     * 创建人
     */
    @ApiModelProperty(hidden = true)
    private Long createUser;

    /**
     * 更新人
     */
    @ApiModelProperty(hidden = true)
    private Long updateUser;

    /**
     * 创建时间
     */
    @ApiModelProperty(hidden = true)
    private Date createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty(hidden = true)
    private Date updateTime;
    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
}
