package cn.atsoft.dasheng.app.model.params;

import lombok.Data;
import cn.atsoft.dasheng.model.validator.BaseValidatingParam;

import java.util.Date;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * <p>
 * 行业表
 * </p>
 *
 * @author
 * @since 2021-08-02
 */
@Data
@ApiModel
public class CrmIndustryParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;


    /**
     * 行业id
     */
    @ApiModelProperty("行业id")
    private Long industryId;


    private Integer sort;
    /**
     * 行业名称
     */
    @ApiModelProperty("行业名称")
    private String industryName;

    /**
     * 上级
     */
    @ApiModelProperty("上级")
    private Long parentId;

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

    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
    private Long deptId;

    @Override
    public String checkParam() {
        return null;
    }

}
