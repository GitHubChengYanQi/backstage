package cn.atsoft.dasheng.erp.pojo;

import cn.atsoft.dasheng.erp.entity.Inkind;
import cn.atsoft.dasheng.erp.entity.Sku;
import cn.atsoft.dasheng.erp.model.result.InkindResult;
import cn.atsoft.dasheng.orCode.entity.OrCodeBind;
import lombok.Data;

@Data
public class printInkindRequest {
    private Sku sku;
    private InkindResult inkindResult;
    private OrCodeBind orCodeBind;
}
