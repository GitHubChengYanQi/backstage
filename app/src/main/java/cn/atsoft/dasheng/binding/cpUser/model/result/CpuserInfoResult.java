package cn.atsoft.dasheng.binding.cpUser.model.result;

import lombok.Data;
import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
/**
 * <p>
 * 
 * </p>
 *
 * @author 
 * @since 2021-10-12
 */
@Data
@ApiModel
public class CpuserInfoResult implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 用户和openid关联
     */
    @ApiModelProperty("用户和openid关联")
    private Long cpUserId;

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
     * 会员编号
     */
    @ApiModelProperty("会员编号")
    private Long memberId;

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
     * 状态
     */
    @ApiModelProperty("状态")
    private Integer display;

    /**
     * 部门id
     */
    @ApiModelProperty("部门id")
    private Long deptId;
    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
}
