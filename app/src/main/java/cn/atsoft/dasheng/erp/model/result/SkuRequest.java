package cn.atsoft.dasheng.erp.model.result;

import cn.atsoft.dasheng.erp.entity.AttributeValues;
import lombok.Data;

import java.util.List;

@Data
public class SkuRequest {
    private String attribute;
    private List<AttributeValues> value;
}
