package cn.atsoft.dasheng.storehouse.model.result;

import lombok.Data;
import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
/**
 * <p>
 * 仓库物料分类绑定表
 * </p>
 *
 * @author Captain_Jazz
 * @since 2023-05-15
 */
@Data
@ApiModel
public class StorehouseSpuClassBindResult implements Serializable {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty("")
    private Long storehouseSpuClassBindId;

    @ApiModelProperty("")
    private Long spuClassId;

    @ApiModelProperty("")
    private Long storehouseId;

    /**
     * 创建时间
     */
    @ApiModelProperty(hidden = true)
    private Date createTime;

    @ApiModelProperty("")
    private Long tenantId;

    /**
     * 创建者
     */
    @ApiModelProperty(hidden = true)
    private Long createUser;

    /**
     * 修改时间
     */
    @ApiModelProperty(hidden = true)
    private Date updateTime;

    /**
     * 修改者
     */
    @ApiModelProperty(hidden = true)
    private Long updateUser;

    /**
     * 状态
     */
    @ApiModelProperty("状态")
    private Integer display;

    /**
     * 部门编号
     */
    @ApiModelProperty("部门编号")
    private Long deptId;
    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
}
