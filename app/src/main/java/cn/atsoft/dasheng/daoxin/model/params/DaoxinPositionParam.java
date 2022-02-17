package cn.atsoft.dasheng.daoxin.model.params;

import lombok.Data;
import cn.atsoft.dasheng.model.validator.BaseValidatingParam;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * daoxin职位表
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-02-17
 */
@Data
@ApiModel
public class DaoxinPositionParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;


    /**
     * 主键id
     */
    @ApiModelProperty("主键id")
    private Long positionId;

    /**
     * 职位名称
     */
    @ApiModelProperty("职位名称")
    private String name;

    /**
     * 职位编码
     */
    @ApiModelProperty("职位编码")
    private String code;

    /**
     * 顺序
     */
    @ApiModelProperty("顺序")
    private Integer sort;

    /**
     * 状态(字典)
     */
    @ApiModelProperty("状态(字典)")
    private String status;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remark;

    /**
     * 创建时间
     */
    @ApiModelProperty(hidden = true)
    private Date createTime;

    /**
     * 更新者
     */
    @ApiModelProperty(hidden = true)
    private Long updateUser;

    /**
     * 更新时间
     */
    @ApiModelProperty(hidden = true)
    private Date updateTime;

    /**
     * 创建者
     */
    @ApiModelProperty(hidden = true)
    private Long createUser;

    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;

    @Override
    public String checkParam() {
        return null;
    }

}
