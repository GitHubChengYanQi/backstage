package cn.atsoft.dasheng.goods.sku.model.params;

import cn.atsoft.dasheng.base.dict.AbstractDictMap;
import cn.atsoft.dasheng.form.model.params.RestGeneralFormDataParam;
import cn.atsoft.dasheng.goods.category.entity.RestCategory;
import cn.atsoft.dasheng.goods.classz.entity.RestClass;
import cn.atsoft.dasheng.goods.classz.model.RestSkuAttributeAndValue;
import cn.atsoft.dasheng.goods.classz.model.params.RestAttributeParam;
import cn.atsoft.dasheng.goods.classz.model.params.RestAttributeValuesParam;
import cn.atsoft.dasheng.goods.classz.model.params.RestClassParam;
import cn.atsoft.dasheng.goods.spu.model.RestSpuAttribute;
import cn.atsoft.dasheng.goods.spu.model.params.RestSpuParam;
import cn.atsoft.dasheng.model.validator.BaseValidatingParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
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
public class RestSkuParam implements Serializable, BaseValidatingParam {


    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "spu分类")
    private RestClassParam restClass;
    @ApiModelProperty(value = "spu分类")
    private RestSpuAttribute spuAttribute;
    @ApiModelProperty(value = "物料属性与属性值")
    private List<RestSkuAttributeAndValue> skuAttributeAndValues;
    @ApiModelProperty(value = "通用表单保存")
    private List<RestGeneralFormDataParam> generalFormDataParams;
    @ApiModelProperty(value = "spu分类")
    private Long spuClassificationId;
    @ApiModelProperty(value = "绑定品牌提交参数")
    private List<Long> brandIds;
    @ApiModelProperty(value = "spu分类")
    private RestSpuParam spu;

    private Integer initialNumber;

    /**
     * 编码
     */
    @ApiModelProperty(value = "物料编码",notes = "为空则根据规则自动生成编码")
    private String coding;

    @ApiModelProperty("spuId")
    private Long spuId;
    private Long spuClass;

    @ApiModelProperty("skuId")
    private Long skuId;

    @ApiModelProperty(value = "spu名称",notes = "如果spuId为空则spuName为必填，创建对应spu")
    private String spuName;

    @ApiModelProperty("型号名")
    private String name;

    @ApiModelProperty("型号名")
    private String skuName;

    @ApiModelProperty(value = "类目Id",notes = "如果为空则使用spuName创建类目")
    private Long classId;

    private List<RestAttributeParam> attribute;

    @ApiModelProperty("分类Id")
    private Long categoryId;

    @ApiModelProperty("物料单位Id")
    private Long unitId;

    @ApiModelProperty("材质")
    private Long texture;

    private String enclosure;   //附件

    private String model;//型号

    private String packaging;//包装方式

    private Long status;

    private Integer type;

    @ApiModelProperty("添加方式")
    private Integer addMethod;


    @ApiModelProperty("sku唯一标识")
    private String skuValue;

    @ApiModelProperty("备注")
    private String remarks;

    @ApiModelProperty("图片")
    private String images;

    private String drawing;

    @ApiModelProperty("sku加密")
    private String skuValueMd5;

    @ApiModelProperty("是否禁用")
    private Integer isBan;

    @ApiModelProperty("规格")
    public String specifications;

    @ApiModelProperty("执行标准")
    private String standard;
    @ApiModelProperty("文件")
    private String fileId;


    /**
     * 热处理
     */
    private String heatTreatment;
    /**
     * 养护周期
     */
    private Integer maintenancePeriod;
    /**
     * 级别
     */
    private String level;
    /**
     * 表色
     */
    private String color;
    /**
     * 图幅
     */
    private String viewFrame;
    /**
     * 尺寸
     */
    private String skuSize;
    /**
     * 重量
     */
    private String weight;
    /**
     * 材质id
     */
    private String materialId;
    /**
     * 零件号
     */
    private String partNo;
    /**
     * 国家标准
     */
    private String nationalStandard;

    /**
     * 之间方案id
     */
    private Long qualityPlanId;

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


    @Override
    public String checkParam() {
        return null;
    }

}
