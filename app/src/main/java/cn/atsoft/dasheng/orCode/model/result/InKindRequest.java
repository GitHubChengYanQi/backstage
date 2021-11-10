package cn.atsoft.dasheng.orCode.model.result;

import cn.atsoft.dasheng.app.model.params.InstockParam;
import lombok.Data;

@Data
public class InKindRequest {
    private Long codeId;
    private Long Id;
    private String type;
    private InstockParam instockParam;

    private Long sorehousePositionsId;

}
