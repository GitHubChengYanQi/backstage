package cn.atsoft.dasheng.erp.model.result;

import cn.atsoft.dasheng.app.entity.Brand;
import cn.atsoft.dasheng.app.model.result.BrandResult;
import cn.atsoft.dasheng.appBase.model.result.MediaResult;
import cn.atsoft.dasheng.appBase.model.result.MediaUrlResult;
import cn.atsoft.dasheng.erp.model.params.SkuJson;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class SkuSimpleResult {
    /**
     * sku名字
     */
    List<BrandResult> brandResults;
    @ApiModelProperty("sku名字")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long skuId;

    private List<String> imgThumbUrls;//缩略图

    List<MediaResult> imgResults;


    private Integer maintenancePeriod;

    private Integer lockStockDetailNumber;

    private String skuName;

    private List<SkuJson> skuJsons;

    private String standard;

    private SpuResult spuResult;

    @ApiModelProperty("规格")
    public String specifications;

    private Long stockNumber;

    private Integer type;

    private Integer batch;
    
    private Boolean inBom;

    private Long qualityPlanId;

  private  List<String> imgUrls;


}
