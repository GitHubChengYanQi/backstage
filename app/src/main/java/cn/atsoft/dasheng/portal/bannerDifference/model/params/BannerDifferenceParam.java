package cn.atsoft.dasheng.portal.bannerDifference.model.params;

import lombok.Data;
import cn.atsoft.dasheng.model.validator.BaseValidatingParam;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 轮播图分类
 * </p>
 *
 * @author 
 * @since 2021-08-18
 */
@Data
@ApiModel
public class BannerDifferenceParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;


    /**
     * 轮播图分类id
     */
    @ApiModelProperty("轮播图分类id")
    private Long classificationId;

    /**
     * 轮播图区别
     */
    @ApiModelProperty("轮播图区别")
    private String difference;

    /**
     * 创建时间
     */
    @ApiModelProperty(hidden = true)
    private Date createTime;

    /**
     * 创建者
     */
    @ApiModelProperty(hidden = true)
    private Long createUser;

    /**
     * 修改时间
     */
    @ApiModelProperty(hidden = true)
    private Date updateTime;

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
     * 部门id
     */
    @ApiModelProperty("部门Id")
    private Long deptId;

    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;

    @Override
    public String checkParam() {
        return null;
    }

}
