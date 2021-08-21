package cn.atsoft.dasheng.app.model.result;

import lombok.Data;

import java.util.List;

@Data
public class OutstockRequest {
    private Long adressId;
    private Long contactsId;
    private Long customerId;
    private Long phoneId;
    private List<Long> ids;
}
