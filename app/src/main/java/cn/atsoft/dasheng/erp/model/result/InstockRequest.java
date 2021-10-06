package cn.atsoft.dasheng.erp.model.result;

import lombok.Data;

import java.util.List;

@Data
public class InstockRequest {
    private Long brandId;
    private Long itemId;
    private Long number;
}
