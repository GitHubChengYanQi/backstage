package cn.atsoft.dasheng.app.model.result;

import cn.atsoft.dasheng.app.model.params.DeliveryDetailsParam;
import cn.atsoft.dasheng.app.model.params.OutstockParam;
import cn.atsoft.dasheng.app.model.params.StockDetailsParam;
import lombok.Data;

import java.util.List;

@Data
public class OutstockRequest {
    private Long adressId;
    private Long contactsId;
    private Long customerId;
    private Long phoneId;
    private  List<OutstockParam> ids ;
}
