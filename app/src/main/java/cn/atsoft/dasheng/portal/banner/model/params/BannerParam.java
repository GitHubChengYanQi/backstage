package cn.atsoft.dasheng.portal.banner.model.params;

import cn.atsoft.dasheng.portal.banner.model.validator.BaseValidatingParam;
import lombok.Data;

import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;

/**
 * <p>
 * 轮播图
 * </p>
 *
 * @author 
 * @since 2021-08-17
 */
@Data
@ApiModel
public class BannerParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;


    /**
     * 轮播图id
     */
    @ApiModelProperty("轮播图id")
    private Long bannerId;

    /**
     * 轮播图标题
     */
    @ApiModelProperty("轮播图标题")
    private String title;

    /**
     * 图片路径
     */
    @ApiModelProperty("图片路径")
    private String imgUrl;

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

    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;

    @Override
    public String checkParam() {
        return null;
    }

}
