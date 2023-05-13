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
 * 邀请记录  申请记录
 * </p>
 *
 * @author Captain_Jazz
 * @since 2023-05-09
 */
@Data
@ApiModel
public class TenantBindLogResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer isAdmin;
    private UserResult userResult;
    @ApiModelProperty("操作人")
    private UserResult updateUserResult;
    @ApiModelProperty("邀请人")
    private UserResult inviterUserResult;
    @ApiModelProperty("邀请人")
    private Dept dept;
    @ApiModelProperty("租户")
    private TenantResult tenantResult;
    @ApiModelProperty("部门集合")
    private List<Map<String,Object>> deptList;

    private Integer status;

    @ApiModelProperty("")
    private Long tenantBindLogId;

    @ApiModelProperty("")
    private Long userId;

    @ApiModelProperty("")
    private Long auditUser;

    @ApiModelProperty("")
    private Long tenantId;

    /**
     * 邀请记录/申请记录
     */
    @ApiModelProperty("邀请记录/申请记录")
    private String type;

    @ApiModelProperty("")
    private Long deleteUser;

    @ApiModelProperty("")
    private Date deleteTime;

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

    /**
     * 邀请人
     */
    @ApiModelProperty("邀请人")
    private Long inviterUser;

    /**
     * 部门id
     */
    @ApiModelProperty("部门id")
    private Long inviteDeptId;
    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
}
