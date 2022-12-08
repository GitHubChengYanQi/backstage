package cn.atsoft.dasheng.erp.model.params;

import cn.atsoft.dasheng.app.model.params.Attribute;
import cn.atsoft.dasheng.base.dict.AbstractDictMap;
import cn.atsoft.dasheng.erp.entity.AttributeValues;
import cn.atsoft.dasheng.erp.entity.SkuValues;
import cn.atsoft.dasheng.erp.model.request.SkuAttributeAndValue;
import cn.atsoft.dasheng.erp.pojo.SearchObject;
import cn.atsoft.dasheng.erp.pojo.SelectBomEnum;
import cn.atsoft.dasheng.generalForm.entity.GeneralFormData;
import cn.atsoft.dasheng.generalForm.model.params.GeneralFormDataParam;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import cn.atsoft.dasheng.model.validator.BaseValidatingParam;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

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
public class SkuParam extends AbstractDictMap implements Serializable, BaseValidatingParam {



    private static final long serialVersionUID = 1L;
    private List<AttributeValuesParam> attributeValues;
    @ApiModelProperty("skuId")
    private List<Long> brandIds;
    private Long skuId;
    private Long customerId;
    private List<Long> skuIds = new ArrayList<>();
    private List<Long> imageIds;
    private List<Long> enclosureIds;
    private List<GeneralFormDataParam> generalFormDataParams;
    private List<Long> fileIds;
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
    private Integer maintenancePeriod;
    private String fileId;
    private SpuClassificationParam spuClassification;
    private String spuClassName;
    private Long oldSkuId;
    private Long partsSkuId;
    private List<SkuAttributeAndValue> sku;
    private String customerName;
    private String selectType;
    private List<Long> storehousePositionsIds;
    private Long storehousePositionsId;
    private Long storehouseId;
    private List<Long> customerIds;
    private List<Long> spuClassIds;  //物料分类查询条件
    private String fromType;
    private String thisMonth;   //本月
    private Integer timeWithin; //查询条件(几天内)
    private String startTime;
    private String endTime;
    private String enclosure;   //附件



    private List<Long> anomalySkuIds; //异常的物料
    private Boolean openBom = false;
    private Boolean openPosition = false;

    private String sort;
    private Map<String, String> sortMap;

    private Long status;
    /**
     * 是否查询仓库
     */
    private Boolean stockView = false;
    /**
     * 按库存数查询参数
     */
    private Integer mixNum;
    private Integer maxNum;
    /**
     * 按bom查询条件
     */
    private SelectBomEnum selectBom;
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


    /**
     * 热处理
     */
    private String heatTreatment;
    /**
     * 级别
     */
    private String level;
    /**
     * 表色
     */
    private String color;
    /**
     * 尺寸
     */
    private String skuSize;
    /**
     * 包装材料
     */
    private String packaging;
    /**
     * 重量
     */
    private String weight;
    /**
     * 材质id
     */
    private String materialId;
    /**
     * 图幅
     */
    private String viewFrame;
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
