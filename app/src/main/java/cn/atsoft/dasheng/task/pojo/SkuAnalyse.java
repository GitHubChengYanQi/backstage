package cn.atsoft.dasheng.task.pojo;

import lombok.Data;

/**
 * 物料分析格式化
 */
@Data
public class SkuAnalyse {
    private Long skuId;
    private String className;
    private Long spuId;
    private Long spuClassId;
}
