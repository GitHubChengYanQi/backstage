package cn.atsoft.dasheng.production.model.params;

import lombok.Data;
import cn.atsoft.dasheng.model.validator.BaseValidatingParam;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 工位表
 * </p>
 *
 * @author song
 * @since 2021-10-29
 */
@Data
@ApiModel
public class ProductionStationParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;
    private List<Long> userIds;


    /**
     * 工位id
     */
    @ApiModelProperty("工位id")
    private Long productionStationId;

    /**
     * 工位名称
     */
    @ApiModelProperty("工位名称")
    private String name;

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

    @Override
    public String checkParam() {
        return null;
    }

}
