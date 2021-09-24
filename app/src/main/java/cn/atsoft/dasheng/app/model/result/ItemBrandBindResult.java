package cn.atsoft.dasheng.app.model.result;

import cn.atsoft.dasheng.app.entity.Brand;
import lombok.Data;
import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
/**
 * <p>
 * 商品品牌绑定表
 * </p>
 *
 * @author 
 * @since 2021-09-24
 */
@Data
@ApiModel
public class ItemBrandBindResult implements Serializable {

    private static final long serialVersionUID = 1L;
    private BrandResult BrandResult;
    private String brandName;

    /**
     * 产品品牌绑定表
     */
    @ApiModelProperty("产品品牌绑定表")
    private Long itemBrandBindId;

    /**
     * 商品id
     */
    @ApiModelProperty("商品id")
    private Long itemId;

    /**
     * 品牌id
     */
    @ApiModelProperty("品牌id")
    private Long brandId;

    @ApiModelProperty(hidden = true)
    private Date createTime;

    @ApiModelProperty(hidden = true)
    private Long createUser;

    @ApiModelProperty(hidden = true)
    private Date updateTime;

    @ApiModelProperty(hidden = true)
    private Long updateUser;

    @ApiModelProperty("")
    private Integer display;

    /**
     * 部门编号
     */
    @ApiModelProperty("部门编号")
    private Long deptId;
    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
}
