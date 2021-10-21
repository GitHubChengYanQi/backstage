package cn.atsoft.dasheng.erp.model.params;

import lombok.Data;

import java.util.List;

@Data
public class SkuJson {
    private List<SkuValuesRequest> skuValuesRequests;
}
