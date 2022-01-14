package cn.atsoft.dasheng.supplier.model.result;

import lombok.Data;
import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
/**
 * <p>
 * 供应商品牌绑定
 * </p>
 *
 * @author song
 * @since 2022-01-13
 */
@Data
@ApiModel
public class SupplierBrandResult implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 供应商 品牌绑定
     */
    @ApiModelProperty("供应商 品牌绑定")
    private Long supplierBrandId;

    /**
     * 供应商id
     */
    @ApiModelProperty("供应商id")
    private Long customerId;

    /**
     * 品牌id
     */
    @ApiModelProperty("品牌id")
    private Long brandId;

    /**
     * 删除状态
     */
    @ApiModelProperty("删除状态")
    private Integer display;

    @ApiModelProperty(hidden = true)
    private Long createUser;

    @ApiModelProperty(hidden = true)
    private Long updateUser;

    @ApiModelProperty(hidden = true)
    private Date createTime;

    @ApiModelProperty(hidden = true)
    private Date updateTime;
    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
}
