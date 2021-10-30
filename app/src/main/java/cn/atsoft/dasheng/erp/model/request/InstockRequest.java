package cn.atsoft.dasheng.erp.model.request;

import lombok.Data;

import java.util.List;

@Data

public class InstockRequest {
    private List<Long> ids;
    private String type;
}
