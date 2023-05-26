package cn.atsoft.dasheng.erp.model.result;

import lombok.Data;

@Data
public class SkuCountByClassIdResult {
    private Long classId;

    private Integer skuCount;
}
