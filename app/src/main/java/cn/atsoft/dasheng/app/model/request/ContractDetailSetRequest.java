package cn.atsoft.dasheng.app.model.request;

import cn.atsoft.dasheng.app.entity.ContractDetail;
import cn.atsoft.dasheng.app.model.result.BrandResult;
import cn.atsoft.dasheng.app.model.result.ContractDetailResult;
import cn.atsoft.dasheng.crm.model.result.OrderDetailResult;
import cn.atsoft.dasheng.erp.model.result.SkuResult;
import lombok.Data;

import java.util.List;

@Data
public class ContractDetailSetRequest {
    private Long skuId;
    private Long quantity;
    private Long brandId;
    private Long customerId;
    private SkuResult skuResult;
    private BrandResult brandResult;
    List<OrderDetailResult> children;
}
