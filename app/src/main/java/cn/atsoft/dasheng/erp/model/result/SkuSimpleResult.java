package cn.atsoft.dasheng.erp.model.result;

import cn.atsoft.dasheng.erp.model.params.SkuJson;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class SkuSimpleResult {
    /**
     * sku名字
     */
    @ApiModelProperty("sku名字")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long skuId;

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


}
