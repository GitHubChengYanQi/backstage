package cn.atsoft.dasheng.Excel.pojo;

import cn.atsoft.dasheng.Excel.service.excelEntity;
import cn.atsoft.dasheng.app.model.result.BrandResult;
import cn.atsoft.dasheng.appBase.model.result.MediaResult;
import cn.atsoft.dasheng.erp.model.params.SkuJson;
import cn.atsoft.dasheng.erp.model.result.SpuResult;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SkuExcelExportPojo  implements excelEntity {
    List<BrandResult> brandResults;
    @ApiModelProperty("id")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long skuId;

    @ApiModelProperty("sku名字")
    private String standard;

    @ApiModelProperty("产品名称")
    private String skuMessage;

//    @ApiModelProperty("品牌")
//    private String brandName;

    @ApiModelProperty("型号")
    private String model;

    @ApiModelProperty("零件号")
    private String partNo;

    @ApiModelProperty("颜色")
    private String color;

    @ApiModelProperty("单位名称")
    private String unitName;

    @ApiModelProperty("规格参数")
    private String attributeAndValues;

    @ApiModelProperty("热处理")
    private String heatTreatment;

    @ApiModelProperty("等级")
    private String level;

    @ApiModelProperty("包装方式")
    private String packaging;

    @ApiModelProperty("规格")
    private String specifications;

    @ApiModelProperty("国家标准")
    private String nationalStandard;
//
//    @ApiModelProperty("sku名字")
//    private List<String> materials;

    @ApiModelProperty("尺寸")
    private String skuSize;


}
