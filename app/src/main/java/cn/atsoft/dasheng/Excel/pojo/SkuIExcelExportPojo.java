package cn.atsoft.dasheng.Excel.pojo;

import cn.atsoft.dasheng.Excel.service.IExcelEntity;
import cn.atsoft.dasheng.app.model.result.BrandResult;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class SkuIExcelExportPojo implements IExcelEntity {
    List<BrandResult> brandResults;

    @ApiModelProperty("物料编码")
    private String standard;

    @ApiModelProperty("产品名称")
    private String spu;

//    @ApiModelProperty("品牌")
//    private String brandName;

    @ApiModelProperty("型号")
    private String model;

    @ApiModelProperty("零件号")
    private String partNo;

    @ApiModelProperty("颜色")
    private String color;

    @ApiModelProperty("单位")
    private String unitId;

    @ApiModelProperty("产品码")
    private String spuCoding;

    @ApiModelProperty("物料描述")
    private String sku;

    @ApiModelProperty("材质")
    private String materialId;

    @ApiModelProperty("热处理")
    private String heatTreatment;

    @ApiModelProperty("规格参数")
    private String attributeAndValues;

    @ApiModelProperty("等级")
    private String level;

    @ApiModelProperty("养护周期")
    private String maintenancePeriod;

    @ApiModelProperty("备注")
    private String remarks;

    @ApiModelProperty("二维码生成方式")
    private String batch;

    @ApiModelProperty("包装方式")
    private String packaging;

    @ApiModelProperty("规格")
    private String specifications;

    @ApiModelProperty("国家标准")
    private String nationalStandard;

    @ApiModelProperty("尺寸")
    private String skuSize;

    @ApiModelProperty("图幅")
    private String viewFrame;
}
