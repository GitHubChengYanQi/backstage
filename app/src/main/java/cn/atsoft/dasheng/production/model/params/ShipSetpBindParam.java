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
 * 工序关联绑定工具与设备表
 * </p>
 *
 * @author song
 * @since 2022-02-10
 */
@Data
@ApiModel
public class ShipSetpBindParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;


    /**
     * 绑定表
     */
    @ApiModelProperty("绑定表")
    private Long shipSetpBindId;

    /**
     * 工序id
     */
    @ApiModelProperty("工序id")
    private Long shipSetpId;

    /**
     * 分类
     */
    @ApiModelProperty("分类")
    private String type;

    /**
     * 所属id
     */
    @ApiModelProperty("所属id")
    private Long fromId;

    /**
     * 是否常用
     */
    @ApiModelProperty("是否常用")
    private Integer isCommon;

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
