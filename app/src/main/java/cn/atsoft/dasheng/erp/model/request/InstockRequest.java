package cn.atsoft.dasheng.erp.model.request;

import lombok.Data;

import java.util.List;

@Data

public class InstockRequest {
    private Long ids;
    private String type;
    private Long positionsId;
}
