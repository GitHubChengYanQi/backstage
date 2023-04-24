package cn.atsoft.dasheng.sys.modular.system.model.result;

import cn.atsoft.dasheng.media.model.result.MediaUrlResult;
import lombok.Data;
import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
/**
 * <p>
 * 系统租户表
 * </p>
 *
 * @author Captain_Jazz
 * @since 2023-04-07
 */
@Data
@ApiModel
public class TenantResult implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty("LOGO返回结果")
    private MediaUrlResult logoResult;

    @ApiModelProperty("")
    private Long tenantId;

    /**
     * 租户名称
     */
    @ApiModelProperty("租户名称")
    private String name;

    /**
     * 租户logo
     */
    @ApiModelProperty("租户logo")
    private Long logo;

    /**
     * 租户地址
     */
    @ApiModelProperty("租户地址")
    private String address;

    /**
     * 租户邮箱
     */
    @ApiModelProperty("租户邮箱")
    private String email;

    /**
     * 联系方式
     */
    @ApiModelProperty("联系方式")
    private String telephone;

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
