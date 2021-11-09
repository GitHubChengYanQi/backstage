package cn.atsoft.dasheng.app.model.result;

import cn.atsoft.dasheng.app.model.params.Attribute;
import cn.atsoft.dasheng.app.model.params.Values;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.model.result.AttributeInSpu;
import com.sun.jna.platform.win32.Winspool;
import lombok.Data;

@Data
public class SkuRequest implements Comparable<SkuRequest> {
    private Long attributeId;
    private Long attributeValuesId;

    @Override
    public int compareTo(SkuRequest o) {
        return (int)(this.attributeId - o.getAttributeId());
    }
}
