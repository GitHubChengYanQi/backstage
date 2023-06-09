package cn.atsoft.dasheng.app.pojo;

import cn.atsoft.dasheng.erp.model.result.SkuResult;
import lombok.Data;

@Data
public class AnalysisResult {
    private Long skuId;
    private String strand;
    private String SpuName;
    private String skuName;
    private String specifications;
    private String spuClass;
    private Long produceMix;   //生产数
    private Long lackNumber;  //缺料数
    private Long demandNumber; //需求数
    private Long stockNumber;
    private SkuResult skuResult;
}
