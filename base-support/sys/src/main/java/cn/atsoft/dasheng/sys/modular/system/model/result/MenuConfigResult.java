package cn.atsoft.dasheng.sys.modular.system.model.result;

import lombok.Data;
import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
/**
 * <p>
 * 菜单显示配置表
 * </p>
 *
 * @author Captain_Jazz
 * @since 2023-04-28
 */
@Data
@ApiModel
public class MenuConfigResult implements Serializable {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty("")
    private Long configId;

    @ApiModelProperty("")
    private Long menuId;

    @ApiModelProperty("")
    private Integer hidden;

    @ApiModelProperty("")
    private Long tenantId;

    /**
     * 删除状态
     */
    @ApiModelProperty("删除状态")
    private Integer display;

    @ApiModelProperty(hidden = true)
    private Long createUser;

    @ApiModelProperty(hidden = true)
    private Long updateUser;

    @ApiModelProperty(hidden = true)
    private Date createTime;

    @ApiModelProperty(hidden = true)
    private Date updateTime;

    @ApiModelProperty("")
    private String source;
    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
}
