package cn.atsoft.dasheng.erp.model.result;

import cn.atsoft.dasheng.erp.entity.SkuValues;
import lombok.Data;

import java.util.Date;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * <p>
 * sku表
 * </p>
 *
 * @author
 * @since 2021-10-18
 */
@Data
@ApiModel
public class SkuResult implements Serializable {

    private static final long serialVersionUID = 1L;
    List<SkuValuesResult> skuValuesResults;

    private String categoryName;

    private String spuName;
    @ApiModelProperty("")
    private Long skuId;

    /**
     * sku名字
     */
    @ApiModelProperty("sku名字")
    private String skuName;

    /**
     * spu id
     */
    @ApiModelProperty("spu id")
    private Long spuId;

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
}
