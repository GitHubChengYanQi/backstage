package cn.atsoft.dasheng.erp.model.params;

import cn.atsoft.dasheng.app.model.params.Attribute;
import cn.atsoft.dasheng.erp.entity.AttributeValues;
import cn.atsoft.dasheng.erp.entity.SkuValues;
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
public class SkuParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;
    private List<AttributeValuesParam> attributeValues;
    @ApiModelProperty("")
    private Long skuId;
    List<Long> skuIds;
    private SpuParam spu;
    private Long spuClassificationId;
    /**
     * 类型
     */
    private SpuRequest spuAttributes;
    private Integer type;
    /**
     * sku名字
     */
    @ApiModelProperty("sku名字")
    private String skuName;
    @ApiModelProperty("sku唯一标识")
    private String skuValue;

    @ApiModelProperty("sku加密")
    private String skuValueMd5;
    @ApiModelProperty("是否禁用")
    private Integer isBan;
    @ApiModelProperty("规格")
    public String specifications;
    @ApiModelProperty("执行标准")
    private String standard;

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

}
