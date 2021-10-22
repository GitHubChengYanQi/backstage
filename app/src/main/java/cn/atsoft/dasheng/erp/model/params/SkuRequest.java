package cn.atsoft.dasheng.erp.model.params;

import lombok.Data;

import java.util.List;

@Data
public class SkuRequest {
    List<List<SkuJson>> spuRequests;
}
