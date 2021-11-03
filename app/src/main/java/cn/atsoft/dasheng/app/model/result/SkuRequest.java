package cn.atsoft.dasheng.app.model.result;

import cn.atsoft.dasheng.app.model.params.Attribute;
import cn.atsoft.dasheng.app.model.params.Values;
import com.sun.jna.platform.win32.Winspool;
import lombok.Data;

@Data
public class SkuRequest {
    private Long attributeId;
    private Long attributeValuesId;
}
