package cn.atsoft.dasheng.asyn.pojo;

import lombok.Data;

import java.util.List;

@Data
public class BomRotation {
    private String skuName;
    private List<SkuAnalyse> skuAnalyses;
}
