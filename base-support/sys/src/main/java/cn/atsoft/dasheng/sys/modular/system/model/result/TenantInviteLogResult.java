package cn.atsoft.dasheng.sys.modular.system.model.result;

import cn.atsoft.dasheng.sys.modular.system.entity.Dept;
import lombok.Data;
import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
/**
 * <p>
 * 邀请记录  申请记录
 * </p>
 *
 * @author Captain_Jazz
 * @since 2023-05-12
 */
@Data
@ApiModel
public class TenantInviteLogResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private TenantResult tenantResult;

    private Dept dept;

    private UserResult userResult;

    @ApiModelProperty("")
    private Long tenantInviteLogId;

    @ApiModelProperty("")
    private Long tenantId;

    /**
     * 部门id
     */
    @ApiModelProperty("部门id")
    private Long deptId;

    /**
     * 删除状态
     */
    @ApiModelProperty("删除状态")
    private Integer display;

    /**
     * 邀请人
     */
    @ApiModelProperty("邀请人")
    private Long inviterUser;

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
