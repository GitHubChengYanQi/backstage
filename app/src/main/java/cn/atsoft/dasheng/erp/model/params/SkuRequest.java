package cn.atsoft.dasheng.erp.model.params;

import cn.atsoft.dasheng.erp.model.result.AttributeInSpu;
import cn.atsoft.dasheng.erp.model.result.ItemAttributeResult;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class SkuRequest {
    private List<AttributeInSpu> tree;
    private List<Map<String,Object>> list;
}
