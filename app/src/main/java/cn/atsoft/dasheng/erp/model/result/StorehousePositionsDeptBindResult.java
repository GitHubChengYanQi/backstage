package cn.atsoft.dasheng.erp.model.result;

import lombok.Data;
import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
/**
 * <p>
 * 库位权限绑定表
 * </p>
 *
 * @author 
 * @since 2022-01-25
 */
@Data
@ApiModel
public class StorehousePositionsDeptBindResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<Long> deptIds;
    @ApiModelProperty("")
    private String bindId;

    /**
     * 库位id
     */
    @ApiModelProperty("库位id")
    private Long storehousePositionsId;

    /**
     * 部门id
     */
    @ApiModelProperty("部门id")
    private Long deptId;

    /**
     * 创建者
     */
    @ApiModelProperty(hidden = true)
    private Long createUser;

    /**
     * 创建时间
     */
    @ApiModelProperty(hidden = true)
    private Date createTime;

    /**
     * 修改者
     */
    @ApiModelProperty(hidden = true)
    private Long updateUser;

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
    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
}
