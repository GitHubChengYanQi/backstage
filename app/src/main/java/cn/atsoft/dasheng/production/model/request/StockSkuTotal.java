package cn.atsoft.dasheng.production.model.request;

import cn.atsoft.dasheng.erp.model.result.StorehousePositionsResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.omg.CORBA.LongHolder;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StockSkuTotal {

    private Long skuId;
    private Long number;

}
