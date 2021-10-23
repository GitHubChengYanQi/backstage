package cn.atsoft.dasheng.erp.model.params;

import cn.atsoft.dasheng.app.model.params.Attribute;
import cn.atsoft.dasheng.erp.entity.AttributeValues;
import lombok.Data;

import java.util.List;

@Data
public class SpuRequest {
    private List<Attribute>  spuRequests;
    private List<AttributeValues> value;
}
