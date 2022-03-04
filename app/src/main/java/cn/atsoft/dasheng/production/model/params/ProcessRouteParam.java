package cn.atsoft.dasheng.production.model.params;

import lombok.Data;
import cn.atsoft.dasheng.model.validator.BaseValidatingParam;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 工艺路线列表
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-02-15
 */
@Data
@ApiModel
public class ProcessRouteParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;


    /**
     * 工艺路线id
     */
    @ApiModelProperty("工艺路线id")
    private Long processRouteId;

    @NotNull
    private Long skuId;

    private Long spuId;

    private Integer shipNumber;

    /**
     * 工艺路线编号
     */
    @ApiModelProperty("工艺路线编号")
    private String processRouteCoding;

    /**
     * 工艺路线名称
     */
    @ApiModelProperty("工艺路线名称")
    private String processRoteName;

    /**
     * 关联工艺物料清单
     */
    @ApiModelProperty("关联工艺物料清单")
    private Long partsId;

    /**
     * 版本号
     */
    @ApiModelProperty(hidden = true)
    private Long version;

    /**
     * 状态
     */
    @ApiModelProperty("状态")
    private Integer status;

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

    @Override
    public String checkParam() {
        return null;
    }

}
