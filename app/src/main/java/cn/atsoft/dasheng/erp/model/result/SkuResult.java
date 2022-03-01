package cn.atsoft.dasheng.erp.model.result;

import cn.atsoft.dasheng.app.entity.Unit;
import cn.atsoft.dasheng.app.model.result.BrandResult;
import cn.atsoft.dasheng.erp.entity.QualityPlan;
import cn.atsoft.dasheng.erp.entity.SkuValues;
import cn.atsoft.dasheng.erp.entity.Spu;
import cn.atsoft.dasheng.erp.entity.SpuClassification;
import cn.atsoft.dasheng.erp.model.params.SkuJson;
import cn.atsoft.dasheng.erp.model.params.SkuRequest;
import cn.atsoft.dasheng.erp.model.params.SpuParam;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.util.Date;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;
import java.util.Map;

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

    List<AttributeValuesResult> list;
    private List<SkuJson> skuJsons;
    private String categoryName;
    private SpuResult spuResult;
    private String spuName;
    private Long partsId;
    private String createUserName;
    private String skuTextValue;
    private Long spuClass;
    private Long brandId;
    @ApiModelProperty("")
    private Long skuId;
    private Spu spu;
    private QualityPlan qualityPlan;
    private Unit unit;
    private SpuClassification spuClassification;
    private Long fileId;
    private Integer batch;
    private SkuRequest skuTree;
    private SpuClassification skuClass;
    private List<BrandResult> brandResultList;
    private BrandResult brandResult;
    private User user;
    private Boolean inBom;

    /**
     * 之间方案id
     */
    private Long qualityPlanId;

    /**
     * 编码
     */
    private String coding;
    /**
     * 类型
     */
    private Integer type;
    private Integer addMethod;
    /**
     * sku名字
     */
    @ApiModelProperty("sku名字")
    private String skuName;

    @ApiModelProperty("是否禁用")
    private String isBan;

    @ApiModelProperty("sku唯一标识")
    private String skuValue;
    @ApiModelProperty("备注")
    private String remarks;
    @ApiModelProperty("sku加密")
    private String skuValueMd5;
    @ApiModelProperty("执行标准")
    private String standard;


    /**
     * spu id
     */
    @ApiModelProperty("spu id")
    private Long spuId;
    @ApiModelProperty("规格")
    public String specifications;
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
