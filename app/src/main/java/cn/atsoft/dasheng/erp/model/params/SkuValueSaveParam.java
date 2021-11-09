package cn.atsoft.dasheng.erp.model.params;

import cn.atsoft.dasheng.erp.entity.AttributeValues;
import lombok.Data;

import java.util.List;

@Data
public class SkuValueSaveParam {
    private List<AttributeValues> valuesList;
    private String valueName;
}
