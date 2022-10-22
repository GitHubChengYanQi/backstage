package cn.atsoft.dasheng.app.model.request;

import cn.atsoft.dasheng.erp.model.result.SkuSimpleResult;
import cn.atsoft.dasheng.sys.modular.system.model.result.UserResult;
import lombok.Data;

@Data
public class OutStockView {
    private Long pickListsId;
    private Long userId;
//    private UserResult userResult;
//    private Integer detailCount;
//    private Integer doneNumber;
    private SkuSimpleResult skuResult;
    private Integer pickSkuCount;
    private Integer pickNumCount;
    private Integer outSkuCount;
    private Integer outNumCount;
}
