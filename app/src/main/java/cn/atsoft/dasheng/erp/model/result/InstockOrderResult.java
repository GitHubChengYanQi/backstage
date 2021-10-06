package cn.atsoft.dasheng.erp.model.result;

import cn.atsoft.dasheng.app.model.result.StorehouseResult;
import cn.atsoft.dasheng.sys.modular.system.model.result.UserResult;
import lombok.Data;

import java.util.Date;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * <p>
 * 入库单
 * </p>
 *
 * @author song
 * @since 2021-10-06
 */
@Data
@ApiModel
public class InstockOrderResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private UserResult userResult;

    private StorehouseResult storehouseResult;

    @ApiModelProperty("仓库id")
    private Long storeHouseId;

    private Date time;
    /**
     * 入库单
     */
    @ApiModelProperty("入库单")
    private Long instockOrderId;

    /**
     * 负责人
     */
    @ApiModelProperty("负责人")
    private Long userId;

    @ApiModelProperty(hidden = true)
    private Date createTime;

    @ApiModelProperty(hidden = true)
    private Long createUser;

    @ApiModelProperty(hidden = true)
    private Date updateTime;

    @ApiModelProperty(hidden = true)
    private Long updateUser;

    @ApiModelProperty("")
    private Integer display;

    /**
     * 部门编号
     */
    @ApiModelProperty("部门编号")
    private Long deptId;

    /**
     * 入库状态
     */
    @ApiModelProperty("入库状态")
    private Boolean state;
    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
}
