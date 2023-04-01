package cn.atsoft.dasheng.erp.model.params;

import cn.atsoft.dasheng.base.dict.AbstractDictMap;
import cn.atsoft.dasheng.erp.model.request.SkuAttributeAndValue;
import cn.atsoft.dasheng.erp.pojo.SelectBomEnum;
import cn.atsoft.dasheng.generalForm.model.params.GeneralFormDataParam;
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
public class SkuCopyCheckParam extends AbstractDictMap implements Serializable, BaseValidatingParam {

    private List<Long> brandIds;
    private Long skuId;
    private List<Long> imageIds;
    private List<Long> enclosureIds;
    private List<Long> fileIds;
    private SpuParam spu;
    private Long spuClassificationId;
    private Long spuStandard;
    private Long spuClass;
    private Long unitId;
    private Integer batch;
    private String name;
    private Integer maintenancePeriod;
    private String fileId;
    private List<SkuAttributeAndValue> sku;
    private String enclosure;   //附件
    private String model;//型号
    private String packaging;//包装方式


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
     * spu id
     */
    @ApiModelProperty("spu id")
    private Long spuId;

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
