package cn.atsoft.dasheng.erp.model.result;

import lombok.Data;

import java.util.List;

@Data
public class SkuRequest {
    private String attribute;
    private List<String> value;
}
