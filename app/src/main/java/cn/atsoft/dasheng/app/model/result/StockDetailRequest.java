package cn.atsoft.dasheng.app.model.result;

import cn.atsoft.dasheng.erp.model.result.BackSku;
import lombok.Data;

import java.util.List;

@Data
public class StockDetailRequest {
    private List<BackSku> backSkus;
}
