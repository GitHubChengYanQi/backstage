package cn.atsoft.dasheng.action.model.param;

import cn.atsoft.dasheng.action.Enum.ReceiptsEnum;
import cn.atsoft.dasheng.form.model.params.DocumentsStatusParam;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class StatusParam {

    ReceiptsEnum receiptsEnum;

    @NotNull
    private DocumentsStatusParam param;
}
