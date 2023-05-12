package cn.atsoft.dasheng.sys.modular.system.model.params;

import lombok.Data;
import cn.atsoft.dasheng.model.validator.BaseValidatingParam;
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
 * @since 2023-05-09
 */
@Data
@ApiModel
public class TenantBindLogParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;

    private Long tenantInviteLogId;

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

    @Override
    public String checkParam() {
        return null;
    }

}
