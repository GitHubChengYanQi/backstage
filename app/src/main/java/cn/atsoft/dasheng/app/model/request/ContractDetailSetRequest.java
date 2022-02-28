package cn.atsoft.dasheng.app.model.request;

import cn.atsoft.dasheng.app.entity.ContractDetail;
import cn.atsoft.dasheng.app.model.result.ContractDetailResult;
import lombok.Data;

import java.util.List;

@Data
public class ContractDetailSetRequest {
    private Long skuId;
    private Long quantity;
    private Long brandId;
    private Long customerId;
    List<ContractDetailResult> children;
}
