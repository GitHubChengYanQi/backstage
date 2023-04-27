package cn.atsoft.dasheng.miniapp.model.result;

import cn.atsoft.dasheng.sys.modular.system.model.result.TenantResult;
import cn.atsoft.dasheng.sys.modular.system.model.result.UserResult;
import lombok.Data;
import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
/**
 * <p>
 * 微信小程序配置表（对应租户）
 * </p>
 *
 * @author Captain_Jazz
 * @since 2023-04-25
 */
@Data
@ApiModel
public class WxMaConfigResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private TenantResult tenantResult;

    private UserResult userResult;

    /**
     * 主键
     */
    @ApiModelProperty("主键")
    private Long wxMaConfigId;

    /**
     * 租户id
     */
    @ApiModelProperty("租户id")
    private Long tenantId;

    @ApiModelProperty("")
    private String appid;

    @ApiModelProperty("")
    private String secret;

    @ApiModelProperty("")
    private String aesKey;

    @ApiModelProperty("")
    private String token;

    @ApiModelProperty("")
    private String msgDataFormat;

    @ApiModelProperty("")
    private Integer display;

    @ApiModelProperty(hidden = true)
    private Long createUser;

    @ApiModelProperty(hidden = true)
    private Long updateUser;

    @ApiModelProperty(hidden = true)
    private Date createTime;

    @ApiModelProperty(hidden = true)
    private Date updateTime;

    /**
     * 部门编号
     */
    @ApiModelProperty("部门编号")
    private Long deptId;
    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
}
