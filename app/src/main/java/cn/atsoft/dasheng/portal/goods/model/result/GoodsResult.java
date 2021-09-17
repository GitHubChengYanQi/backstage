package cn.atsoft.dasheng.portal.goods.model.result;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
/**
 * <p>
 * 首页商品
 * </p>
 *
 * @author siqiang
 * @since 2021-08-19
 */
@Data
@ApiModel
public class GoodsResult implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 商品id
     */
    @ApiModelProperty("商品id")
    private Long goodId;

    /**
     * 商品名称
     */
    @ApiModelProperty("商品名称")
    private String goodName;

    /**
     * 商品标题
     */
    @ApiModelProperty("商品标题")
    private String title;

    /**
     * 图片路径
     */
    @ApiModelProperty("图片路径")
    private String imgUrl;

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
     * 评论
     */
    @ApiModelProperty("评论")
    private String comment;

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
     * 部门id
     */
    @ApiModelProperty("部门Id")
    private Long deptId;
    /**
     * 状态
     */
    @ApiModelProperty("状态")
    private Integer display;
    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
}
