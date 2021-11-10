package cn.atsoft.dasheng.orCode.model.result;

import cn.atsoft.dasheng.app.model.params.InstockParam;
import cn.atsoft.dasheng.erp.model.params.InstockListParam;
import lombok.Data;

@Data
public class InKindRequest {
    private Long codeId;
    private Long Id;
    private String type;
    private InstockListParam instockListParam;

    private Long sorehousePositionsId;

}
