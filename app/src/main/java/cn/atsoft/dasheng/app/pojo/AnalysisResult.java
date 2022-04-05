package cn.atsoft.dasheng.app.pojo;

import lombok.Data;

@Data
public class AnalysisResult {
    private Long  skuId;
    private String strand;
    private String SpuName;
    private String skuName;
    private String specifications;
    private Long produceMix;   //生产数
    private Long lackNumber;  //缺料数
}
