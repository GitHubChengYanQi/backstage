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
 *
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-08-11
 */
@Data
@ApiModel
public class MaintenanceLogDetailParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;
    private List<MaintenanceLogDetailParam> maintenanceLogDetailParams;
    private Long storehousePositionsId;

    @ApiModelProperty("")
    private Long maintenanceLogDetailId;

    @ApiModelProperty("")
    private Long maintenanceLogId;

    @ApiModelProperty("")
    private Long inkindId;

    @ApiModelProperty("")
    private Long skuId;

    /**
     * 品牌id
     */
    @ApiModelProperty("品牌id")
    private Long brandId;

    /**
     * 数量
     */
    @ApiModelProperty("数量")
    private Integer number;

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

    @ApiModelProperty("")
    private Long maintenanceDetailId;

    @ApiModelProperty("")
    private Long maintenanceId;

    /**
     * 附件
     */
    @ApiModelProperty("附件")
    private String enclosure;

    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;

    @Override
    public String checkParam() {
        return null;
    }

}
