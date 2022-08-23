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
 * 养护记录
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-08-11
 */
@Data
@ApiModel
public class MaintenanceLogParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;

    private List<MaintenanceLogDetailParam> maintenanceLogDetailParams;

    private String coding;
    private Long storehousePositionsId;

    private List<String> times;


    @ApiModelProperty("")
    private Long maintenanceLogId;

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
