package cn.atsoft.dasheng.erp.model.params;

import cn.atsoft.dasheng.app.model.params.Attribute;
import cn.atsoft.dasheng.base.dict.AbstractDictMap;
import cn.atsoft.dasheng.erp.entity.AttributeValues;
import cn.atsoft.dasheng.erp.entity.SkuValues;
import cn.atsoft.dasheng.erp.model.request.SkuAttributeAndValue;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import cn.atsoft.dasheng.model.validator.BaseValidatingParam;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
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
public class SkuParam extends AbstractDictMap implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;
    private List<AttributeValuesParam> attributeValues;
    @ApiModelProperty("skuId")
    private List<Long> brandIds;
    private Long skuId;
    private Long customerId;
    private List<Long> skuIds;
    private SpuParam spu;
    private Long spuClassificationId;
    private Long spuStandard;
    private List<Long> id;
    private List<Long> noSkuIds;
    private Long spuClass;
    private Long unitId;
    private Integer batch;
    private String spuName;
    private String name;
    private String fileId;
    private SpuClassificationParam spuClassification;
    private String spuClassName;
    private Long oldSkuId;

    private List<SkuAttributeAndValue> sku;

    private String selectType;

    /**
     * 编码
     */
    private String coding;
    /**
     * 类型
     */
    private SpuRequest spuAttributes;
    private Integer type;
    @ApiModelProperty("添加方式")
    private Integer addMethod;
    /**
     * sku名字
     */
    @ApiModelProperty("sku名字")
    private String skuName;
    @ApiModelProperty("sku唯一标识")
    private String skuValue;
    @ApiModelProperty("备注")
    private String remarks;

    @ApiModelProperty("sku加密")
    private String skuValueMd5;
    @ApiModelProperty("是否禁用")
    private Integer isBan;
    @ApiModelProperty("规格")
    public String specifications;
    @ApiModelProperty("执行标准")
    private String standard;

    /**
     * 之间方案id
     */
    private Long qualityPlanId;

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

    @Override
    public String checkParam() {
        return null;
    }

    @Override
    public void init() {

    }

    @Override
    protected void initBeWrapped() {

    }
}
