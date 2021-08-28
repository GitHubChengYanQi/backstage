package cn.atsoft.dasheng.portal.goodsDetailsBanner.model.params;

import lombok.Data;
import cn.atsoft.dasheng.model.validator.BaseValidatingParam;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 商品轮播图
 * </p>
 *
 * @author siqiang
 * @since 2021-08-19
 */
@Data
@ApiModel
public class GoodsDetailsBannerParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;


    /**
     * 轮播图id
     */
    @ApiModelProperty("轮播图id")
    private Long detailBannerId;

    /**
     * 商品明细id
     */
    @ApiModelProperty("商品明细id")
    private Long goodDetailsId;

    /**
     * 排序
     */
    @ApiModelProperty("排序")
    private Long sort;

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
