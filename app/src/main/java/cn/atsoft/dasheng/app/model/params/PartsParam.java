package cn.atsoft.dasheng.app.model.params;

import cn.atsoft.dasheng.app.model.result.Item;
import cn.atsoft.dasheng.app.model.result.SkuRequest;
import cn.atsoft.dasheng.erp.model.request.SkuAttributeAndValue;
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
 * 清单
 * </p>
 *
 * @author song
 * @since 2021-10-21
 */
@Data
@ApiModel
public class PartsParam implements Serializable, BaseValidatingParam {

    private Integer status;

    private static final long serialVersionUID = 1L;

    private String skuName;

    private List<SkuRequest> skuRequests;

    private String children;

    private String childrens;

    private String specifications;

    private List<SkuAttributeAndValue> sku;

    private Integer batch;

    private String skuNote;

    private String type;

    private List<ErpPartsDetailParam> parts;

    private Long pSkuId;

    private List<Long> skuIds;

    private Long skuId;

    private String standard;
    /**
     * 接參
     */
    private Item item;

    /**
     * 清单id
     */
    @NotNull
    @ApiModelProperty("清单id")
    private Long partsId;

    private List<Spu> partsAttributes;

    /**
     * 物料名称
     */
    @ApiModelProperty("物料名称")
    private Long spuId;

    /**
     * 规格描述
     */
    @ApiModelProperty("规格描述")
    private String attribute;


    /**
     * 组成物品id
     */
    @ApiModelProperty("组成物品id")
    private Long pid;

    /**
     * 零件数量
     */
    @ApiModelProperty("零件数量")
    private Integer number;

    /**
     * 创建时间
     */
    @ApiModelProperty(hidden = true)
    private Date createTime;

    /**
     * 修改时间
     */
    @ApiModelProperty(hidden = true)
    private Date updateTime;

    /**
     * 创建者
     */
    @ApiModelProperty(hidden = true)
    private Long createUser;

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
     * 部门编号
     */
    @ApiModelProperty("部门编号")
    private Long deptId;

    /**
     * 零件名称
     */
    @ApiModelProperty("零件名称")
    private String partName;

    @ApiModelProperty("关联sku")
    private String skus;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String note;

    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;

    @Override
    public String checkParam() {
        return null;
    }

}
