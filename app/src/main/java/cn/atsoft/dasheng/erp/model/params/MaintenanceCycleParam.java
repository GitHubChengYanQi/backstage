package cn.atsoft.dasheng.erp.model.params;

import lombok.Data;
import cn.atsoft.dasheng.model.validator.BaseValidatingParam;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 物料维护周期
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-07-08
 */
@Data
@ApiModel
public class MaintenanceCycleParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;


    /**
     * 养护周期id
     */
    @ApiModelProperty("养护周期id")
    private Long maintenanceCycleId;

    /**
     * 物料id
     */
    @ApiModelProperty("物料id")
    private Long skuId;

    /**
     * 养护周期(天)
     */
    @ApiModelProperty("养护周期(天)")
    private Integer maintenancePeriod;

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

    /**
     * 部门id
     */
    @ApiModelProperty("部门id")
    private Long deptId;

    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;

    @Override
    public String checkParam() {
        return null;
    }

}
