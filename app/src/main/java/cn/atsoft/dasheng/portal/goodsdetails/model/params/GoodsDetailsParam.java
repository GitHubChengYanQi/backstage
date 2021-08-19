package cn.atsoft.dasheng.portal.goodsdetails.model.params;

import cn.atsoft.dasheng.model.validator.BaseValidatingParam;
import lombok.Data;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 首页商品详情
 * </p>
 *
 * @author siqiang
 * @since 2021-08-19
 */
@Data
@ApiModel
public class GoodsDetailsParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;


    /**
     * 商品明细id
     */
    @ApiModelProperty("商品明细id")
    private String goodDetailsId;

    /**
     * 商品id
     */
    @ApiModelProperty("商品id")
    private Long goodId;

    /**
     * 商品轮播图id
     */
    @ApiModelProperty("商品轮播图id")
    private Long detailBannerId;

    /**
     * 商品标题
     */
    @ApiModelProperty("商品标题")
    private String title;

    /**
     * 商品售价
     */
    @ApiModelProperty("商品售价")
    private BigDecimal price;

    /**
     * 商品原价
     */
    @ApiModelProperty("商品原价")
    private BigDecimal lastPrice;

    /**
     * 服务
     */
    @ApiModelProperty("服务")
    private String server;

    /**
     * 规格id
     */
    @ApiModelProperty("规格id")
    private Long specificationId;

    /**
     * 商品详情
     */
    @ApiModelProperty("商品详情")
    private String details;

    /**
     * 排序
     */
    @ApiModelProperty("排序")
    private Long sort;

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
