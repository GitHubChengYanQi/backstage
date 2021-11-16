package cn.atsoft.dasheng.orCode.model.result;

import cn.atsoft.dasheng.app.model.result.OutstockOrderResult;
import cn.atsoft.dasheng.erp.model.result.QualityTaskResult;
import lombok.Data;

@Data
public class QualityRequest {
    private String type;
    private QualityTaskResult result;
}
