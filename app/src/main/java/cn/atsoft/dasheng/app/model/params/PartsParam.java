package cn.atsoft.dasheng.app.model.params;

import lombok.Data;
import cn.atsoft.dasheng.model.validator.BaseValidatingParam;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 清单
 * </p>
 *
 * @author song
 * @since 2021-10-21
 */
@Data
@ApiModel
public class PartsParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;


    /**
     * 清单id
     */
    @ApiModelProperty("清单id")
    private Long partsId;

    private List<PartsAttribute> partsAttributes;

    /**
     * 物品id
     */
    @ApiModelProperty("物品id")
    private Long spuId;

    /**
     * 规格描述
     */
    @ApiModelProperty("规格描述")
    private String attribute;

    private List<PartsParam> parts;

    /**
     * 组成物品id
     */
    @ApiModelProperty("组成物品id")
    private Long pid;

    /**
     * 零件数量
     */
    @ApiModelProperty("零件数量")
    private Integer number;

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

    /**
     * 零件名称
     */
    @ApiModelProperty("零件名称")
    private String partName;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String note;

    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;

    @Override
    public String checkParam() {
        return null;
    }

}
