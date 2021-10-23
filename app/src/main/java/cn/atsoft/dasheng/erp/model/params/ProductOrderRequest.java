package cn.atsoft.dasheng.erp.model.params;

import lombok.Data;

import java.util.List;

@Data
public class ProductOrderRequest {
    private List<ProductOrderDetailsParam> orderDetail;

    private Long contactsId;

    private Long adressId;

    private Long phoneId;

    private Long customerId;
}
