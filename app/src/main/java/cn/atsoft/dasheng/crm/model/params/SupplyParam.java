package cn.atsoft.dasheng.crm.model.params;

import cn.atsoft.dasheng.app.model.params.BrandParam;
import lombok.Data;
import cn.atsoft.dasheng.model.validator.BaseValidatingParam;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 供应商供应物料
 * </p>
 *
 * @author song
 * @since 2021-12-20
 */
@Data
@ApiModel
public class SupplyParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;

    private List<SupplyParam> supplyParams;

    private List<Long> supplyIds;

    private String supplierModel;

    @NotNull
    private List<Long> brandIds;

    private String nameSource; //模糊查询

    private String name;       //模糊查询

    private Long levelId;

    private Long brandId;

    private List<BrandParam> brandParams;

    private List<Long> skuIds;

    private List<Long> noSkuIds;
    private List<Long> noBrandIds;
    private List<Long> noCustomerIds;

    @ApiModelProperty("")
    private Long supplyId;

    /**
     * sku
     */
    @NotNull
    @ApiModelProperty("sku")
    private Long skuId;

    /**
     * 供应商id
     */
    @ApiModelProperty("供应商id")
    @NotNull
    private Long customerId;

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

    @Override
    public String checkParam() {
        return null;
    }

}
