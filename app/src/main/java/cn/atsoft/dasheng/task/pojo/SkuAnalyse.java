package cn.atsoft.dasheng.task.pojo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * 物料分析格式化
 */
@Data
public class SkuAnalyse {
    private String className;

    private Long number;

    private Long stockNumber;

    @JSONField(serialize = false)
    private Long skuId;

    @JSONField(serialize = false)
    private Long spuId;

    @JSONField(serialize = false)
    private Long spuClassId;

}
