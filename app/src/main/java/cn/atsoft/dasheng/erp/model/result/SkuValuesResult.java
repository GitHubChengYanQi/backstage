package cn.atsoft.dasheng.erp.model.result;

import lombok.Data;
import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
/**
 * <p>
 * sku详情表
 * </p>
 *
 * @author 
 * @since 2021-10-18
 */
@Data
@ApiModel
public class SkuValuesResult implements Serializable {

    private static final long serialVersionUID = 1L;
    private AttributeValuesResult attributeValuesResult;

    @ApiModelProperty("")
    private Long skuDetailId;

    @ApiModelProperty("")
    private Long skuId;

    /**
     * 属性Id
     */
    @ApiModelProperty("属性Id")
    private Long attributeId;

    /**
     * 属性值id
     */
    @ApiModelProperty("属性值id")
    private Long attributeValuesId;

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
